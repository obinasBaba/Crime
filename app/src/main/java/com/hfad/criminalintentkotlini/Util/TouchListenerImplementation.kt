package com.hfad.criminalintentkotlini.Util

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.hfad.criminalintentkotlini.UI.Fragemnts.CrimeListFragment
import com.hfad.criminalintentkotlini.UI.TAG
import kotlin.math.abs

class TouchListenerImplementation(
    context: Context?,
    val recyclerView: RecyclerView,
    val clickListeners: ClickListeners,
    val fragment: CrimeListFragment
) : RecyclerView.SimpleOnItemTouchListener() {

    private var gestureDetector: GestureDetector

    init {

        gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent?): Boolean {
                    Log.d(TAG, "singleTAP UP" )

                    e?.let {
                        val childView: View? = recyclerView.findChildViewUnder(it.x, it.y)
                        childView?.let {
                            clickListeners.onClick( recyclerView.getChildAdapterPosition( childView ), childView )
                        }
                    }
                    return true
                }

                override fun onLongPress(e: MotionEvent?) {
                    e?.let {
                        val childView: View? = recyclerView.findChildViewUnder(it.x, it.y)
                        childView?.let {
                            clickListeners.onLongClick( recyclerView.getChildAdapterPosition( childView ), childView )
                        }
                    }
                }

                override fun onFling(
                    startAt: MotionEvent?,
                    endAt: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if ( startAt != null && endAt != null ) {
                        val finalX = abs( endAt.x ) - abs( startAt.x )
                        val finalY = abs( endAt.y ) - abs( startAt.y )

                        if ( finalX > finalY ) {
                            Toast.makeText( context, "Right or Left ", Toast.LENGTH_SHORT ).show()
                        }
                    }
                    return true
                }
            })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView: View? = recyclerView.findChildViewUnder(e.x, e.y)
        childView?.let{
            gestureDetector.onTouchEvent( e )
        }
        return false
    }

}