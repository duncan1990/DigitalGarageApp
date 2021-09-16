package com.ahmety.digitalgarageapp

import com.google.gson.annotations.SerializedName

data class JSONModel(
    @SerializedName("name")
    var name: String?,

    @SerializedName("email")
    var email: String?,

    @SerializedName("hash")
    var hash: String?,
)
