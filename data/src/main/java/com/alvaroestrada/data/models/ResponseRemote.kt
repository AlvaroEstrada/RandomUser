package com.alvaroestrada.data.models

import com.squareup.moshi.Json

data class ResponseRemote(
    @Json(name = "results") val contacts: List<ContactRemote>,
    @Json(name = "info") val info: InfoRemote
)

data class InfoRemote(
    @Json(name = "seed") val seed: String,
    @Json(name = "results") val results: String,
    @Json(name = "page") val page: String,
    @Json(name = "version") val version: String,
)
