/*
 * MIT License
 *
 * Copyright (c) 2026 Fabricio Batista Narcizo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dk.itu.moapd.androidthreads.ui.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A delegate class to handle view binding in fragments, ensuring proper lifecycle management.
 *
 * This delegate automatically clears the binding reference when the fragment's view is destroyed,
 * preventing memory leaks and ensuring that the binding is only accessed when the view is valid.
 *
 * @param T The type of ViewBinding.
 * @property fragment The fragment instance where the binding is used.
 * @property viewBindingFactory A factory function to create the ViewBinding instance from a View.
 */
class FragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val viewBindingFactory: (View) -> T,
) : ReadOnlyProperty<Fragment, T> {
    /**
     * Holds the current binding instance or null if not initialized or cleared.
     */
    private var binding: T? = null

    /**
     * Initializes the delegate by observing the fragment's lifecycle to manage the binding's
     * lifecycle.
     */
    init {
        fragment.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
                        viewLifecycleOwner.lifecycle.addObserver(
                            object : DefaultLifecycleObserver {
                                override fun onDestroy(owner: LifecycleOwner) {
                                    binding = null
                                }
                            },
                        )
                    }
                }
            },
        )
    }

    /**
     * Gets the value of the binding property, initializing it if necessary.
     *
     * @param thisRef The fragment instance.
     * @param property The property metadata.
     *
     * @return The ViewBinding instance.
     *
     * @throws IllegalStateException if the fragment's view is destroyed.
     */
    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>,
    ): T {
        val binding = binding
        if (binding != null) return binding

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(androidx.lifecycle.Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException(
                "Should not attempt to get bindings when Fragment views are destroyed.",
            )
        }

        return viewBindingFactory(thisRef.requireView()).also { this.binding = it }
    }
}

/**
 * Extension function to simplify the usage of [FragmentViewBindingDelegate] in fragments.
 *
 * @param T The type of ViewBinding.
 * @param factory A factory function to create the ViewBinding instance from a View.
 *
 * @return An instance of [FragmentViewBindingDelegate] for the fragment.
 */
fun <T : ViewBinding> Fragment.viewBinding(factory: (View) -> T) = FragmentViewBindingDelegate(this, factory)
