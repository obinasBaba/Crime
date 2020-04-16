package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.graphics.Color
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.UI.TAG
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapter(private val crimes: List<Crime>, var sparseBoolean: SparseBooleanArray)
                : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>()
{
    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        var crimetitle: TextView = view.findViewById(R.id.sup_id)
        var solvedImage: ImageView = view.findViewById(R.id.solved_img)
        var dateLabel: TextView = view.findViewById(R.id.dateText_id)

        fun bind(crime: Crime, position: Int) {
            view.setBackgroundColor(if (sparseBoolean.get(position)) 0x9934B5E4.toInt() else Color.TRANSPARENT);
            val crimeDate: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault())

            if (crime.solved)
                solvedImage.visibility = ImageView.VISIBLE
            else
                solvedImage.visibility = ImageView.GONE
            dateLabel.text = crimeDate.format(Date())
            crimetitle.text = crime.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "OnCreateView")
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.single_row, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //holder.view.isActivated = sparseBoolean[ position ]
        holder.bind(crimes[position], position)
        Log.d(TAG, "OnBIndView")
    }

    override fun getItemCount(): Int = crimes.size

    /**
    ActionMode Methods
     */

    fun toggleSelection(pos: Int) {
        return makeSelection(pos, !sparseBoolean.get(pos))
    }

    private fun makeSelection(pos: Int, value: Boolean) {
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
}

