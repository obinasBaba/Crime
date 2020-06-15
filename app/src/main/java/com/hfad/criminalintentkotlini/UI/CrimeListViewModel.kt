package com.hfad.criminalintentkotlini.UI

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.SparseBooleanArray
import android.widget.Toast
import androidx.lifecycle.*
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.Model.DataManager
import com.hfad.criminalintentkotlini.Util.ActionModeCallback
import com.hfad.criminalintentkotlini.Util.RecyclerAdapter
import java.io.File
import java.lang.NullPointerException
import kotlin.math.roundToInt

class CrimeListViewModel(private val applicationCtx: Application, private val dataManager: DataManager ) : ViewModel() {


    private val crimeDetail: MutableLiveData<Crime> by lazy { MutableLiveData<Crime>() }
    var sparseBoolean : SparseBooleanArray = SparseBooleanArray()
    var crimeModified : MutableLiveData< Boolean > = MutableLiveData( false ) // Determine whether to update give crime or not
    val crimeList : LiveData< List<Crime> > = fetchCrimeListFromDB()
    var cachedCrime: Crime? = null // A crime use to compare for change
    var savedCrime: Crime? = null
    var cachedIndex: Int = -1

    val deleteSelectionPressed = Transformations.distinctUntilChanged( ActionModeCallback.deleteCounter )
    val deselectPressed = Transformations.distinctUntilChanged( ActionModeCallback.stopActionModeCounter )

    init {
        Toast.makeText( applicationCtx, "viewModel STARTED", Toast.LENGTH_LONG).show()
    }

    private fun fetchCrimeListFromDB() = dataManager.readBulk()

    fun getDetailCrimeLiveData(): LiveData<Crime> = crimeDetail

    fun getCrimesLiveData(): LiveData< List<Crime> > = crimeList

    fun queryCrimeById( newIndex: Int ): Crime {

        return if ( newIndex == cachedIndex )
            savedCrime ?: cachedCrime!!
        else {
            cachedIndex = newIndex
            cachedCrime ?: dataManager.queryCrimeById( newIndex ).also {
                cachedCrime = it
            }
        }
    }

    fun deleteCrimes(adapter: RecyclerAdapter){
        val willBeDeleted : MutableList< Crime > = ArrayList( )
        val size = sparseBoolean.size()
        IntRange( 0, size ).forEach { index ->
            if ( sparseBoolean.valueAt( index ) ) {
                val recyclerPosition = sparseBoolean.keyAt( index )
                val crimeAtThatPosition = adapter.getCrimeAtIndex( recyclerPosition )
                willBeDeleted.add( crimeAtThatPosition )
            }
        }
        dataManager.deleteCrimes( willBeDeleted )
    }

    fun deleteCrime( crime : Crime ){
        dataManager.deleteCrimes( listOf( crime ) )
    }

    // TODO -  RUN ON BACKGROUND
    fun updateCrime(crimeById: Crime) =
        dataManager.updateCrimeDb(crimeById ).also {
            crimeModified.value = false
        }
    fun createCrime( selectedCrime: Crime) =
        dataManager.addNewCrime( selectedCrime ).also {
           crimeModified.value = false
       }

    override fun onCleared() {
        Toast.makeText( applicationCtx, "MODEL VIEW DESTROYED", Toast.LENGTH_LONG).show()
        super.onCleared()
    }

    fun reverse() {
        savedCrime = null
        crimeModified = MutableLiveData( false )
        cachedCrime = null
        cachedIndex = -1
    }

    fun create(): Crime = cachedCrime ?: Crime().also { cachedCrime = it }
    fun update( id : Int ): Crime = cachedCrime ?: dataManager.queryCrimeById( id ).also { cachedCrime = it }
    fun getFileLocation(selectedCrime: Crime): File {
        return dataManager.getFile( selectedCrime )
    }

    fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap? {
        // Read in the dimensions of the image on disk
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        try {
            BitmapFactory.decodeFile(path, options)

            val srcWidth = options.outWidth.toFloat()
            val srcHeight = options.outHeight.toFloat()

            // Figure out how much to scale down by
            var inSampleSize = 1
            if (srcHeight > destHeight || srcWidth > destWidth) {
                val heightScale = srcHeight / destHeight
                val widthScale = srcWidth / destWidth

                val sampleScale = if (heightScale > widthScale) {
                    heightScale
                } else {
                    widthScale
                }
                inSampleSize = sampleScale.roundToInt()
            }

            options = BitmapFactory.Options()
            options.inSampleSize = inSampleSize

            return BitmapFactory.decodeFile(path, options)

        }catch ( nil : IllegalStateException ){
            Toast.makeText( applicationCtx, "NiL", Toast.LENGTH_SHORT ).show()
        }catch ( other : Exception ){
            Toast.makeText( applicationCtx, "$other", Toast.LENGTH_SHORT ).show()

        }
        // Read in and create final bitmap
        return null
    }

}
