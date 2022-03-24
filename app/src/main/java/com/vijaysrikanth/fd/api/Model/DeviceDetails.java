package com.vijaysrikanth.fd.api.Model;

import com.google.gson.annotations.SerializedName;

public class DeviceDetails
{
    @SerializedName("device_id")
    String device_id;
    @SerializedName("secret_key")
    String secret_key = null;

    public DeviceDetails(String mDeviceId,String mSecretkey)
    {
        this.device_id = mDeviceId;
        this.secret_key = mSecretkey;
    }
}
