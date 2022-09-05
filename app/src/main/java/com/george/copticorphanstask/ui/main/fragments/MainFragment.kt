package com.george.copticorphanstask.ui.main.fragments

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.george.copticorphanstask.R.layout.fragment_main
import com.george.copticorphanstask.adapter.repository.RepositoryAdapter
import com.george.copticorphanstask.base.fragments.ActivityFragmentAnnoation
import com.george.copticorphanstask.base.fragments.MainBaseFragment
import com.george.copticorphanstask.databinding.FragmentMainBinding
import com.george.copticorphanstask.domain.RepositoryDomain
import com.george.copticorphanstask.network.Resource
import com.george.copticorphanstask.network.asDomainModels
import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote
import com.george.copticorphanstask.ui.main.GithubViewModel
import com.george.copticorphanstask.util.RecyclerViewScrollListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("NonConstantResourceId")
@ActivityFragmentAnnoation(fragment_main)
class MainFragment : MainBaseFragment<FragmentMainBinding>() {

    @Inject
    lateinit var repositoryAdapter: RepositoryAdapter
    private val githubViewModel by activityViewModels<GithubViewModel>()

    override fun FragmentMainBinding.initialization() {
        lifecycleOwner = this@MainFragment
        bGithubViewModel = githubViewModel
        setupRecyclerView()

    }

    override fun FragmentMainBinding.setListener() {
        with(githubViewModel) {

            Timber.i("#1 -- ${userReposList.hasObservers()}")
            userReposList.observe(viewLifecycleOwner, listObserver())
            Timber.i("#2 -- ${userReposList.hasObservers()}")

            // bug: never observe the data
            /*lifecycleScope.launchWhenCreated {
                userReposList.observeForever { domainRepos ->
                    Timber.tag("GEORGE").i("Fragment: list size >>> ${domainRepos.size}")
                    if (domainRepos.isNotEmpty()) {
                        repositoryAdapter.submitList(domainRepos)
                        swipeRefresh.isRefreshing = false
                    }
                }
            }*/

            // usersReposMutableLiveData.observe(viewLifecycleOwner, responseObserver())

            swipeRefresh.setOnRefreshListener {
                onResetUserRepos()
            }

        }

    }

    private val scrollListener = RecyclerViewScrollListener {
        githubViewModel.onLoadMoreUserRepos()
    }

    private fun FragmentMainBinding.setupRecyclerView() {
        rvRepositories.apply {
            addOnScrollListener(scrollListener)
            adapter = repositoryAdapter
        }
    }

    // Y ///////////////////////////////////////////////////////////////////////////////// OBSERVERS
    private fun FragmentMainBinding.listObserver() =
        Observer<List<RepositoryDomain>> { domainRepos ->
            Timber.tag("GEORGE").i("Fragment: list size >>> ${domainRepos.size}")
            if (domainRepos.isNotEmpty()) {
                repositoryAdapter.submitList(domainRepos)
                swipeRefresh.isRefreshing = false
            } /*else {
                repositoryAdapter.submitList(fakeRepositories)
                swipeRefresh.isRefreshing = false
            }*/
        }

    private fun FragmentMainBinding.responseObserver() =
        Observer<Resource<MutableList<RepositoryRemote>>?> {
            it.handler(
                mLoading = { swipeRefresh.isRefreshing = true },
                mError = { err ->
                    showSnackBar(root, err)
                    swipeRefresh.isRefreshing = false
                    shimmer.visibility = View.GONE
                },
                mFailed = { err ->
                    showSnackBar(root, err)
                    swipeRefresh.isRefreshing = false
                    shimmer.visibility = View.GONE
                }
            ) { remoteRepos ->
                swipeRefresh.isRefreshing = false
                shimmer.visibility = View.GONE
                rvRepositories.visibility = View.VISIBLE
                val list = remoteRepos.asDomainModels()
                if (list.isNotEmpty()) {
                    repositoryAdapter.submitList(list)
                    swipeRefresh.isRefreshing = false
                } else {
                    showSnackBar(root, "empty list")
                    Timber.i("empty list")
                }
            }
        }

}
