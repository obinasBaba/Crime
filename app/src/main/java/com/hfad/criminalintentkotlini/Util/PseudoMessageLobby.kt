package com.hfad.criminalintentkotlini.Util

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class PseudoMessageLobby( private val executor: ExecutorService, private val lifecycle : Lifecycle ) : LifecycleObserver
{
    init {
        lifecycle.addObserver( this )
    }

    private val messageLobby = Runnable {
       try {
           TimeUnit.SECONDS.sleep( 4 )
           while ( true ){
               val randomUser =  Random.nextLong( 4 )
               Log.d( "main", "MSG from $randomUser" )
               TimeUnit.SECONDS.sleep( randomUser )
           }
       }catch ( i : InterruptedException ){
           Log.d( "main", "Lobby disconnected" )
       }
    }

    @OnLifecycleEvent( Lifecycle.Event.ON_START )
    private fun join() {
        Log.d( "main", lifecycle.currentState.name )
        if ( lifecycle.currentState.isAtLeast( Lifecycle.State.STARTED ) )
            executor.execute( messageLobby )
    }

    @OnLifecycleEvent( Lifecycle.Event.ON_DESTROY )
    private fun leaveChatRoom() {
        if ( !executor.isTerminated )
            executor.shutdownNow()
    }
}