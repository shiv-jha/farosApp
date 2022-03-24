package com.vijaysrikanth.fd

import android.app.ProgressDialog
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.webkit.WebView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.FragmentActivity


class VideoActivity : FragmentActivity() {
    private val webView :WebView? = null
    private var mUrl :String = ""
    private var simpleVideoView: VideoView? = null
    var mediaControls: MediaController? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        progressDialog = ProgressDialog(this@VideoActivity)
        progressDialog?.setMessage("loading...")
        progressDialog?.setCancelable(true)
        progressDialog?.show()
        mUrl = intent.getStringExtra("URL").toString()
//        val webView = findViewById<WebView>(R.id.webView)
//        webView.webViewClient = WebViewClient()
//        webView.loadUrl(mUrl)
//        webView.settings.javaScriptEnabled = true
        simpleVideoView = findViewById<VideoView>(R.id.videoView)
        if (mediaControls == null) {
            // creating an object of media controller class
            mediaControls = MediaController(this)
            // set the anchor view for the video view
            mediaControls!!.setAnchorView(this.simpleVideoView)
        }
        // set the media controller for video view
        simpleVideoView!!.setMediaController(mediaControls)

        // set the absolute path of the video file which is going to be played
//        simpleVideoView!!.setVideoURI(Uri.parse(mUrl))
        simpleVideoView!!.setVideoPath(mUrl)
        simpleVideoView!!.requestFocus()

        // starting the video
        simpleVideoView!!.start()

        // display a toast message
        // after the video is completed
        simpleVideoView!!.setOnCompletionListener {
            finish()
        }

        // display a toast message if any
        // error occurs while playing the video
        simpleVideoView!!.setOnErrorListener { mp, what, extra ->
            Toast.makeText(applicationContext, "An Error Occured " +
                    "While Playing Video !!!", Toast.LENGTH_LONG).show()
            finish()
            false

        }
        simpleVideoView!!.setOnPreparedListener(OnPreparedListener { mp ->
            mp.setOnInfoListener { mp, what, extra ->
//                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) progressDialog!!.show()
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) progressDialog!!.show()
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) progressDialog!!.dismiss()
                false
            }
        })

    }
    override fun onBackPressed() {
//        if (webView?.canGoBack() == true)
//            webView.goBack()
//        else
//            super.onBackPressed()
        if (progressDialog?.isShowing == true)
        {
            progressDialog?.dismiss()
        }
        finish()
    }
}