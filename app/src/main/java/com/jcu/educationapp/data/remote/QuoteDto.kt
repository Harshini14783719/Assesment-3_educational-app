package com.jcu.educationapp.data.remote

import com.google.gson.annotations.SerializedName

data class QuoteDto(
    @SerializedName("id") val id: Int,
    @SerializedName("quote") val quote: String,
    @SerializedName("author") val author: String
)

data class FactDto(
    @SerializedName("id") val id: String?,
    @SerializedName("text") val text: String?
)
