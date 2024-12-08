package com.example.camera

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.camera.databinding.ActivityVideoPlayerBinding
import android.Manifest
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.graphics.Color
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import java.io.File

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    companion object{
        var mVideoFile:File?=null
    }
     override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoPath: String = intent.getStringExtra("VIDEO_PATH")?: ""
//        val videoUri = intent.("VIDEO_URI")
        Log.d(">>>>>>>>>VideoUri>>>>>>>>>>>>>", videoPath.toString())

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video_player)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
         binding.playBtn.setOnClickListener {
             binding.videoView.start()
         }
        if (mVideoFile!=null) {
             val REQUEST_CODE = 1001
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
            } else {
                    mVideoFile?.let { initializePlayer(it) }
                // Permission granted, initialize the player
            }
//            initializePlayer(videoUri)
        } else {
           Toast.makeText(this,"No Video Capture..", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializePlayer(file: File) {

        // Create a Uri for the video path
        val videoUri = Uri.fromFile(file)
        // val videoUri = Uri.parse(NEW_IMAGE_BASE_URL+videoUrl)


        // Set the video URI to the VideoView
        binding.videoView.setVideoURI(videoUri)
        binding.videoView.setZOrderOnTop(true);
        // Create MediaController to enable controls (play, pause, seek)
        val mediaController = MediaController(this)
        //mediaController.visibility = View.GONE;
        mediaController.setAnchorView(binding.videoView)

        // Set MediaController for the VideoView
        binding.videoView.setMediaController(mediaController)

        // Start the video
        // videoView.start()
        binding.videoView.setOnPreparedListener { mediaPlayer ->
            Log.d("VideoView", "Video prepared")
            mediaPlayer.setOnInfoListener { mp, what, extra ->
                Log.d("VideoView", "Info: what=$what, extra=$extra")
                false
            }
            mediaPlayer.setOnErrorListener { mp, what, extra ->
                Log.e("VideoView", "Error: what=$what, extra=$extra")
                true
            }
        }
        binding.videoView.setOnErrorListener { mp, what, extra ->
            // Handle the error here
            Log.e("VideoPlayerActivity", "Error playing video: what=$what, extra=$extra")
            Toast.makeText(this, "An error occurred while playing the video.", Toast.LENGTH_SHORT).show()
            // Force black background on error
            binding.videoView.setBackgroundColor(Color.BLACK)
            return@setOnErrorListener true
        }
    }
    override fun onPause() {
        super.onPause()
        binding.videoView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.pause()
    }
}