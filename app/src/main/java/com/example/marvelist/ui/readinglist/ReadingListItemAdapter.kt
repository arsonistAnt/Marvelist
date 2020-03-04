package com.example.marvelist.ui.readinglist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.local.ReadingProgress
import com.example.marvelist.databinding.ReadingStatusItemBinding
import com.example.marvelist.ui.readinglist.ReadingListItemAdapter.ReadingStatusViewHolder
import com.example.marvelist.utils.ComicItemListener
import com.example.marvelist.utils.MultiSelectCallbacks
import com.example.marvelist.utils.MultiSelectHandler
import com.google.android.material.chip.ChipGroup

/**
 * Adapter for displaying a list of [ComicDetail] items that has been saved from the local database. Uses the
 * [ReadingStatusViewHolder] to display reading progress details for each item. Also handles multi-selection
 * of items.
 */
class ReadingListItemAdapter(diffCallBack: DiffUtil.ItemCallback<ComicDetail>) :
    ListAdapter<ComicDetail, ReadingStatusViewHolder>(diffCallBack) {
    // A set of view holders that have been marked visually for selection.
    private var selectedViewHolders = mutableSetOf<ReadingStatusViewHolder>()
    // A multi selection handler for the ComicDetail objects.
    private var multiSelectHandler = MultiSelectHandler<ComicDetail>()
    // A boolean state for the adapter to determine if its long/click presses should be a selection type action.
    var inSelectedState = false
    // An onClick listener for the  ComicViewHolder.
    private var onClickListener: ComicItemListener.OnItemClicked? = null
    // A long pressed listener for the  ComicViewHolder.
    private var longPressedListener: ComicItemListener.OnItemLongPressed? = null
    // Listener for when a chip has been checked in a view holder.
    private var chipGroupListener: ReadingStatusChipListener.OnChipCheckedListener? = null

    class ReadingStatusViewHolder(val binding: ReadingStatusItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comicDetail: ComicDetail) {
            binding.textView.text = comicDetail.title
            setReadingStatusChip(comicDetail.progress)
        }

        /**
         * Set status chip checked state based on [ReadingProgress] type.
         *
         * @param readingProgress the reading progress type
         */
        private fun setReadingStatusChip(readingProgress: ReadingProgress) {
            when (readingProgress) {
                ReadingProgress.IN_PROGRESS -> {
                    binding.inProgressChip.isChecked = true
                }
                ReadingProgress.READ -> {
                    binding.readChip.isChecked = true
                }
                else -> {
                    binding.unreadChip.isChecked = true
                }
            }
        }

        /**
         * Assign function callback to the [ChipGroup.OnCheckedChangeListener].
         *
         * @param action the callback that takes a unique chip id.
         */
        fun addGroupChipListener(action: (chipId: Int) -> Unit) {
            binding.readingStatusGroup.setOnCheckedChangeListener { chipGroup, id ->
                action(id)
            }
        }

        /**
         * Assign a function call back when the View has been clicked.
         *
         * @param action the function callback to assign to the View onClick method.
         * @see addItemClickListener
         */
        fun addClickListener(action: () -> Unit) {
            binding.readingStatusContainer.setOnClickListener {
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
            binding.readingStatusContainer.setOnLongClickListener {
                action()
                true
            }
        }

        /**
         * Toggle UI selection on this ViewHolder
         *
         * @param isSelected boolean that determines the selection UI toggle.
         */
        fun toggleSelection(isSelected: Boolean) {
            if (isSelected)
                selectUI()
            else
                deselectUI()
        }

        /**
         * Perform any UI changes on the View Holder if it has been selected.
         */
        private fun selectUI() {
            binding.readingStatusContainer.background = ColorDrawable(Color.parseColor("#b8b8b8"))
        }

        /**
         * Undo any UI changes on the View Holder if it has been deselected.
         */
        private fun deselectUI() {
            binding.readingStatusContainer.background = ColorDrawable(Color.parseColor("#FFFFFF"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingStatusViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ReadingStatusItemBinding.inflate(inflater, parent, false)

        return ReadingStatusViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ReadingStatusViewHolder, position: Int) {
        val comicItem = getItem(position)
        // Toggle selection, in case the view holder is being re-used.
        if (inSelectedState) {
            if (multiSelectHandler.containsItem(comicItem))
                holder.toggleSelection(true)
            else
                holder.toggleSelection(false)
        }
        holder.bind(comicItem)
        handleListeners(holder, comicItem, position)
    }

    /**
     * Add item listeners as well as handle the multi selection for the UI
     * and data associated with this adapter.
     *
     * @param holder the [ReadingStatusViewHolder] that has been selected along with the comicItem.
     * @param comicItem the [ComicDetail] that's been selected.
     * @param position the position of the [comicItem] in the submitted list.
     */
    private fun handleListeners(
        holder: ReadingStatusViewHolder,
        comicItem: ComicDetail,
        position: Int
    ) {
        holder.apply {
            // Handle click listeners
            addClickListener {
                if (!inSelectedState)
                    onClickListener?.onClick(comicItem, position)
                else {
                    // Multi selection actions
                    val shouldToggleSelection = multiSelectHandler.toggleSelection(comicItem)
                    holder.toggleSelection(shouldToggleSelection)
                    selectedViewHolders.add(holder)
                }
            }
            // Handle long press listeners
            addLongPressListener {
                longPressedListener?.onLongPressed(comicItem, position)
                // Multi selection actions
                inSelectedState = true
                selectedViewHolders.add(holder)
                val shouldToggleSelection = multiSelectHandler.toggleSelection(comicItem)
                holder.toggleSelection(shouldToggleSelection)
            }
            // Handle chip group listener
            addGroupChipListener { chipId ->
                chipGroupListener?.onChecked(comicItem, chipId)
            }
        }
    }

    /**
     * Add an [ComicItemListener.OnItemClicked] listener to the adapter.
     *
     * @param listener the callback for [ReadingStatusViewHolder]'s onClick().
     */
    fun addItemClickListener(listener: ComicItemListener.OnItemClicked) {
        onClickListener = listener
    }

    /**
     * Add an [MultiSelectCallbacks.OnUpdateItemSelection] listener to [multiSelectHandler].
     *
     * @param listener the callback for [multiSelectHandler].
     */
    fun addMultiSelectionUpdateListener(listener: MultiSelectCallbacks.OnUpdateItemSelection<ComicDetail>) {
        multiSelectHandler.addUpdateListener(listener)
    }

    /**
     * Add click listener for the reading status chips on each view holder.
     *
     * @param listener the listener to add.
     */
    fun addChipGroupListener(listener: ReadingStatusChipListener.OnChipCheckedListener) {
        chipGroupListener = listener
    }

    /**
     * Return a set of [ComicDetail] objects thats been marked for selection by the [multiSelectHandler]
     *
     * @return a set of [ComicDetail] objects
     */
    fun getSelection() = multiSelectHandler.getSelectionSet()


    /**
     * Remove all selection UI and selection data.
     */
    fun clearSelection() {
        // Remove selection UI from all view holders.
        for (vh in selectedViewHolders) {
            vh.toggleSelection(false)
        }
        inSelectedState = false
        multiSelectHandler.clearSelection()
    }

    /**
     * De-reference any listeners/resources for garbage collection.
     */
    fun removeAllListeners() {
        onClickListener = null
        longPressedListener = null
        chipGroupListener = null
        multiSelectHandler.removeAllListeners()
    }
}

/**
 * TODO: Add comments
 */
interface ReadingStatusChipListener {
    interface OnChipCheckedListener {
        fun onChecked(comicDetail: ComicDetail, chipId: Int)
    }
}