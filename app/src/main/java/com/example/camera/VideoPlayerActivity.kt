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

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val videoUri: Uri? = intent.getParcelableExtra("VIDEO_URI")
//        val videoUri = intent.("VIDEO_URI")
        Log.d(">>>>>>>>>VideoUri>>>>>>>>>>>>>", videoUri.toString())

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (videoUri != null) {
             val REQUEST_CODE = 1001
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
            } else {
                if(ActivityCompat.checkSelfPermission(this, READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(READ_MEDIA_VIDEO), 13)
                }else{
                    initializePlayer(videoUri)
                }
                // Permission granted, initialize the player
            }
//            initializePlayer(videoUri)
        } else {
           Toast.makeText(this,"No Video Capture..", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializePlayer(videoUri: Uri) {
        Log.d("id it cslling","${videoUri}")
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        val mediaItem = MediaItem.fromUri(videoUri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}