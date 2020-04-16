package com.hfad.criminalintentkotlini.Model.Database

import android.provider.BaseColumns
import org.intellij.lang.annotations.Language


/**
 * --Hold organization and structure of 'crime' database.
 *  mostly hold constants
 */
sealed class CrimeDatabaseContract : BaseColumns {

    // Creating inner class for each table inside the Database
    object CrimeEntry {

        const val TABLE_NAME: String = "crime"

        //Or use the 'BaseColumns' naming convection
        const val COLUMN_CRIME_ID: String = "_id"
        const val COLUMN_CRIME_TITLE = "crime_title"
        const val COLUMN_CRIME_DATE = "crime_date"
        const val COLUMN_CRIME_SOLVED = "crime_solved"

        @Language("SQLAndroid")
        const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                "( $COLUMN_CRIME_ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                " $COLUMN_CRIME_TITLE TEXT NOT NULL ," +
                " $COLUMN_CRIME_DATE TEXT, " +
                "$COLUMN_CRIME_SOLVED BOOL )"
    }
}