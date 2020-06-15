package com.hfad.criminalintentkotlini.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.hfad.criminalintentkotlini.R
import kotlinx.android.synthetic.main.content_file_selection.*
import java.io.File

class FileSelection : AppCompatActivity() {

    lateinit var resultIntent: Intent
    lateinit var filesLocation: File
     var fileList:  List<File>? = ArrayList()
    var fileNames: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_selection)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        resultIntent = Intent("com.hfad.criminalintentkotlini.ACTION_RETURN_FILE", null)
        filesLocation = filesDir
        fileList = filesLocation.listFiles()?.toList()
        fileList?.let {

            it.forEach {file ->
                fileNames.add( file.absolutePath)
            }
            listView_id.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fileNames)
            listView_id.onItemClickListener =
                AdapterView.OnItemClickListener{ _, _, position, _ ->
                    val file : File = it[ position ]
                    val contentUrl = FileProvider.getUriForFile(
                        this, "com.hfad.criminalintentkotlini.fileprovider", file )

                    resultIntent.setDataAndType( contentUrl, contentResolver.getType( contentUrl ) )
                    resultIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    setResult( Activity.RESULT_OK, resultIntent )
                    finish()
                }

        } ?: run { fileNotPresent.visibility = View.VISIBLE }
    }

}