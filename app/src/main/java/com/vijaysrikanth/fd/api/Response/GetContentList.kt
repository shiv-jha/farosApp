package com.vijaysrikanth.fd.api.Response

import com.google.gson.annotations.SerializedName

data class GetContentList (
    @SerializedName("library_id")
    var library_id: String? = null,

    @SerializedName("content_type")
    var content_type: String? = null,

    @SerializedName("content_location")
    var content_location: String? = null,

    @SerializedName("content_size")
    var content_size: String? = null,

    @SerializedName("last_updated")
    var last_updated: String? = null,

    @SerializedName("last_updated_by")
    var last_updated_by: String? = null,

    @SerializedName("content_title")
    var content_title: String? = null,

    @SerializedName("layout_count")
    var layout_count: String? = null,
)