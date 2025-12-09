package com.malsync.android.data.remote.dto.kitsu

import com.google.gson.annotations.SerializedName

/**
 * Kitsu API DTOs (JSON:API format)
 */

data class KitsuLibraryResponse(
    @SerializedName("data")
    val data: List<KitsuLibraryEntry>,
    @SerializedName("included")
    val included: List<KitsuAnime>?,
    @SerializedName("meta")
    val meta: KitsuMeta?,
    @SerializedName("links")
    val links: KitsuLinks?
)

data class KitsuLibraryEntry(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("attributes")
    val attributes: KitsuLibraryAttributes,
    @SerializedName("relationships")
    val relationships: KitsuRelationships?
)

data class KitsuLibraryAttributes(
    @SerializedName("status")
    val status: String,
    @SerializedName("progress")
    val progress: Int,
    @SerializedName("reconsuming")
    val reconsuming: Boolean,
    @SerializedName("reconsumeCount")
    val reconsumeCount: Int,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class KitsuAnime(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("attributes")
    val attributes: KitsuAnimeAttributes
)

data class KitsuAnimeAttributes(
    @SerializedName("slug")
    val slug: String,
    @SerializedName("canonicalTitle")
    val canonicalTitle: String,
    @SerializedName("titles")
    val titles: Map<String, String>?,
    @SerializedName("abbreviatedTitles")
    val abbreviatedTitles: List<String>?,
    @SerializedName("synopsis")
    val synopsis: String?,
    @SerializedName("coverImage")
    val coverImage: KitsuImage?,
    @SerializedName("posterImage")
    val posterImage: KitsuImage?,
    @SerializedName("episodeCount")
    val episodeCount: Int?,
    @SerializedName("episodeLength")
    val episodeLength: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("startDate")
    val startDate: String?,
    @SerializedName("endDate")
    val endDate: String?,
    @SerializedName("averageRating")
    val averageRating: String?,
    @SerializedName("showType")
    val showType: String?,
    @SerializedName("subtype")
    val subtype: String?
)

data class KitsuImage(
    @SerializedName("tiny")
    val tiny: String?,
    @SerializedName("small")
    val small: String?,
    @SerializedName("medium")
    val medium: String?,
    @SerializedName("large")
    val large: String?,
    @SerializedName("original")
    val original: String?
)

data class KitsuRelationships(
    @SerializedName("anime")
    val anime: KitsuRelationship?,
    @SerializedName("manga")
    val manga: KitsuRelationship?,
    @SerializedName("user")
    val user: KitsuRelationship?
)

data class KitsuRelationship(
    @SerializedName("links")
    val links: Map<String, String>?,
    @SerializedName("data")
    val data: KitsuResourceIdentifier?
)

data class KitsuMeta(
    @SerializedName("count")
    val count: Int?
)

data class KitsuLinks(
    @SerializedName("first")
    val first: String?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("last")
    val last: String?
)

data class KitsuAnimeDetailResponse(
    @SerializedName("data")
    val data: KitsuAnime
)

data class KitsuAnimeSearchResponse(
    @SerializedName("data")
    val data: List<KitsuAnime>,
    @SerializedName("meta")
    val meta: KitsuMeta?,
    @SerializedName("links")
    val links: KitsuLinks?
)

data class KitsuLibraryEntryRequest(
    @SerializedName("data")
    val data: KitsuLibraryEntryData
)

data class KitsuLibraryEntryData(
    @SerializedName("type")
    val type: String = "libraryEntries",
    @SerializedName("attributes")
    val attributes: KitsuLibraryEntryAttributes,
    @SerializedName("relationships")
    val relationships: KitsuLibraryEntryRelationships
)

data class KitsuLibraryEntryAttributes(
    @SerializedName("status")
    val status: String?,
    @SerializedName("progress")
    val progress: Int?,
    @SerializedName("rating")
    val rating: String?
)

data class KitsuLibraryEntryRelationships(
    @SerializedName("user")
    val user: KitsuRelationshipData,
    @SerializedName("anime")
    val anime: KitsuRelationshipData
)

data class KitsuRelationshipData(
    @SerializedName("data")
    val data: KitsuResourceIdentifier
)

data class KitsuResourceIdentifier(
    @SerializedName("type")
    val type: String,
    @SerializedName("id")
    val id: String
)

data class KitsuLibraryEntryResponse(
    @SerializedName("data")
    val data: KitsuLibraryEntry
)

data class KitsuUserResponse(
    @SerializedName("data")
    val data: List<KitsuUser>
)

data class KitsuUserDetailResponse(
    @SerializedName("data")
    val data: KitsuUser
)

data class KitsuUser(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("attributes")
    val attributes: KitsuUserAttributes
)

data class KitsuUserAttributes(
    @SerializedName("name")
    val name: String,
    @SerializedName("avatar")
    val avatar: KitsuImage?,
    @SerializedName("lifeSpentOnAnime")
    val lifeSpentOnAnime: Int?,
    @SerializedName("animeCount")
    val animeCount: Int?,
    @SerializedName("mangaCount")
    val mangaCount: Int?
)
