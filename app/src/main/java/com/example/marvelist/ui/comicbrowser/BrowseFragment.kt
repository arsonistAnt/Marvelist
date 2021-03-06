package com.example.marvelist.ui.comicbrowser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelist.R
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.local.ComicPreview
import com.example.marvelist.databinding.BrowseFragLayoutBinding
import com.example.marvelist.injection.injector
import com.example.marvelist.injection.viewModel
import com.example.marvelist.ui.comicdetails.ComicDetailsFragment
import com.example.marvelist.utils.ComicItemListener
import com.example.marvelist.utils.NetworkResponseHandler
import com.google.android.material.snackbar.Snackbar

class BrowseFragment : Fragment(), ComicItemListener.OnItemClicked,
    ComicItemListener.OnItemLongPressed {
    private lateinit var viewBinding: BrowseFragLayoutBinding
    private lateinit var comicPagingAdapter: ComicPreviewPagingAdapter

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
        browserViewModel.comicPagedList.observe(viewLifecycleOwner, Observer { comicPreviewList ->
            comicPagingAdapter.submitList(comicPreviewList)
        })

        // Observe the network state.
        browserViewModel.browseNetworkResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.status == NetworkResponseHandler.Status.FAILED) {
                showNetworkSnackBarError(response, viewBinding.root)
            }
        })
    }

    /**
     * Show a snack bar that displays a retry button and the network error message.
     *
     * @param networkResponse a [NetworkResponseHandler.Response] that contains the HTTP response code and messages.
     * @param view the view that the snack bar will reside in.
     */
    private fun showNetworkSnackBarError(
        networkResponse: NetworkResponseHandler.Response,
        view: View
    ) {
        Snackbar.make(
            view,
            networkResponse.message,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Retry") {
                browserViewModel.refreshComicBrowser()
            }.show()
    }

    /**
     * Define initial parameters for the recycler view that displays comic items.
     *
     * @param binding the [BrowseFragLayoutBinding] object that contains view's relevant to the comic browser.
     */
    private fun setupComicBrowser(binding: BrowseFragLayoutBinding) {
        // Configure the ComicPreviewAdapter
        comicPagingAdapter = ComicPreviewPagingAdapter(diffUtilCallback).apply {
            addItemClickListener(this@BrowseFragment)
            addItemLongPressedListener(this@BrowseFragment)
        }
        // Configure the RecyclerView.
        binding.comicBrowseRecycler.apply {
            adapter = comicPagingAdapter
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
        comicPagingAdapter.removeAllListeners()
    }

    /**
     * OnClick method for [ComicPreviewPagingAdapter] items. Action in this method will be
     * used for navigational purposes, more specifically navigating to the [ComicDetailsFragment].
     *
     * @see ComicItemListener.OnItemClicked
     */
    override fun onClick(comicItem: ComicPreview, position: Int) {
        val direction = R.id.action_browseFragment_to_comicDetailsFragment
        val bundle = bundleOf("comicId" to comicItem.id)
        findNavController().navigate(direction, bundle)
    }

    /**
     * onLongPressed method for [ComicPreviewPagingAdapter] items.
     *
     * @see ComicItemListener.OnItemLongPressed
     */
    override fun onLongPressed(comicItem: ComicPreview, position: Int) {
        browserViewModel.saveComicLocalDatabase(comicItem as ComicDetail)
        // Display a toast to user that the comic has been saved.
        Toast.makeText(
            requireContext(),
            "Added  \"${comicItem.title}\" to the Reading List.",
            Toast.LENGTH_SHORT
        ).show()
    }
}