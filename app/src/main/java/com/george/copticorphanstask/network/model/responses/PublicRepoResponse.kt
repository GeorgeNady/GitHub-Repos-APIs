package com.george.copticorphanstask.network.model.responses

import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote

data class PublicRepoResponse(
    var repositories: MutableList<RepositoryRemote>
)
