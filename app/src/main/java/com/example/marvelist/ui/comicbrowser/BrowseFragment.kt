package com.example.marvelist.ui.comicbrowser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelist.R
import com.example.marvelist.data.local.ComicPreview
import com.example.marvelist.databinding.BrowseFragLayoutBinding
import com.example.marvelist.injection.injector
import com.example.marvelist.injection.viewModel
import com.example.marvelist.ui.comicdetails.ComicDetailsFragment

class BrowseFragment : Fragment(), ComicPreviewItemListeners.OnItemClicked {
    private lateinit var viewBinding: BrowseFragLayoutBinding
    private lateinit var comicAdapter: ComicPreviewAdapter

    // Provide the BrowserViewModel via injector dagger component.
    private val browserViewModel by viewModel {
        injector.browseViewModel
    }

    // The default DiffUtil configuration for the ComicBaseAdapter
    private val diffUtilCallback = object : DiffUtil.ItemCallback<ComicPreview>() {
        override fun areItemsTheSame(oldItem: ComicPreview, newItem: ComicPreview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ComicPreview, newItem: ComicPreview): Boolean {
            return (oldItem.id == newItem.id) && (oldItem.title == newItem.title) && (oldItem.comicDescription == newItem.comicDescription)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = BrowseFragLayoutBinding.inflate(inflater, container, false)
        // Setup RecyclerView for Comic browsing.
        setupComicBrowser(viewBinding)
        setupObservableData()
        return viewBinding.root
    }

    /**
     * Observe data emitted from the ViewModel object.
     */
    private fun setupObservableData() {
        browserViewModel.comicPagedList.observe(viewLifecycleOwner, Observer {
            comicAdapter.submitList(it)
        })
    }

    /**
     * Define initial parameters for the recycler view that displays comic items.
     *
     * @param binding the [BrowseFragLayoutBinding] object that contains view's relevant to the comic browser.
     */
    private fun setupComicBrowser(binding: BrowseFragLayoutBinding) {
        // Configure the ComicPreviewAdapter
        comicAdapter = ComicPreviewAdapter(diffUtilCallback).apply {
            addItemClickListener(this@BrowseFragment)
        }
        // Configure the RecyclerView.
        binding.comicBrowseRecycler.apply {
            adapter = comicAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /**
     * Cleanup any resources in this callback.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.unbind()
        // Removes fragment reference from the comicAdapter.
        comicAdapter.removeItemClickListener()
    }

    /**
     * OnClick method for [ComicPreviewAdapter] items. Action in this method will be
     * used for navigational purposes, more specifically navigating to the [ComicDetailsFragment].
     *
     * @see ComicPreviewAdapter.onClickListener
     */
    override fun onClick(comicItem: ComicPreview, position: Int) {
        val direction = R.id.action_browseFragment_to_comicDetailsFragment
        findNavController().navigate(direction)
    }
}