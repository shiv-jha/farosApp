package com.vijaysrikanth.fd

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import app.com.retrofitwithrecyclerviewkotlin.ApiInterface
import com.sgs.citytax.util.IClickListener
import com.vijaysrikanth.fd.api.Response.GetContentList
import com.vijaysrikanth.fd.api.Response.GetLayoutList
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : FragmentActivity(), IClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerAdapter
    val imageList: ArrayList<GetContentList> = arrayListOf()
    var mViewPagerAdapter: ImageAdapter? = null
    var mViewPager: ViewPager? = null
    var i: Int = 0
    var progressDialog: ProgressDialog? = null
//    private var mDrawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewPager = findViewById(R.id.viewPagerMain)
        recyclerView = findViewById(R.id.recyclerview)
//        mDrawerLayout = findViewById(R.id.drw_layout);

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog?.setMessage("loading..., please wait")
        recyclerAdapter = RecyclerAdapter(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = recyclerAdapter
        progressDialog?.show()
        val apiInterface = ApiInterface.create().getLayoutList()
        apiInterface.enqueue( object : Callback<List<GetLayoutList>>{
            override fun onResponse(call: Call<List<GetLayoutList>>?, response: Response<List<GetLayoutList>>?) {
                progressDialog?.dismiss()
                if(response?.body() != null)
                {
                    Log.e("GetLayoutList", "onResponse1: "+(response.body() as List<GetLayoutList>) )
                    val  mGetLayoutList = response.body()?.get(0)
                    recyclerAdapter.setListItems(response.body() as List<GetLayoutList>)
                    recyclerAdapter.notifyItemChanged(0)

                }
            }

            override fun onFailure(call: Call<List<GetLayoutList>>?, t: Throwable?) {
                Log.e("GetLayoutList", "onResponse: "+t )
            }
        })

    }

    private fun callLayoutImage(jsonObject: JSONObject?, length: Int)
    {
        progressDialog?.show()
        val apiInterface = ApiInterface.create().getContentList1(jsonObject?.getString("content_id"))
        if (apiInterface != null) {
            apiInterface.enqueue( object : Callback<List<GetContentList>>{
                override fun onResponse(call: Call<List<GetContentList>>?, response: Response<List<GetContentList>>?) {
                    progressDialog?.dismiss()

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


    override fun onClick(view: View, position: Int, obj: Any) {
        imageList.clear()
        i=0
        try {
            var jsonObject: JSONObject? = null
            val jsonArray = JSONArray((obj as GetLayoutList).layout_content)
            for (index in 0 until jsonArray.length()) {
                jsonObject = jsonArray.getJSONObject(index)!!
                callLayoutImage(jsonObject, jsonArray.length())
            }
        } catch (e: JSONException) {
            Log.e( "JSONException: ",""+e.toString() )
            Toast.makeText(this, "No Content", Toast.LENGTH_SHORT).show()
        }
//        mDrawerLayout?.closeDrawers();
    }

    override fun onLongClick(view: View, position: Int, obj: Any) {
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

}