package com.hfad.criminalintentkotlini.Model.Database.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface CrimeDao
{
    @Transaction
    @Query("SELECT * FROM CRIME_TABLE WHERE id = :crimeId")
    fun getCrime( crimeId : Int ): Crime

    @get:Query("SELECT * FROM CRIME_TABLE")
     val getBulk : LiveData< List<Crime>>

    @Insert( entity = Crime::class )
    fun insertBulkOrSingle( vararg crimes: Crime )

    @Query( "SELECT lastUpdated FROM CRIME_TABLE WHERE id = :id")
    fun getCrimeLastUpdated( id : Int ) : Date

    @Delete( entity = Crime::class)
    fun deleteBulk( vararg crimes : Crime ) : Int

    @Update( entity = Crime::class )
    fun update( crime : Crime ) : Int

}