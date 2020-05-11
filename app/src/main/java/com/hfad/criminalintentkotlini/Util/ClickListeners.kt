package com.hfad.criminalintentkotlini.Util

import android.view.View

interface ClickListeners {
    fun onClick(pos: Int, view: View)
    fun onLongClick(pos: Int, view: View)
}