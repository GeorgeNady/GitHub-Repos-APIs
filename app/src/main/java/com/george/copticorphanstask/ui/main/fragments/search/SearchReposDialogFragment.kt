package com.george.copticorphanstask.ui.main.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.george.copticorphanstask.adapter.repository.RepositoryAdapter
import com.george.copticorphanstask.base.fragments.MainBaseFragment
import com.george.copticorphanstask.databinding.BottomSheetSearchBinding
import com.george.copticorphanstask.domain.RepositoryDomain
import com.george.copticorphanstask.util.RecyclerViewScrollListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchReposDialogFragment(frag: MainBaseFragment<*>): BottomSheetDialogFragment() {

    val repositoryAdapter by lazy { RepositoryAdapter(frag) }

    companion object {
        const val TAG = "SearchReposDialogFragment"
    }

    private val binding by lazy { BottomSheetSearchBinding.inflate(layoutInflater) }
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        with(binding) {
            lifecycleOwner = this@SearchReposDialogFragment
            bSearchViewModel = searchViewModel
            setupRecyclerView()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){

            with(searchViewModel) {



                swipeRefresh.setOnRefreshListener {
                    onResetSearchClashes(true)
                }

                searchList.observe(viewLifecycleOwner, searchListObserver())
                searchQuery.apply {
                    if (!hasObservers()) observe(viewLifecycleOwner, clashesSearchQueryObserver())
                }
                searchErrorShowEvent.observe(viewLifecycleOwner, errorObserver())
            }
        }
    }

    // Y ///////////////////////////////////////////////////////////////////////////////// OBSERVERS
    private fun BottomSheetSearchBinding.searchListObserver() = Observer<List<RepositoryDomain>?> { repos ->
        Timber.d("Search Repositories Observer: $repos")
        val query = searchViewModel.searchQuery.value ?: ""
        if (repos.isNotEmpty() || (repos.isEmpty() && query.isNotEmpty())) {
            repositoryAdapter.submitList(repos)
            swipeRefresh.isRefreshing = false
        }
    }

    private fun clashesSearchQueryObserver() = Observer<String> {
        Timber.i("clashesSearchQuery >>> $it")
        searchViewModel.onResetSearchClashes(true)
    }

    private fun BottomSheetSearchBinding.errorObserver() = Observer<String?> {
        it?.let { message ->
            Snackbar.make(root, message, Snackbar.LENGTH_LONG).show()
        }
    }

    // Y ///////////////////////////////////////////////////////////////////// RECYCLER VIEW HANDLER
    private val scrollListener = RecyclerViewScrollListener {
        searchViewModel.onLoadMoreClashes()
    }

    private fun BottomSheetSearchBinding.setupRecyclerView() {
        rvRepositories.apply {
            addOnScrollListener(scrollListener)
            adapter = repositoryAdapter
        }
    }

}