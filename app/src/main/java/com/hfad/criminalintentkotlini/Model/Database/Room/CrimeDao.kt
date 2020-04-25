package com.hfad.criminalintentkotlini.Model.Database.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface CrimeDao
{
    @Query("SELECT * FROM CRIME_TABLE WHERE id = :crimeId")
    fun getCrime( crimeId : Int ): Crime

    @get:Query("SELECT * FROM CRIME_TABLE")
     val getBulk : LiveData< List<Crime>>

    @Insert( entity = Crime::class )
    fun insertBulk(data: List< Crime > )

//    @Query( "SELECT lastUpdated FROM CRIME_TABLE WHERE id = :id")
//    fun getCrimeLastUpdated( id : Int ) : Date
}