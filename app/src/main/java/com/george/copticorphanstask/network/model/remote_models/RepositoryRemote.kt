package com.george.copticorphanstask.network.model.remote_models

import com.google.gson.annotations.SerializedName

data class RepositoryRemote @JvmOverloads constructor(
    var id: Int,
    var name: String,
    @SerializedName("private") var isPrivate: Boolean,
    var owner: OwnerRemote,
    @SerializedName("html_url") var htmlUrl: String,
    var description: String? = "",
    @SerializedName("created_at") var createdAt: String? = "N/A",
    var language: String? = "N/A",
    var visibility: String? = if (isPrivate) "private" else "public",
    @SerializedName("default_branch") var defaultBranch: String,
    @SerializedName("forks_count") var forks: Int,
    @SerializedName("watchers_count") var watchers: Int,
    @SerializedName("stargazers_count") var stars: Int,
)