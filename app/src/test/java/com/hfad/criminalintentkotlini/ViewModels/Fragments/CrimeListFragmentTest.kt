package com.hfad.criminalintentkotlini.ViewModels.Fragments
import android.app.Instrumentation
import android.content.Context
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class CrimeListFragmentTest : TestCase() {

    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = Instrumentation().context
    }

    @Test
    fun testDeleteSelectedItems()
    {

    }
}