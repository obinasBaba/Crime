package com.hfad.criminalintentkotlini.Model.Database.Room

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface CrimeDao
{
    @Transaction
    @Query("SELECT * FROM CRIME_TABLE WHERE _id = :crimeId")
    fun getCrime( crimeId : Int ): Crime

    @Query("DELETE FROM CRIME_TABLE WHERE _id = :crimeId")
    fun deleteCrime( crimeId : Long ): Int

    @Query("DELETE FROM CRIME_TABLE WHERE :selection")
    fun deleteCrimes( selection : String ): Int

    @Query("UPDATE CRIME_TABLE SET _id = :id WHERE _id == :id")
    fun updateCrime( id : Long ): Int

    @Query("SELECT * FROM CRIME_TABLE WHERE _id == :id")
    fun selectCrime( id : Long ): Cursor

    @get:Query( "SELECT * FROM CRIME_TABLE" )
    val selectAll : Cursor


    @get:Query("SELECT * FROM CRIME_TABLE")
     val getBulk : LiveData< List<Crime>>

    @Insert( entity = Crime::class  )
    fun insertBulkOrSingle( vararg crimes: Crime )

    @Query( "SELECT lastUpdated FROM CRIME_TABLE WHERE _id = :id")
    fun getCrimeLastUpdated( id : Int ) : Date

    @Delete( entity = Crime::class)
    fun deleteBulk( vararg crimes : Crime ) : Int

    @Update( entity = Crime::class )
    fun update( crime : Crime ) : Int
}