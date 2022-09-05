package com.george.copticorphanstask.network

import com.george.copticorphanstask.domain.OwnerDomain
import com.george.copticorphanstask.domain.RepositoryDomain
import com.george.copticorphanstask.network.model.remote_models.OwnerRemote
import com.george.copticorphanstask.network.model.remote_models.RepositoryRemote

fun List<RepositoryRemote>.asDomainModels() = map {
    RepositoryDomain(
        id = it.id,
        name = it.name,
        isPrivate = it.isPrivate,
        owner = it.owner.asDomainModel(),
        htmlUrl = it.htmlUrl,
        description = it.description,
        createdAt = it.createdAt,
        language = it.language,
        visibility = it.visibility,
        defaultBranch = it.defaultBranch,
        forks = it.forks,
        watchers = it.watchers,
        stars = it.stars,
    )
}

fun RepositoryRemote.asDomainModel() = RepositoryDomain(
    id = id,
    name = name,
    isPrivate = isPrivate,
    owner = owner.asDomainModel(),
    htmlUrl = htmlUrl,
    description = description,
    createdAt = createdAt,
    language = language,
    visibility = visibility,
    defaultBranch = defaultBranch,
    forks = forks,
    watchers = watchers,
    stars = stars
)

fun OwnerRemote.asDomainModel() = OwnerDomain(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl,
)