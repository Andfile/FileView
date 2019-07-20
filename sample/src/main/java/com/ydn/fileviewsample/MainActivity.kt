package com.ydn.fileviewsample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.ydn.fileview.FileView
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private val TAG = "FileViewDemo"
    private val PERMISSION_REQUEST_CODE: Int = 101

    private lateinit var fileView: FileView
    private var scrolled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        copyFromAssets()
        setupPermissions()

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            if (scrolled) {
                button.setText("bottom")
                fileView.scrollTop()
            } else {
                button.setText("top")
                fileView.scrollBottom()
            }
            scrolled = !scrolled
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            init()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                    finishActivity(0)
                } else {
                    init()
                }
            }
        }
    }

    private fun init() {
        fileView = findViewById(R.id.fileview)
        fileView.setFilename(this.filesDir.absolutePath + "/file.txt")
                .setLayoutRes(R.layout.fileview_item)
                .setPageSize(50)
                .scrollTop()

    }

    private fun copyFromAssets() {
        val bufferSize = 1024
        val assetManager = this.assets

        val inputStream = assetManager.open("asc6.txt")
        val outputStream = FileOutputStream(File(this.filesDir, "file.txt"))

        try {
            inputStream.copyTo(outputStream, bufferSize)
        } finally {
            inputStream.close()
            outputStream.flush()
            outputStream.close()
        }
    }
}
