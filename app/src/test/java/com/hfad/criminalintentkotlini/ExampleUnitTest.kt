package com.hfad.criminalintentkotlini

import android.content.Context
import android.os.Environment
import androidx.core.os.EnvironmentCompat
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.manipulation.Ordering
import java.lang.RuntimeException
import java.util.concurrent.Callable
import java.util.concurrent.Executors
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

        val exe = Executors.newSingleThreadExecutor()
        val submit = exe.submit(Callable {
            Thread.sleep(10000)
            0
        })

        val a = submit.get()
        println(a)
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

