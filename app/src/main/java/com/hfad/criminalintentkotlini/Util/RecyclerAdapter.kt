package com.hfad.criminalintentkotlini.Util

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.TAG
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapter( var sparseBoolean: SparseBooleanArray )
     : RecyclerView.Adapter<RecyclerAdapter.CrimeViewHolder>()
{

    private val asyncListDiffer = AsyncListDiffer< Crime >( this, DIFF_ITEM_CALLBACK )
    private var newCrimeIndex: Int? = null

    init {
        asyncListDiffer.addListListener { previousList, currentList ->
            currentList.asSequence().filterNot { currentCrime ->
                currentCrime in previousList
            }.forEach {
                newCrimeIndex = asyncListDiffer.currentList.indexOf( it )
            }
        }
    }

    fun scrollToPosition( block : ( Int ) -> Unit ) {
        newCrimeIndex?.let( block )
    }

    companion object {
        private var INSTANCE : RecyclerAdapter? = null
        fun getInstance( sparseBoolean: SparseBooleanArray ) : RecyclerAdapter =
            INSTANCE ?: RecyclerAdapter( sparseBoolean ).also {
                INSTANCE = it
            }

       private object DIFF_ITEM_CALLBACK : DiffUtil.ItemCallback<Crime>() {
            override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean =
                oldItem == newItem
        }
    }

    inner class CrimeViewHolder(private val view: View ) : RecyclerView.ViewHolder(view) {
        var crimetitle: TextView = view.findViewById(R.id.sup_id)
        var solvedImage: ImageView = view.findViewById(R.id.solved_img)
        var dateLabel: TextView = view.findViewById(R.id.createdDate_id)
        var lastModified: TextView = view.findViewById(R.id.updatedDate_id)

        fun bind(crime : Crime, position: Int ) {

            view.setBackgroundColor( if (sparseBoolean.get( position )) Color.YELLOW else Color.TRANSPARENT)

            // TODO - Change to proper date
            val crimeDate: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            if (crime.solved!!) solvedImage.visibility = ImageView.VISIBLE
            else solvedImage.visibility = ImageView.GONE
            dateLabel.text = crime.date.toString()
            lastModified.text = crime.lastUpdated.toString()
            crimetitle.text = crime.title
        }
    }

    fun getCrimeId( pos : Int ) = asyncListDiffer.currentList[ pos ].id

    override fun getItemCount() : Int = asyncListDiffer.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeViewHolder {
        Log.d(TAG, "OnCreateView")
        return CrimeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_row, parent, false))
    }

    override fun onBindViewHolder(holderCrime: CrimeViewHolder, position : Int ) {
        Log.d(TAG, "OnBIndView")
        holderCrime.bind( asyncListDiffer.currentList[ position ], position )
    }

    fun submitList( newList: List<Crime> ) {
        asyncListDiffer.submitList( newList )
    }

    /**
     *  ======================== Class Util Methods========================== >
     */


    /**
        ==================== Action_Mode UTIL Methods =========================== >
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

    fun getCrimeAtIndex(pos: Int): Crime {
        return asyncListDiffer.currentList[pos]
    }
}

