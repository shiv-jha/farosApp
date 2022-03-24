package com.vijaysrikanth.fd.api.Response

import com.google.gson.annotations.SerializedName

data class GetDeviceDetailsById (
    @SerializedName("device_id")
    var library_id: String? = null,

    @SerializedName("secret_key")
    var content_type: String? = null,

    @SerializedName("layout_id")
    var content_location: String? = null,

)