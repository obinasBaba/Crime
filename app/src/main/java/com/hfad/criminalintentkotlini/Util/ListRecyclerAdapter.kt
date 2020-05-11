package com.hfad.criminalintentkotlini.Util

import android.graphics.Color
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.TAG
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ItemCallback : DiffUtil.ItemCallback< Crime >() {

    override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean
            = oldItem equals newItem

    override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean
            = oldItem equals newItem
}

class ListRecyclerAdapter( var crimeList: ArrayList< Crime >, var sparseBoolean: SparseBooleanArray )
            : ListAdapter<Crime, ListRecyclerAdapter.ViewHolder>( ItemCallback() )
{

    inner class ViewHolder( private val view: View) : RecyclerView.ViewHolder(view) {

        var crimetitle: TextView = view.findViewById(R.id.sup_id)
        var solvedImage: ImageView = view.findViewById(R.id.solved_img)
        var dateLabel: TextView = view.findViewById(R.id.createdDate_id)

        fun bind(crime : Crime, position: Int ) {

            view.setBackgroundColor(if (sparseBoolean.get( position )) 0x9934B5E4.toInt() else Color.TRANSPARENT);

            // TODO - Change to proper date
            val crimeDate: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            if (crime.solved!!) solvedImage.visibility = ImageView.VISIBLE
            else solvedImage.visibility = ImageView.GONE
            dateLabel.text = crimeDate.format( Date())
            crimetitle.text = crime.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "OnCreateView")
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position : Int ) {
        Log.d(TAG, "OnBIndView")
        holder.bind( crimeList[ position ], position )
    }

    override fun submitList(list: MutableList<Crime>?) {
        super.submitList(list)
    }

    override fun getItemCount() : Int = crimeList.size

    fun getCrimeId( pos : Int ) = crimeList[ pos ].id

    /**
    ==================== Action_Mode Methods =========================== >
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
        return crimeList[pos]
    }
}