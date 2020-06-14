package com.hfad.criminalintentkotlini

import java.util.concurrent.Callable
import java.util.concurrent.Executors


private val executor = Executors.newSingleThreadExecutor()

fun runOnExecutor( block : () -> Unit ) {
    executor.execute(block)
}

fun < R >withResult( block: () -> Unit, type : R )
{
     executor.submit(  Runnable(block) ,  Int )

}

class twv : Callable< Int >
{
    override fun call(): Int {
        return 0
    }
}