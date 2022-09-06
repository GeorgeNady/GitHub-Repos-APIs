package com.george.copticorphanstask.ui.main.fragments.public_repo_frag

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.george.copticorphanstask.R
import com.george.copticorphanstask.R.layout.fragment_public_repos
import com.george.copticorphanstask.adapter.repository.RepositoryAdapter
import com.george.copticorphanstask.base.fragments.ActivityFragmentAnnoation
import com.george.copticorphanstask.base.fragments.MainBaseFragment
import com.george.copticorphanstask.databinding.FragmentPublicReposBinding
import com.george.copticorphanstask.domain.RepositoryDomain
import com.george.copticorphanstask.ui.auth.AuthActivity
import com.george.copticorphanstask.ui.auth.AuthViewModel
import com.george.copticorphanstask.util.RecyclerViewScrollListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("NonConstantResourceId")
@ActivityFragmentAnnoation(fragment_public_repos)
class PublicReposFragment : MainBaseFragment<FragmentPublicReposBinding>() {

    @Inject lateinit var repositoryAdapter: RepositoryAdapter
    private val viewModel by viewModels<PublicReposViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()

    override fun FragmentPublicReposBinding.initialization() {
        lifecycleOwner = this@PublicReposFragment
        bPublicRepoViewModel = viewModel
        setupRecyclerView()
    }

    override fun FragmentPublicReposBinding.setListener() {

        toolbar.setupMaterialToolbar(true, intArrayOf(R.id.logout)) { ints ->
            ints.maxOf {
                when(it) {
                    R.id.logout -> {
                        authViewModel.logout()
                        startActivity<AuthActivity>()
                        true
                    }
                    R.id.search -> {
//                        PublicReposFragmentDirections.searchactin
                        true
                    }
                    else -> false
                }
            }
        }

        with(viewModel) {
            publicReposList.observe(viewLifecycleOwner, listObserver())

            swipeRefresh.setOnRefreshListener {
                onResetPublicRepos()
            }

            btnMyRepos.setOnClickListener {
                findNavController().navigate(
                    PublicReposFragmentDirections.actionPublicReposFragmentToMainFragment())
            }
        }
    }

    // Y ///////////////////////////////////////////////////////////////////////////// RECYCLER VIEW
    private val scrollListener = RecyclerViewScrollListener {
        viewModel.onLoadMorePublicRepos()
    }

    private fun FragmentPublicReposBinding.setupRecyclerView() {
        rvRepositories.apply {
            addOnScrollListener(scrollListener)
            adapter = repositoryAdapter
        }
    }

    // Y ///////////////////////////////////////////////////////////////////////////////// OBSERVERS
    private fun FragmentPublicReposBinding.listObserver() =
        Observer<List<RepositoryDomain>> { domainRepos ->
            Timber.tag("GEORGE").i("Fragment: list size >>> ${domainRepos.size}")
            if (domainRepos.isNotEmpty()) {
                repositoryAdapter.submitList(domainRepos.toList())
                swipeRefresh.isRefreshing = false
            }
        }

}