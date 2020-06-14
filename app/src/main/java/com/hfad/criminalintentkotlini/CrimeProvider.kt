package com.hfad.criminalintentkotlini

import android.content.*
import android.database.CharArrayBuffer
import android.database.ContentObserver
import android.database.Cursor
import android.database.DataSetObserver
import android.database.sqlite.SQLiteCursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.CursorAdapter
import androidx.room.RoomMasterTable.TABLE_NAME
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.hfad.criminalintentkotlini.Model.Database.Room.Crime
import com.hfad.criminalintentkotlini.Model.Database.Room.CrimeDB

 class CrimeProvider : ContentProvider() {

    private lateinit var helper : SupportSQLiteOpenHelper
    private lateinit var ctx : Context

    companion object {
        const val AUTHORITY = "com.hfad.criminalintentkotlini.provider"
        const val AUTHORITY_URI = "content://$AUTHORITY"
        val CRIME_URI : Uri = Uri.parse( AUTHORITY_URI +"/"+ Crime.TABLE_NAME )

        /** The match code for some items in the Crime table.  */
        private const val CODE_CRIME_DIR = 1

        /** The match code for an item in the Cheese table.  */
        private const val CODE_CRIME_ITEM = 2

        /** uri matcher*/
        private val Matcher : UriMatcher = UriMatcher( UriMatcher.NO_MATCH ).apply {
            addURI(AUTHORITY_URI, Crime.TABLE_NAME, CODE_CRIME_DIR )
            addURI(AUTHORITY_URI, Crime.TABLE_NAME + "/*", CODE_CRIME_ITEM);

        }
    }

    override fun onCreate(): Boolean {
        if ( context == null )
            return false
        helper = CrimeDB.getInstance( context!! ).openHelper
        ctx = context!!
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor?
    {
        return when( Matcher.match( uri ) ) {
            CODE_CRIME_ITEM -> {
                val id = ContentUris.parseId( uri )
                CrimeDB.getInstance( ctx ).getCrimeDao().selectCrime( id  )
            }
            CODE_CRIME_DIR -> {
                CrimeDB.getInstance(ctx).getCrimeDao().selectAll
            }
            else -> throw IllegalArgumentException("Unknown uri -  $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int
    {
       if ( Matcher.match( uri ) == CODE_CRIME_DIR && Matcher.match(uri) == CODE_CRIME_ITEM )
       {
           val writer = helper.writableDatabase
           val id = writer.delete( Crime.TABLE_NAME, selection, selectionArgs )
           ctx.contentResolver.notifyChange( uri, null)
           return id
       }else
           throw IllegalArgumentException("Unknown uri -  $uri")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri?
    {
        if ( context == null )
            throw Throwable( "Context is Null" )

       when( Matcher.match( uri ) )
       {
           CODE_CRIME_ITEM -> { throw IllegalArgumentException( "Cant Insert with ID" )  }
           CODE_CRIME_DIR -> {
               val id = CrimeDB.getInstance( context!!)
                                     .getCrimeDao()
                                     .insertBulkOrSingle( Crime.fromContentValues( values ) )
               context!!.contentResolver.notifyChange( uri, null)
               return ContentUris.withAppendedId( CRIME_URI, ContentUris.parseId( uri ) )
           }
           else -> throw IllegalArgumentException("Unknown uri -  $uri")
       }
    }

    override fun bulkInsert(uri: Uri, values: Array<out ContentValues>): Int {
        if ( context == null )
            throw Throwable( "Context is Null" )

        when( Matcher.match( uri ) ) {
            CODE_CRIME_ITEM -> { throw IllegalArgumentException( "Can't Insert with ID = $uri" )  }
            CODE_CRIME_DIR -> {
                val mutableList : MutableList< Crime > = MutableList( values.size ) {
                    Crime.fromContentValues( values[ it ] )
                }
                val count = CrimeDB.getInstance( context!!)
                    .getCrimeDao()
                    .insertBulkOrSingle( *mutableList.toTypedArray() )
                context!!.contentResolver.notifyChange( uri, null)
                return ContentUris.parseId( uri ).toInt()
            }
            else -> throw IllegalArgumentException("Unknown uri -  $uri")
        }
    }

    override fun update( uri: Uri, values: ContentValues?,
                         selection: String?,
                         selectionArgs: Array<String>?): Int
    {
        if ( context == null )
            throw Throwable( "Context is Null" )

        when( Matcher.match( uri ) ) {
            CODE_CRIME_ITEM -> {
                val crime = Crime.fromContentValues( values )
                val count = CrimeDB.getInstance( context!!).getCrimeDao().update( crime )
                context!!.contentResolver.notifyChange( uri, null)
                return count
            }
            CODE_CRIME_DIR -> { throw IllegalArgumentException("Cant update without ID") }

            else -> throw IllegalArgumentException("Unknown uri -  $uri")
        }
    }



    override fun getType(uri: Uri): String? {
        return when( Matcher.match( uri) ){
            CODE_CRIME_ITEM -> { "vnd.android.cursor.item/$AUTHORITY . ${Crime.TABLE_NAME}" }
            CODE_CRIME_DIR -> { "vnd.android.cursor.dir/$AUTHORITY . ${Crime.TABLE_NAME}" }
            else -> throw IllegalArgumentException("Unknown uri -  $uri")
        }
    }

    
}
