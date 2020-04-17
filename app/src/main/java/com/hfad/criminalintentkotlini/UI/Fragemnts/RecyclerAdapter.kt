package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.database.ContentObserver
import android.database.Cursor
import android.database.DataSetObserver
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract
import com.hfad.criminalintentkotlini.Model.Database.CrimeDatabaseContract.*
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.TAG
import java.lang.IllegalStateException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapter private constructor (var cursor: Cursor?, var sparseBoolean: SparseBooleanArray)
    : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>()
{

    private var rowId : Int = -1
    private var titleIndex : Int = -1
    private var solvedIndex : Int = -1
    private var dateIndex : Int = -1
    private val dataSetObserver : DataSetObserver by lazy { DataSetObserverInner() }
    private var cursorIsValid : Boolean

    companion object {
        private var INSTANCE : RecyclerAdapter? = null
        fun getInstance( cursor: Cursor?, sparseBoolean: SparseBooleanArray ) : RecyclerAdapter  =
            INSTANCE ?: RecyclerAdapter( cursor, sparseBoolean ).also {
                INSTANCE = it
            }
    }

    init
    {
        cursorIsValid = cursor != null

        if ( cursorIsValid ){
            indexing( cursor!! )
            cursor!!.registerDataSetObserver( dataSetObserver )
        }
    }

    inner class ViewHolder(  val view: View ) : RecyclerView.ViewHolder(view) {

        var crimetitle: TextView = view.findViewById(R.id.sup_id)
        var solvedImage: ImageView = view.findViewById(R.id.solved_img)
        var dateLabel: TextView = view.findViewById(R.id.dateText_id)

        inline fun bind(  _id: Int, crimeBlock : () -> Crime ) {

            view.setBackgroundColor(if (sparseBoolean.get(_id)) 0x9934B5E4.toInt() else Color.TRANSPARENT);
            val crime = crimeBlock()
            val crimeDate: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            if (crime.solved) solvedImage.visibility = ImageView.VISIBLE
            else solvedImage.visibility = ImageView.GONE
            dateLabel.text = crimeDate.format( Date())
            crimetitle.text = crime.title
        }

    }

    override fun getItemCount() : Int = cursor?.count ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "OnCreateView")
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_row, parent, false))
    }

    override fun onBindViewHolder( holder: ViewHolder, position : Int ) {
        Log.d(TAG, "OnBIndView")
        moveToPosition( position )
        holder.bind( position ) { buildCrime( cursor!! ) }
    }

    fun changeCursor( cursor: Cursor? ){
        val swapResult = swapCursor( cursor )
        swapResult?.close()
    }

    fun getSelectedCrimeId( pos : Int  )
    {

    }

    /**
     *  ======================== Class Util Methods========================== >
     */

    private fun indexing( cursor: Cursor ) {
        rowId = cursor.getColumnIndex( CrimeEntry.COLUMN_CRIME_ID )
        titleIndex = cursor.getColumnIndex(CrimeEntry.COLUMN_CRIME_TITLE)
        solvedIndex = cursor.getColumnIndex(CrimeEntry.COLUMN_CRIME_SOLVED)
        dateIndex = cursor.getColumnIndex(CrimeEntry.COLUMN_CRIME_DATE)
    }

    private fun swapCursor( newCursor: Cursor? ) : Cursor? {

        if ( cursor == newCursor) return null

        val oldCursor = cursor
        oldCursor?.unregisterDataSetObserver( dataSetObserver )

        cursor = newCursor
        cursor?.let {
            it.registerDataSetObserver( dataSetObserver )
            indexing( it )
            cursorIsValid = true
            notifyDataSetChanged()

        } ?: run {
            cursorIsValid = false
            rowId  = -1
            titleIndex = -1
            solvedIndex = -1
            dateIndex = -1
            notifyDataSetChanged()
        }
        return oldCursor
    }

    private fun moveToPosition( position: Int ) {
        cursor?.moveToPosition(position) ?: throw IllegalStateException("Cursor empty")
    }

    private  fun  buildCrime( cursor : Cursor ) : Crime {
        val id = cursor.getInt( rowId )
        val title = cursor.getString( titleIndex )
        val date = Date( cursor.getString( dateIndex ) )
        val solved = cursor.getInt( solvedIndex ) == 0

        return Crime( id, title, solved, date )
    }

    /**
        ==================== Action_Mode Methods ========================
     */

    fun toggleSelection(pos: Int) {
        return makeSelection(pos, !sparseBoolean.get(pos))
    }

    private fun makeSelection( pos: Int, value: Boolean ) {
        if (value)
            sparseBoolean.put(pos, true)
        else
            sparseBoolean.delete(pos)
        notifyItemChanged(pos)
    }

    fun removeSelection() {
        sparseBoolean = SparseBooleanArray()
        notifyDataSetChanged()
    }

    inner class DataSetObserverInner : DataSetObserver(){
        override fun onChanged() {
            cursorIsValid = true
            notifyDataSetChanged()
        }
        override fun onInvalidated() {
            super.onInvalidated()
            cursorIsValid = false
            notifyDataSetChanged()
        }
    }
}

