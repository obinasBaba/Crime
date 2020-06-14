package com.hfad.criminalintentkotlini.Util

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.MutableLiveData
import com.hfad.criminalintentkotlini.R

class ActionModeCallback: ActionMode.Callback {

    companion object{
        val deleteCounter : MutableLiveData< Int > = MutableLiveData( 0 )
        val stopActionModeCounter : MutableLiveData< Int > = MutableLiveData( 0 )
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

        when( item?.itemId ){
            R.id.action_delete -> {
                deleteCounter.value = deleteCounter.value!!.plus( 1 )
            }
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
        stopActionModeCounter.value = stopActionModeCounter.value!!.plus( 1 )
    }
}