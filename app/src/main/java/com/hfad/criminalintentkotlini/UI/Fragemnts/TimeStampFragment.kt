package com.hfad.criminalintentkotlini.UI.Fragemnts

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import java.util.*

class TimeStampFragment() : DialogFragment()
{

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendarInstance = Calendar.getInstance()
        val year = calendarInstance.get( Calendar.YEAR )
        val month = calendarInstance.get( Calendar.MONTH )
        val day = calendarInstance.get( Calendar.DAY_OF_MONTH )

        return DatePickerDialog( requireContext(), null, year, month, day )
    }

}