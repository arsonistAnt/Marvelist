package com.example.marvelist.ui.readinglist

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelist.R
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.local.ComicPreview
import com.example.marvelist.databinding.ReadingListFragLayoutBinding
import com.example.marvelist.injection.injector
import com.example.marvelist.injection.viewModel
import com.example.marvelist.ui.viewmodel.ReadingListViewModel
import com.example.marvelist.utils.ComicItemListener
import com.example.marvelist.utils.MultiSelectCallbacks
import com.example.marvelist.utils.toComicInfo

class ReadingListFragment : Fragment(), ComicItemListener.OnItemClicked,
    MultiSelectCallbacks.OnUpdateItemSelection<ComicDetail> {
    private lateinit var viewBinding: ReadingListFragLayoutBinding
    private lateinit var readingListAdapter: ReadingListItemAdapter
    private lateinit var actionBarMenu: Menu
    // Inject the ReadingListViewModel
    private val readingListViewModel by viewModel {
        injector.readingListViewModel
    }

    // Diff util item callback
    private val diffUtilCallback = object : DiffUtil.ItemCallback<ComicDetail>() {
        override fun areItemsTheSame(oldItem: ComicDetail, newItem: ComicDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ComicDetail, newItem: ComicDetail): Boolean {
            return (oldItem.id == newItem.id) && (oldItem.title == newItem.title) && (oldItem.comicDescription == newItem.comicDescription)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ReadingListFragLayoutBinding.inflate(inflater, container, false)
        requireActivity().actionBar?.title = getString(R.string.reading_list_title)
        // Setup recycler view for the reading list.
        setupReadingList(viewBinding)
        setupObservableData(readingListViewModel)
        readingListViewModel.getAllSavedComics()
        setHasOptionsMenu(true)
        return viewBinding.root
    }

    private fun setupObservableData(viewModel: ReadingListViewModel) {
        viewModel.savedComics.observe(viewLifecycleOwner, Observer {
            readingListAdapter.submitList(it)
        })
    }

    /**
     * Configure the layout manager and recycler adapter to display a list
     * of comics that's been saved from the database.
     *
     * @param binding the view binding that contains the recycler view associated with the list.
     */
    private fun setupReadingList(binding: ReadingListFragLayoutBinding) {
        readingListAdapter = ReadingListItemAdapter(diffUtilCallback)
            .apply {
                addItemClickListener(this@ReadingListFragment)
                addMultiSelectionUpdateListener(this@ReadingListFragment)
            }
        binding.readingListRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = readingListAdapter
        }
    }


    /**
     * Navigate to the comic details fragment.
     */
    override fun onClick(comicItem: ComicPreview, position: Int) {
        val direction = R.id.action_readingListFragment_to_comicDetailsFragment
        val bundle = bundleOf("comicId" to comicItem.id)
        findNavController().navigate(direction, bundle)
    }

    /**
     * Update the number of items selected in the action bar as well as handling the
     * delete options visibility for multi-selection.
     *
     * @param item that was selected in the recycler view.
     * @param size the current size of the multi-selection.
     */
    override fun onUpdateSelection(item: ComicDetail, size: Int) {
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        val deleteMenuItem = actionBarMenu.findItem(R.id.delete_menu_item)
        if (size > 0) {
            supportActionBar?.title = "Selected: $size"
            // Show the delete icon when a selection has been made.
            actionBarMenu.findItem(R.id.delete_menu_item).isVisible = true
            deleteMenuItem.isVisible = true
        } else {
            // When the number of selections is 0 hide all options related to deletion.
            readingListAdapter.clearSelection()
            hideDeleteMenuOptions(deleteMenuItem, supportActionBar)
        }
    }

    /**
     * Inflate custom menu for multi-selection deletion.
     *
     * @param menu the menu tied to the parent activity's action bar.
     * @param inflater the menu inflater.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the reading list menu.
        inflater.inflate(R.menu.reading_list_menu, menu)
        // Initialize actionBarMenu with this menu for later use.
        actionBarMenu = menu
        // Hide the delete icon on start.
        menu.findItem(R.id.delete_menu_item).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        when (item.itemId) {
            R.id.delete_menu_item -> {
                readingListAdapter.getSelection()?.apply {
                    val selections = this.toList().map { it.toComicInfo() }
                    readingListViewModel.removeComics(selections)
                    // Remove all selected data from the adapter.
                    readingListAdapter.clearSelection()
                    // Hide delete options.
                    hideDeleteMenuOptions(item, supportActionBar)
                }
            }
            android.R.id.home -> {
                val numOfSelections: Int = readingListAdapter.getSelection()?.size ?: 0
                if (numOfSelections <= 0) {
                    findNavController().navigateUp()
                } else {
                    readingListAdapter.clearSelection()
                    val deleteMenuItem = actionBarMenu.findItem(R.id.delete_menu_item)
                    hideDeleteMenuOptions(deleteMenuItem, supportActionBar)
                }

            }
        }
        return true
    }

    /**
     *  Hide the selection count and trash icon in the action bar.
     *
     *  @param item the [MenuItem] that contains the trash/delete icon.
     *  @param toolbar the [ActionBar] to change the title of.
     */
    private fun hideDeleteMenuOptions(item: MenuItem, toolbar: ActionBar?) {
        // Hide the delete options
        item.isVisible = false
        toolbar?.title = getString(R.string.reading_list_title)
    }

    /**
     * Cleanup any resources in this callback.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.unbind()
        readingListAdapter.removeAllListeners()
        actionBarMenu.clear()
    }


}