package com.hfad.criminalintentkotlini.Util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FabBehavior1: CoordinatorLayout.Behavior< FloatingActionButton >
{
    constructor(  ) : super()
    constructor( ctx : Context, attr : AttributeSet ) : super( ctx, attr )

    override fun layoutDependsOn( parent: CoordinatorLayout,
                                  child: FloatingActionButton,
                                  dependency: View): Boolean
    {
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout,
                                        child: FloatingActionButton,
                                        dependency: View): Boolean
    {

        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onLayoutChild(parent: CoordinatorLayout,
                               child: FloatingActionButton,
                               layoutDirection: Int): Boolean
    {
        return super.onLayoutChild(parent, child, layoutDirection)
    }
}