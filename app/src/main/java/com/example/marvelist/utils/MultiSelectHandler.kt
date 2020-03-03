package com.example.marvelist.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Handles unique multiple selection of items type T.
 */
class MultiSelectHandler<T> {
    private val _selectionSet = MutableLiveData(mutableSetOf<T>())
    val selectionSet: LiveData<MutableSet<T>>
        get() = _selectionSet

    // Listener callbacks
    private var updateListener: MultiSelectCallbacks.OnUpdateItemSelection<T>? = null
    private var onClearedListener: MultiSelectCallbacks.OnClearSelection? = null


    /**
     * Function determines whether to add or remove item from the selection.
     *
     * @param item the object to remove or add to the [_selectionSet]
     *
     * @return true if added to the set and false if removed from the set.
     */
    fun toggleSelection(item: T): Boolean {
        var added = false
        _selectionSet.value?.let {
            if (item in it) {
                removeSelection(item)
            } else {
                addToSelection(item)
                added = true
            }
            updateListener?.onUpdateSelection(item, it.size)
        }
        return added
    }

    /**
     * Add the item type T to the selection set.
     *
     * @param item the item to add to the selection set.
     */
    private fun addToSelection(item: T) {
        _selectionSet.value?.add(item)
    }

    /**
     * Remove the item type T from the selection set.
     *
     * @param item the item to remove from the selection set.
     */
    private fun removeSelection(item: T) {
        _selectionSet.value?.remove(item)
    }

    /**
     * Get the selection set.
     *
     * @return a nullable [MutableSet]
     */
    fun getSelectionSet(): MutableSet<T>? = _selectionSet.value

    /**
     * Remove everything from the selection list.
     */
    fun clearSelection() {
        _selectionSet.value?.clear()
        onClearedListener?.onClearedSelection()
    }

    /**
     * Returns whether or not the item is in the [_selectionSet]
     *
     * @param item the item to check for.
     *
     * @return true/false if an item is in the set.
     */
    fun containsItem(item: T): Boolean {
        var contains = false
        _selectionSet.value?.let {
            if (it.contains(item))
                contains = true
        }
        return contains
    }

    /**
     * Add a listener that is called whenever the [_selectionSet] is updated.
     *
     * @param listener the [MultiSelectCallbacks.OnUpdateItemSelection] listener.
     */
    fun addUpdateListener(listener: MultiSelectCallbacks.OnUpdateItemSelection<T>) {
        updateListener = listener
    }

    /**
     * Add a listener that is called whenever the [_selectionSet] is cleared/emptied.
     *
     * @param listener the [MultiSelectCallbacks.OnClearSelection] listener.
     */
    fun addClearedSelectionListener(listener: MultiSelectCallbacks.OnClearSelection) {
        onClearedListener = listener
    }

    /**
     * Remove all listeners.
     */
    fun removeAllListeners() {
        updateListener = null
        onClearedListener = null
    }


}

interface MultiSelectCallbacks {
    interface OnUpdateItemSelection<T> {
        fun onUpdateSelection(item: T, size: Int)
    }

    interface OnClearSelection {
        fun onClearedSelection()
    }
}