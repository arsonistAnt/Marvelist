package com.example.marvelist.ui.comicbrowser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelist.data.local.ComicPreview
import com.example.marvelist.databinding.ComicItemLayoutBinding

/**
 * Adapter class to display [ComicPreview] objects in a RecyclerView. This class takes a
 * PagedList of type [ComicPreview] to load comic book data from the network.
 *
 */
class ComicPreviewPagedAdapter(diffCallBack: DiffUtil.ItemCallback<ComicPreview>) :
    PagedListAdapter<ComicPreview, ComicPreviewPagedAdapter.ComicViewHolder>(diffCallBack) {

    // An onClick listener for the  ComicViewHolder.
    private var onClickListener: ComicPreviewItemListeners.OnItemClicked? = null
    // A long pressed listener for the  ComicViewHolder.
    private var longPressedListener: ComicPreviewItemListeners.OnItemLongPressed? = null

    /**
     * A [RecyclerView.ViewHolder] class in charge of binding [ComicPreview] data to the View layout.
     */
    class ComicViewHolder(private val binding: ComicItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comicItem: ComicPreview) {
            binding.comicItem = comicItem
        }

        /**
         * Assign a function call back when the View has been clicked.
         *
         * @param action the function callback to assign to the View onClick method.
         * @see addItemClickListener
         */
        fun addClickListener(action: () -> Unit) {
            binding.exampleTitle.setOnClickListener {
                action()
            }
        }

        /**
         * Assign a function call back when the View has been clicked.
         *
         * @param action the function callback to assign to the View onClick method.
         * @see addItemClickListener
         */
        fun addLongPressListener(action: () -> Unit) {
            binding.exampleTitle.setOnLongClickListener {
                action()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        // Inflate the View binding for the comic_item_layout.xml
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemViewBinding = ComicItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ComicViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        // Bind the ComicBase object data to the View's UI.
        val comicItem = getItem(position)
        comicItem?.let {
            holder.bind(comicItem)
            holder.apply {
                addClickListener {
                    onClickListener?.onClick(comicItem, position)
                }
                addLongPressListener {
                    longPressedListener?.onLongPressed(comicItem, position)
                }
            }
        }
    }

    /**
     * Add an [ComicPreviewItemListeners.OnItemClicked] listener to the adapter.
     */
    fun addItemClickListener(listener: ComicPreviewItemListeners.OnItemClicked) {
        onClickListener = listener
    }

    /**
     * Add an [ComicPreviewItemListeners.OnItemClicked] listener to the adapter.
     */
    fun addItemLongPressedListener(listener: ComicPreviewItemListeners.OnItemLongPressed) {
        longPressedListener = listener
    }

    /**
     * De-reference [onClickListener] & [longPressedListener] for garbage collection.
     */
    fun removeAllListeners() {
        onClickListener = null
        longPressedListener = null
    }
}

/**
 * A listener interface for [ComicPreview] items in the [ComicPreviewPagedAdapter].
 */
interface ComicPreviewItemListeners {
    interface OnItemClicked {
        fun onClick(comicItem: ComicPreview, position: Int)
    }

    interface OnItemLongPressed {
        fun onLongPressed(comicItem: ComicPreview, position: Int)
    }
}