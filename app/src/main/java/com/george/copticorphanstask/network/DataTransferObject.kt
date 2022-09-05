package com.george.copticorphanstask.network

import com.george.copticorphanstask.domain.OwnerDomain
import com.george.copticorphanstask.domain.RepositoryDomain
import com.george.copticorphanstask.network.model.remote_models.OwnerRemote
import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote
import com.george.copticorphanstask.network.model.responses.PublicRepoResponse

fun PublicRepoResponse.asDomainModels(): List<RepositoryDomain> {
    return this.repositories.map {
        RepositoryDomain(
            id = it.id,
            name = it.name,
            isPrivate = it.isPrivate,
            owner = it.owner.asDomainModel(),
            htmlUrl = it.htmlUrl,
            description = it.description,
            fork = it.fork,
            createdAt = it.createdAt,
            language = it.language,
            visibility = it.visibility,
            default_branch = it.default_branch,
        )
    }
}

fun RepositoryRemote.asDomainModel(): RepositoryDomain {
    return RepositoryDomain(
        id = id,
        name = name,
        isPrivate = isPrivate,
        owner = owner.asDomainModel(),
        htmlUrl = htmlUrl,
        description = description,
        fork = fork,
        createdAt = createdAt,
        language = language,
        visibility = visibility,
        default_branch = default_branch,
    )
}

fun OwnerRemote.asDomainModel(): OwnerDomain {
    return OwnerDomain(
        id = id,
        nodeId = nodeId,
        avatarUrl = avatarUrl,
        url = url,
        htmlUrl = htmlUrl,
        type = type,
    )
}