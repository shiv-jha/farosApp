package com.vijaysrikanth.fd


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder
import com.vijaysrikanth.fd.api.Response.GetContentList
import java.io.File


class ImageAdapter(var list: ArrayList<GetContentList>?, var ctx: Context) : PagerAdapter() {
    private lateinit var ImgList: ArrayList<String>
    lateinit var layoutInflater: LayoutInflater


    override fun getCount(): Int {
        return list?.size!!
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(ctx)
        var view = layoutInflater.inflate(R.layout.item, container, false)
        val img = view.findViewById<ImageView>(R.id.imageViewMain)
        Log.e( "instantiateItem: ", ""+list?.get(position)?.content_title+"\n"+list?.get(position)?.library_id)
        if (list?.get(position)?.content_type.equals("image")) {
            img.visibility = View.VISIBLE
            Glide.with(ctx)
                .load("http://3.17.129.226:3000" + list?.get(position)?.content_location)
                .placeholder(R.drawable.placeholder)
                .skipMemoryCache(true)
                .into(img)
        }
        else
        {
            val intent = Intent(ctx, VideoActivity::class.java)
            intent.putExtra("URL","http://3.17.129.226:3000" + list?.get(position)?.content_location)
            ctx.startActivity(intent)
        }
        container.addView(view, 0)
        return view

    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


}