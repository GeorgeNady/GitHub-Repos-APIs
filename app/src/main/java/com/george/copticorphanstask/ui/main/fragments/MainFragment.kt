package com.george.copticorphanstask.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.george.copticorphanstask.adapter.repository.RepositoryAdapter
import com.george.copticorphanstask.databinding.FragmentMainBinding
import com.george.copticorphanstask.domain.RepositoryDomain
import com.george.copticorphanstask.ui.main.GithubViewModel
import com.george.copticorphanstask.util.RecyclerViewScrollListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val githubViewModel by activityViewModels<GithubViewModel>()
    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }

    @Inject lateinit var repositoryAdapter: RepositoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvRepositories.setupRecyclerView()

            lifecycleOwner = this@MainFragment
            bGithubViewModel = githubViewModel

            with(githubViewModel) {
                publicReposList.observe(viewLifecycleOwner, reposListObserver())

                swipeRefresh.setOnRefreshListener {
                    onResetUserRepos()
                }
            }
        }
    }

    private val scrollListener = RecyclerViewScrollListener {
        githubViewModel.onLoadMoreUserRepos()
    }

    private fun RecyclerView.setupRecyclerView() {
        adapter = repositoryAdapter
        addOnScrollListener(scrollListener)
    }

    private fun FragmentMainBinding.reposListObserver() = Observer<List<RepositoryDomain>> {
        Timber.tag("GEORGE").i("list size >>> ${it.size}")
        if (it.isNotEmpty()) {
            repositoryAdapter.submitList(it)
            swipeRefresh.isRefreshing = false
        }
    }

}