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
class ComicPreviewAdapter(diffCallBack: DiffUtil.ItemCallback<ComicPreview>) :
    PagedListAdapter<ComicPreview, ComicPreviewAdapter.ComicViewHolder>(diffCallBack) {

    // An onClick listener for the  ComicViewHolder.
    private var onClickListener: ComicPreviewItemListeners.OnItemClicked? = null

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
            holder.addClickListener {
                onClickListener?.onClick(comicItem, position)
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
     * De-reference [onClickListener] for garbage collection.
     */
    fun removeItemClickListener() {
        onClickListener = null
    }
}

/**
 * A listener interface for [ComicPreview] items in the [ComicPreviewAdapter].
 */
interface ComicPreviewItemListeners {
    interface OnItemClicked {
        fun onClick(comicItem: ComicPreview, position: Int)
    }
}