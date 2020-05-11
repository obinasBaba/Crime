package com.hfad.criminalintentkotlini.UI.Fragemnts


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.icu.util.GregorianCalendar
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.util.*

class TimeStampFragment : DialogFragment()
{
    companion object{
        private const val DATE_BUNDLED_KEY = "initial date to preview"
        const val SERIALIZED_DATE = "serialized date"
        fun getInstance( date: Date? ) : TimeStampFragment {
            val bundle = Bundle().apply {
                putSerializable( DATE_BUNDLED_KEY, date ?: Date() )
            }

           return TimeStampFragment().apply {
               arguments = bundle
           }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendarInstance = Calendar.getInstance()
        calendarInstance.time = requireArguments()[ DATE_BUNDLED_KEY ] as Date

        val year = calendarInstance.get( Calendar.YEAR )
        val month = calendarInstance.get( Calendar.MONTH )
        val day = calendarInstance.get( Calendar.DAY_OF_MONTH )

        val setDateListener = DatePickerDialog.OnDateSetListener{ _ , modifiedYear, modifiedMonth, modifiedDayOfMonth ->
            val intentToTargetFragment = Intent( ).apply {
                val newDate = GregorianCalendar( modifiedYear, modifiedMonth, modifiedDayOfMonth ).time
                putExtra( SERIALIZED_DATE, newDate )
            }

            targetFragment?.onActivityResult( CrimeDetailFragment.DATE_REQUEST_CODE, Activity.RESULT_OK, intentToTargetFragment )
        }

        return DatePickerDialog( requireContext(), setDateListener, year, month, day )
    }
}