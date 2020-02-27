package com.example.wyrmprint.injection

import android.app.Activity
import androidx.fragment.app.Fragment
import com.example.marvelist.injection.components.ActivityComponent


/**
 * An interface of that provides the implementation of [ActivityComponent]
 */
interface InjectionProvider {
    val component: ActivityComponent
}

//Provide an extension property to access the dagger component from the Activity, in Fragments.
val Fragment.injector
    get() = (requireActivity() as InjectionProvider).component

//Provide an extension property to access the dagger component from the Activity.
val Activity.injector
    get() = (this as InjectionProvider).component