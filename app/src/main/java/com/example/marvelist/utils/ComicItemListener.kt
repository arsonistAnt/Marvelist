package com.example.marvelist.utils

import com.example.marvelist.data.local.ComicPreview

/**
 * A listener interface for [ComicPreview] items in a recycler view adapter.
 */
interface ComicItemListener {
    interface OnItemClicked {
        fun onClick(comicItem: ComicPreview, position: Int)
    }

    interface OnItemLongPressed {
        fun onLongPressed(comicItem: ComicPreview, position: Int)
    }
}