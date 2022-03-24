package com.vijaysrikanth.fd

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import app.com.retrofitwithrecyclerviewkotlin.ApiInterface
import com.sgs.citytax.util.IClickListener
import com.vijaysrikanth.fd.api.Model.DeviceDetails
import com.vijaysrikanth.fd.api.Response.GetContentList
import com.vijaysrikanth.fd.api.Response.GetDeviceDetailsById
import com.vijaysrikanth.fd.api.Response.GetLayoutList
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivityNew : FragmentActivity() {
    lateinit var recyclerView: RecyclerView
    val imageList: ArrayList<GetContentList> = arrayListOf()
    var mViewPagerAdapter: ImageAdapter? = null
    var mViewPager: ViewPager? = null
    var i: Int = 0
    var progressDialog: ProgressDialog? = null
//    private var mDrawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("getSerialNumber",getSerialNumber() )
        mViewPager = findViewById(R.id.viewPagerMain)
        recyclerView = findViewById(R.id.recyclerview)
//        mDrawerLayout = findViewById(R.id.drw_layout);

        progressDialog = ProgressDialog(this@MainActivityNew)
        progressDialog?.setMessage("loading..., please wait")
        progressDialog?.show()

        val details =  DeviceDetails(getSerialNumber(),null)
        val apiInterface1 = ApiInterface.create().getDeviceDetailsById(details)
        if (apiInterface1 != null) {
            apiInterface1.enqueue( object : Callback<List<GetDeviceDetailsById>>{
                override fun onResponse(call: Call<List<GetDeviceDetailsById>>?, response: Response<List<GetDeviceDetailsById>>?) {
                    progressDialog?.dismiss()
                    if(response?.body() != null && (response.body() as List<GetDeviceDetailsById>).size>0)
                    {
                        Log.e("GetDeviceDetailsById", "onResponse1: "+(response.body() as List<GetDeviceDetailsById>).get(0).content_location )
                        getLayoutInfo((response.body() as List<GetDeviceDetailsById>).get(0).content_location)

                    }
                    else
                    {
                        showDialog("text")
                    }
                }
                override fun onFailure(call: Call<List<GetDeviceDetailsById>>?, t: Throwable?) {
                    Log.e("GetDeviceDetailsById", "onResponse: "+t )
                }
            })
        }


    }

    private fun getLayoutInfo(contentLocation: String?)
    {
        progressDialog = ProgressDialog(this@MainActivityNew)
        progressDialog?.setMessage("loading..., please wait")
        progressDialog?.show()
        val apiInterface = ApiInterface.create().getLayoutDetailsById(contentLocation)
        if (apiInterface != null) {
            apiInterface.enqueue( object : Callback<List<GetLayoutList>>{
                override fun onResponse(call: Call<List<GetLayoutList>>?, response: Response<List<GetLayoutList>>?) {
                    progressDialog?.dismiss()

                    if(response?.body() != null)
                    {
                        Log.e("GetLayoutList", "onResponse: "+(response.body() as List<GetLayoutList>).get(0).layout_content )
                        imageList.clear()
                        i=0
                        try {
                            var jsonObject: JSONObject? = null
                            val jsonArray = JSONArray((response.body() as List<GetLayoutList>).get(0).layout_content)
                            for (index in 0 until jsonArray.length()) {
                                jsonObject = jsonArray.getJSONObject(index)!!
                                callLayoutImage(jsonObject, jsonArray.length())
                            }
                        } catch (e: JSONException) {
                            Log.e( "JSONException: ",""+e.toString() )
                            Toast.makeText(this@MainActivityNew, "No Content", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<List<GetLayoutList>>?, t: Throwable?) {
                    Log.e("GetLayoutList", "onResponse: "+t )
                }
            })
        }
    }

    private fun showDialog(title: String) {
        val dialog = Dialog(this@MainActivityNew)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        val body = dialog.findViewById(R.id.edtKey) as EditText
        val btnSubmit = dialog.findViewById(R.id.btn_Submit) as Button
        btnSubmit.setOnClickListener {
            dialog.dismiss()
            progressDialog = ProgressDialog(this@MainActivityNew)
            progressDialog?.setMessage("loading..., please wait")
            progressDialog?.show()
            val details =  DeviceDetails(getSerialNumber(),body.text.toString())
            val apiInterface1 = ApiInterface.create().addNewDevice(details)
            if (apiInterface1 != null) {
                apiInterface1.enqueue( object : Callback<List<GetDeviceDetailsById>>{
                    override fun onResponse(call: Call<List<GetDeviceDetailsById>>?, response: Response<List<GetDeviceDetailsById>>?) {
                        progressDialog?.dismiss()
                        if(response?.body() != null && (response.body() as List<GetDeviceDetailsById>).size>0)
                        {
                            Log.e("GetDeviceDetailsById", "onResponse2: "+(response.body() as List<GetDeviceDetailsById>).get(0).content_location )
                            getLayoutInfo((response.body() as List<GetDeviceDetailsById>).get(0).content_location)

                        }
                        else
                        {
                            showDialog("text")
                        }
                    }
                    override fun onFailure(call: Call<List<GetDeviceDetailsById>>?, t: Throwable?) {
                        Log.e("GetDeviceDetailsById", "onResponse: "+t )
                    }
                })
            }
        }
        dialog.show()

    }

    private fun callLayoutImage(jsonObject: JSONObject?, length: Int)
    {
//        progressDialog = ProgressDialog(this@MainActivityNew)
//        progressDialog?.setMessage("loading..., please wait")
//        progressDialog?.show()
        val apiInterface = ApiInterface.create().getContentList1(jsonObject?.getString("content_id"))
        if (apiInterface != null) {
            apiInterface.enqueue( object : Callback<List<GetContentList>>{
                override fun onResponse(call: Call<List<GetContentList>>?, response: Response<List<GetContentList>>?) {
//                    progressDialog?.dismiss()

                    if(response?.body() != null)
                    {
                        Log.e("GetContentList", "onResponse: "+(response.body() as List<GetContentList>).get(0).content_location )
                        i++
                        imageList.add((response.body() as List<GetContentList>).get(0))
                        if (i==length)
                        {
                            displayInfo(imageList,jsonObject?.getString("screen_time"))
                        }
                    }
                }

                override fun onFailure(call: Call<List<GetContentList>>?, t: Throwable?) {
                    Log.e("GetContentList", "onResponse: "+t )
                }
            })
        }
    }

    private fun displayInfo(imageList: ArrayList<GetContentList>?, screenTime: String?)
    {
        mViewPagerAdapter = ImageAdapter(imageList,this)
        mViewPager?.setAdapter(mViewPagerAdapter);

        val handler = Handler()
        var page: Int? = 0
        val Update = Runnable {
            val numPages: Int = mViewPagerAdapter!!.count
            page = (page?.plus(1))?.rem(numPages)
            mViewPager!!.setCurrentItem(page!!, true)
        }
        val timer: Timer = Timer()
        timer.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {handler.post(Update)
            }
//        }, screenTime.toString().toLong()*60*1000, screenTime.toString().toLong()*60*1000)
        }, 25000, 25000)
    }


    private fun getSerialNumber(): String {
        val deviceId: String

        deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Secure.getString(
                getContentResolver(),
                Secure.ANDROID_ID
            )
        } else {
            val mTelephony = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (mTelephony.deviceId != null) {
                mTelephony.deviceId
            } else {
                Secure.getString(
                    getContentResolver(),
                    Secure.ANDROID_ID
                )
            }
        }
        return deviceId;
    }

//    private fun getSerialNumber1(): String {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                return try {
//                    Build.getSerial()
//                } catch (e: java.lang.Exception) {
//                    Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
//                }
//            } else
//                requestForPermission(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 2)
//        } else {
//            return Build.SERIAL
//        }
//        return ""
//    }

    private fun requestForPermission(activity: Activity, permission: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permission, requestCode)
    }

}