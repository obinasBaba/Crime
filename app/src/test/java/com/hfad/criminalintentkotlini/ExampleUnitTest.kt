package com.hfad.criminalintentkotlini

import android.content.Context
import android.os.Environment
import androidx.core.os.EnvironmentCompat
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.manipulation.Ordering
import java.lang.RuntimeException
import javax.xml.parsers.DocumentBuilder
import kotlin.math.sign

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        println("fun")
        val singleton = Singleton::class.java.newInstance()
        val singleton2 = Singleton::class.java.newInstance()
        println(singleton.toString())
        println(singleton2.toString())

        val s = Singleton.getInstance()
        val s2 = Singleton.getInstance()
        println()
        println(s.toString())
        println(s2.toString())


    }
}

class Singleton private constructor() {

    init {
        if ( singleton != null )
            throw RuntimeException(" Use 'getInstance()' to invoke, not Reflection ")
    }
    companion object{
       @Volatile private var singleton :  Singleton? = null
        fun getInstance() : Singleton = singleton ?: synchronized( Singleton::class ){
            singleton ?: Singleton( ).also{
                singleton = it
            }
        }
    }
}

class BuilderPatt{


}

