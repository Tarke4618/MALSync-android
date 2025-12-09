package com.malsync.android.data.remote.dto.shikimori

import com.google.gson.annotations.SerializedName

data class ShikimoriUserProfile(
    @SerializedName("id") val id: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("image") val image: ShikimoriUserImage?,
    @SerializedName("last_online_at") val lastOnlineAt: String?,
    @SerializedName("url") val url: String?
)

data class ShikimoriUserImage(
    @SerializedName("x160") val x160: String?,
    @SerializedName("x148") val x148: String?,
    @SerializedName("x80") val x80: String?,
    @SerializedName("x64") val x64: String?,
    @SerializedName("x48") val x48: String?,
    @SerializedName("x32") val x32: String?,
    @SerializedName("x16") val x16: String?
)
