package com.example.yaauthorizerapplication.data

import com.squareup.moshi.Json

data class UserInfo(
    @Json(name = "real_name") val realName: String,
    @Json(name = "default_email") val defaultEmail: String,
    @Json(name = "is_avatar_empty") val isAvatarEmpty: Boolean,
    @Json(name = "default_avatar_id") val defaultAvatarId: String
)
