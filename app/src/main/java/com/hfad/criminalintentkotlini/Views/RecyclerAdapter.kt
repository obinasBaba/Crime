package com.hfad.criminalintentkotlini.Views

import android.graphics.Color
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.TAG
class RecyclerAdapter( private val crimes: MutableList<Crime>, var sparseBoolean : SparseBooleanArray )
    : RecyclerView.Adapter< RecyclerAdapter.ViewHolder >()
{

    inner class ViewHolder( val view : View ) : RecyclerView.ViewHolder( view )
    {
        var crimetitle : TextView = view.findViewById( R.id.sup_id )
        var solvedCheckBox : CheckBox = view.findViewById( R.id.solvedCheckbox )
        var dateLabel : TextView = view.findViewById( R.id.dateText_id )

        fun bind( crime : Crime ) {
            crimetitle.text = crime.title
            solvedCheckBox.isChecked = crime.solved
            dateLabel.text = crime.date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        Log.d( TAG, "OnCreateView")
        return ViewHolder( LayoutInflater.from( parent.context ).inflate( R.layout.single_row, parent, false ) )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind( crimes[ position ] )
        //holder.view.isActivated = sparseBoolean[ position ]
        holder.view.setBackgroundColor( if (sparseBoolean.get(position))  0x9934B5E4.toInt() else Color.TRANSPARENT);
        Log.d( TAG, "OnBIndView")
    }

    override fun getItemCount(): Int = crimes.size

    /**
        ActionMode Methods
     */

    fun toggleSelection( pos : Int ){
        return makeSelection( pos, !sparseBoolean.get( pos ))
    }

    private fun makeSelection( pos : Int, value : Boolean ){
        if ( value )
            sparseBoolean.put( pos, true )
        else
            sparseBoolean.delete( pos )
        notifyItemChanged( pos )
    }
    fun removeSelection(){
        sparseBoolean = SparseBooleanArray()
        notifyDataSetChanged()
    }

}

