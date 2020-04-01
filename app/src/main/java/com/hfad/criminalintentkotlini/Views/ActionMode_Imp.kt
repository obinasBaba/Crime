package com.hfad.criminalintentkotlini.Views

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import com.hfad.criminalintentkotlini.Fragments.CrimeListFragment
import com.hfad.criminalintentkotlini.R

class ActionMode_Imp constructor(val recyclerAdapter: RecyclerAdapter, private val fragment: Fragment )

    : ActionMode.Callback
{
    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

        when( item?.itemId ){
            R.id.action_delete -> { (fragment as CrimeListFragment ).deleteSelectedItems()  }
            R.id.action_copy -> { }
            R.id.action_forward -> { }
        }

        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate( R.menu.action_menu, menu )
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        recyclerAdapter.removeSelection()
        (fragment as CrimeListFragment).nullifyActionMode()
    }
}