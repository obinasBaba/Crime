package com.hfad.criminalintentkotlini.Model.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//File name on directory and database version
private const val FILE_NAME = "crime.db"
private const val VERSION = 1

class CrimeOpenHelper ( val ctx : Context) : SQLiteOpenHelper( ctx, FILE_NAME, null, VERSION )
{

    override fun onCreate(db: SQLiteDatabase?) {

        //db dose't exist, create db using "contract" class
        db?.execSQL( CrimeDatabaseContract.CrimeEntry.CREATE_TABLE )
        
        //Fill the database with Dummy data for testing
        val fillUp = DatabaseFillUp( db )
        fillUp.fillUp()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}