package com.hfad.criminalintentkotlini.Views

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hfad.criminalintentkotlini.Fragments.ClickListeners
import com.hfad.criminalintentkotlini.Fragments.CrimeListFragment
import com.hfad.criminalintentkotlini.TAG

class TouchListener_Imp(
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
                    Log.d(TAG, "singleTAP UP")
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

                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    return super.onSingleTapConfirmed(e)
                }
            })
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        super.onTouchEvent(rv, e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        super.onRequestDisallowInterceptTouchEvent(disallowIntercept)
    }
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, "onInterceptTouchEvent  ")
        val childView: View? = recyclerView.findChildViewUnder(e.x, e.y)
        if ( childView != null && gestureDetector.onTouchEvent( e ) ){

        }
        return false
    }

}