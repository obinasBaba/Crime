package com.hfad.criminalintentkotlini.Views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.Model.Crime
import com.hfad.criminalintentkotlini.R
import com.hfad.criminalintentkotlini.TAG
class RecyclerAdapter( private val crimes: MutableList<Crime> ) : RecyclerView.Adapter< RecyclerAdapter.ViewHolder >()
{
    inner class ViewHolder( view : View ) : RecyclerView.ViewHolder( view )
    {
         var textView : TextView = view.findViewById( R.id.sup_id )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        Log.d( TAG, "OnCreateView")
        return ViewHolder( LayoutInflater.from( parent.context ).inflate( R.layout.single_row, parent, false ) )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = crimes[ position ].title
        Log.d( TAG, "OnBIndView")
    }

    override fun getItemCount(): Int = crimes.size
}

