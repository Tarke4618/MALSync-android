package com.malsync.android.data.remote.dto.shikimori

import com.google.gson.annotations.SerializedName

data class ShikimoriAnime(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("russian") val russian: String?,
    @SerializedName("image") val image: ShikimoriImage?,
    @SerializedName("url") val url: String?,
    @SerializedName("kind") val kind: String?,
    @SerializedName("score") val score: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("episodes_aired") val episodesAired: Int?,
    @SerializedName("aired_on") val airedOn: String?,
    @SerializedName("released_on") val releasedOn: String?
)

data class ShikimoriImage(
    @SerializedName("original") val original: String?,
    @SerializedName("preview") val preview: String?,
    @SerializedName("x96") val x96: String?,
    @SerializedName("x48") val x48: String?
)

data class ShikimoriUserRate(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("target_id") val targetId: Int, // Anime ID
    @SerializedName("target_type") val targetType: String, // "Anime"
    @SerializedName("score") val score: Int,
    @SerializedName("status") val status: String,
    @SerializedName("rewatches") val rewatches: Int,
    @SerializedName("episodes") val episodes: Int,
    @SerializedName("text") val text: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
