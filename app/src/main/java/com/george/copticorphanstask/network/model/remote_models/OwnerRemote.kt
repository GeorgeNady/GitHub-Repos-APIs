package com.george.copticorphanstask.network.model.remote_models

import com.google.gson.annotations.SerializedName

data class OwnerRemote(
    var id: Int,
    @SerializedName("node_id") var nodeId: String,
    @SerializedName("login") var name: String,
    @SerializedName("avatar_url") var avatarUrl: String,
    var url: String,
    @SerializedName("html_url") var htmlUrl: String,
    var type: String,
)