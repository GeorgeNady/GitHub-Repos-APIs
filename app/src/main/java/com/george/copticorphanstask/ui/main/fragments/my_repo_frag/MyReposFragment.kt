package com.george.copticorphanstask.ui.main.fragments.my_repo_frag

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.george.copticorphanstask.R.layout.fragment_my_repos
import com.george.copticorphanstask.adapter.repository.RepositoryAdapter
import com.george.copticorphanstask.base.fragments.ActivityFragmentAnnoation
import com.george.copticorphanstask.base.fragments.MainBaseFragment
import com.george.copticorphanstask.databinding.FragmentMyReposBinding
import com.george.copticorphanstask.databinding.FragmentPublicReposBinding
import com.george.copticorphanstask.domain.RepositoryDomain
import com.george.copticorphanstask.util.RecyclerViewScrollListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("NonConstantResourceId")
@ActivityFragmentAnnoation(fragment_my_repos)
class MyReposFragment : MainBaseFragment<FragmentMyReposBinding>() {

    @Inject lateinit var repositoryAdapter: RepositoryAdapter
    private val myReposViewModel by activityViewModels<MyReposViewModel>()

    override fun FragmentMyReposBinding.initialization() {
        lifecycleOwner = this@MyReposFragment
        bMyReposViewModel = myReposViewModel
        setupRecyclerView()

    }

    override fun FragmentMyReposBinding.setListener() {
        with(myReposViewModel) {

            toolbar.setupMaterialToolbar()

            userReposList.observe(viewLifecycleOwner, listObserver())
            errorShowEvent.observe(viewLifecycleOwner, errorObserver())

            swipeRefresh.setOnRefreshListener {
                onResetUserRepos()
            }

        }

    }

    // Y ///////////////////////////////////////////////////////////////////////////// RECYCLER VIEW
    private val scrollListener = RecyclerViewScrollListener {
        myReposViewModel.onLoadMoreUserRepos()
    }

    private fun FragmentMyReposBinding.setupRecyclerView() {
        rvRepositories.apply {
            addOnScrollListener(scrollListener)
            adapter = repositoryAdapter
        }
    }

    // Y ///////////////////////////////////////////////////////////////////////////////// OBSERVERS
    private fun FragmentMyReposBinding.listObserver() =
        Observer<List<RepositoryDomain>> { domainRepos ->
            if (domainRepos.isNotEmpty()) {
                repositoryAdapter.submitList(domainRepos)
                swipeRefresh.isRefreshing = false
            }
        }

    private fun FragmentMyReposBinding.errorObserver() = Observer<String?> {
        it?.let { message ->
            Snackbar.make(requireContext(), root, message, Snackbar.LENGTH_LONG).show()
        }
    }

}
