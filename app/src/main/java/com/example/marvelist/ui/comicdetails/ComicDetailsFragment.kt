package com.example.marvelist.ui.comicdetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.databinding.ComicDetailsFragLayoutBinding
import com.example.marvelist.injection.injector
import com.example.marvelist.injection.viewModel
import com.example.marvelist.utils.calculateMaxLines
import com.example.marvelist.utils.formatDateString
import com.example.marvelist.utils.marvelvariants.CreatorVariants
import com.example.marvelist.utils.marvelvariants.DateVariants
import com.example.marvelist.utils.marvelvariants.ThumbnailVariant

class ComicDetailsFragment : Fragment() {
    private lateinit var viewBinding: ComicDetailsFragLayoutBinding
    private lateinit var creatorItemAdapter: CreatorItemAdapter

    // Provide the BrowserViewModel via injector dagger component.
    private val detailsViewModel by viewModel {
        injector.comicDetailsViewModel
    }

    // Diff util for the CreatorItemAdapter.
    private val diffUtilCallback = object : DiffUtil.ItemCallback<MarvelJson.Creator>() {
        override fun areItemsTheSame(
            oldItem: MarvelJson.Creator,
            newItem: MarvelJson.Creator
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: MarvelJson.Creator,
            newItem: MarvelJson.Creator
        ): Boolean {
            return (oldItem.name == newItem.name) && (oldItem.role == newItem.role)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ComicDetailsFragLayoutBinding.inflate(inflater, container, false)
        setupObservableData()
        requestComicBookDetails()
        return viewBinding.root
    }

    /**
     * Display comic book details.
     */
    private fun requestComicBookDetails() {
        // Retrieve comic book id from bundle
        var comicId = -1
        arguments?.apply {
            comicId = getInt("comicId")
        }
        // Request via API
        detailsViewModel.getComicById(comicId)
    }

    /**
     * Observe data emitted from the ViewModel object.
     */
    private fun setupObservableData() {
        detailsViewModel.comicDetailData.observe(viewLifecycleOwner, Observer {
            displayComicBookDetails(viewBinding, it)
        })
    }

    /**
     * Assign comic book data to the Views.
     *
     * @param binding the view's to be assigned data.
     * @param comic the comic book data to display to the view.
     */
    @SuppressLint("SimpleDateFormat")
    private fun displayComicBookDetails(
        binding: ComicDetailsFragLayoutBinding,
        comic: ComicDetail
    ) {
        // Assign data binding, most of the view's should be filled out via the layout.
        binding.comicDetails = comic
        binding.descText.calculateMaxLines()
        // Construct the thumbnail URL and load it into the comic image view.
        val thumbnailUrl = ThumbnailVariant.constructThumbnailUrl(
            comic.thumbnailUrl,
            "jpg",
            ThumbnailVariant.portrait_incredible
        )
        binding.comicPortrait.load(thumbnailUrl)
        // Get the sale & foc date and show it in the date text view.
        displayDatesText(comic.dates, binding)
        // Get Writer's data and display it in the 'Author:' text view.
        displayAuthorText(comic.author, binding.authorName)
        // Get and display the creators in a recycler view.
        displayCreators(comic.author, binding.creditsRecyclerView)
    }

    /**
     * Get and display on sale and foc date information.
     *
     * @param dates the list of [MarvelJson.Date] objects that contains the date information.
     * @param binding the binding that contains the text view relevant to the on sale and foc date.
     */
    private fun displayDatesText(
        dates: List<MarvelJson.Date>?,
        binding: ComicDetailsFragLayoutBinding
    ) {
        val onSaleDateObj = dates?.find { it.type == DateVariants.onSaleDate }
        val focDateObj = dates?.find { it.type == DateVariants.focDate }
        // Obtain the formatted date string in 'MMM DD, YYYY'
        val saleDateStr = onSaleDateObj?.formatDateString() ?: "No sale date available."
        val focDateStr = focDateObj?.formatDateString() ?: "No foc date available."
        binding.datePublished.text = saleDateStr
        binding.focDateTitle.text = focDateStr
    }

    /**
     * Get and display author information.
     *
     * @param creators the [MarvelJson.CreatorGroup] nullable that contains the Writer's name.
     * @param authorTextView the [TextView] to display the author's name into.
     */
    private fun displayAuthorText(creators: MarvelJson.CreatorGroup?, authorTextView: TextView) {
        // Get's the first available writer/author.
        val authorObj = creators?.items?.firstOrNull { CreatorVariants.writer == it.role }
        val authorName = authorObj?.name ?: "No author/writer available"
        authorTextView.text = authorName
    }

    /**
     *  Display all the creators that developed the comic.
     *
     *  @param creators The [MarvelJson.CreatorGroup] that contains a list of creators.
     *  @param creditsRecyclerView the recycler view that displays all of the info.
     */
    private fun displayCreators(
        creators: MarvelJson.CreatorGroup?,
        creditsRecyclerView: RecyclerView
    ) {
        // Only setup the recycler view if this is not null.
        creators?.let {
            creatorItemAdapter = CreatorItemAdapter(diffUtilCallback)
            creditsRecyclerView.apply {
                adapter = creatorItemAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
            creatorItemAdapter.submitList(creators.items)
        }
    }

    /**
     * Cleanup any resources in this callback.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.unbind()
    }
}