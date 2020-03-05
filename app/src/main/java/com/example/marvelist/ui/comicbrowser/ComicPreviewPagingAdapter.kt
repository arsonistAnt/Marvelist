package com.example.marvelist.ui.comicbrowser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.marvelist.data.local.ComicPreview
import com.example.marvelist.databinding.ComicItemLayoutBinding
import com.example.marvelist.utils.ComicItemListener
import com.example.marvelist.utils.calculateMaxLines
import com.example.marvelist.utils.marvelvariants.ThumbnailVariant
import timber.log.Timber

/**
 * Adapter class to display [ComicPreview] objects in a RecyclerView. This class takes a
 * PagedList of type [ComicPreview] to load comic book data from the network.
 *
 */
class ComicPreviewPagingAdapter(diffCallBack: DiffUtil.ItemCallback<ComicPreview>) :
    PagedListAdapter<ComicPreview, ComicPreviewPagingAdapter.ComicViewHolder>(diffCallBack) {

    // An onClick listener for the  ComicViewHolder.
    private var onClickListener: ComicItemListener.OnItemClicked? = null
    // A long pressed listener for the  ComicViewHolder.
    private var longPressedListener: ComicItemListener.OnItemLongPressed? = null

    /**
     * A [RecyclerView.ViewHolder] class in charge of binding [ComicPreview] data to the View layout.
     */
    class ComicViewHolder(private val binding: ComicItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comicItem: ComicPreview) {
            binding.comicItem = comicItem
            assignComicImage(binding, comicItem.thumbnailUrl)
            binding.comicDescription.calculateMaxLines()
        }

        /**
         * Load the url into the comic_portrait image view.
         *
         * @param thumbnailPath the thumbnail path that will be used for the URL construction.
         * @param binding the view binding that contains the image view.
         */
        private fun assignComicImage(binding: ComicItemLayoutBinding, thumbnailPath: String) {
            // Construct the thumbnail URL and load it into the comic image view.
            val thumbnailUrl = ThumbnailVariant.constructThumbnailUrl(
                thumbnailPath,
                "jpg",
                ThumbnailVariant.portrait_incredible
            )
            // Load the image URL using Coil
            binding.comicPortrait.load(thumbnailUrl) {
                crossfade(true)
                listener(onSuccess = { _, _ -> },
                    onError = { _, e ->
                        Timber.e(e)
                    })
            }
        }

        /**
         * Assign a function call back when the View has been clicked.
         *
         * @param action the function callback to assign to the View onClick method.
         * @see addItemClickListener
         */
        fun addClickListener(action: () -> Unit) {
            binding.comicPreviewItemContainer.setOnClickListener {
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
            binding.comicPreviewItemContainer.setOnLongClickListener {
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
     * Add an [ComicItemListener.OnItemClicked] listener to the adapter.
     */
    fun addItemClickListener(listener: ComicItemListener.OnItemClicked) {
        onClickListener = listener
    }

    /**
     * Add an [ComicItemListener.OnItemClicked] listener to the adapter.
     */
    fun addItemLongPressedListener(listener: ComicItemListener.OnItemLongPressed) {
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