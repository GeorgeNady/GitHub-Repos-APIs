package com.george.copticorphanstask.network.model.remote_models

import com.george.copticorphanstask.domain.OwnerDomain
import com.george.copticorphanstask.domain.RepositoryDomain
import com.google.gson.annotations.SerializedName

data class RepositoryRemote(
    var id: Int,
    var name: String,
    @SerializedName("private") var isPrivate: Boolean,
    var owner: OwnerRemote,
    @SerializedName("html_url") var htmlUrl: String,
    var description: String,
    var fork: Boolean,
    @SerializedName("created_at") var createdAt: String,
    var language: String,
    var visibility: String,
    @SerializedName("default_branch") var default_branch: String
)

data class OwnerRemote(
    var id: Int,
    @SerializedName("node_id") var nodeId: String,
    @SerializedName("avatar_url") var avatarUrl: String,
    var url: String,
    @SerializedName("html_url") var htmlUrl: String,
    var type: String,
)