package com.hfad.criminalintentkotlini.Model

import java.util.*

data class Crime constructor(val id : Int = -1, var title : String = "No Title", var solved : Boolean = false, val date : Date = Date())
{
   override infix fun equals(other: Any?): Boolean {
       val otherCrime =  other as? Crime ?: return false
       return this.title == otherCrime.title &&
               this.solved == otherCrime.solved &&
               this.date == otherCrime.date
    }

    infix fun isNotEqual( other: Any? ){

    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + solved.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}