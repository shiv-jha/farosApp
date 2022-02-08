package com.vijaysrikanth.fd.api.Response

import com.google.gson.annotations.SerializedName

data class LayoutContent(
    @SerializedName("content_id")
    var content_id: String? = null,

    @SerializedName("order")
    var order: String? = null,

    @SerializedName("screen_time")
    var screen_time: String? = null

)