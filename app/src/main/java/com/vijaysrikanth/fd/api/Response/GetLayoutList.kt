package com.vijaysrikanth.fd.api.Response

import com.google.gson.annotations.SerializedName

data class GetLayoutList (
    @SerializedName("layout_id")
    var layout_id: String? = null,

    @SerializedName("layout_content")
    var layout_content: String? = null,

//    @SerializedName("layout_content")
//    var layout_content: List<LayoutContent>? = null,

    @SerializedName("last_updated")
    var last_updated: String? = null,

    @SerializedName("last_updated_by")
    var last_updated_by: String? = null,

    @SerializedName("store_id")
    var store_id: String? = null,

    @SerializedName("layout_name")
    var layout_name: String? = null,
)