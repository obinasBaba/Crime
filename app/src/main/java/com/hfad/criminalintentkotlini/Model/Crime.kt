package com.hfad.criminalintentkotlini.Model

import java.util.*

data class Crime constructor(val id : Int = 0, var title : String = "", val solved : Boolean = false, val date : Date = Date())