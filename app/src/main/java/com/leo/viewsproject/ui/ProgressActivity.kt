package com.leo.viewsproject.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.leo.viewsproject.R
import com.leo.viewsproject.widget.PKProgressBar
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ProgressActivity : AppCompatActivity() {
    private var btnstart: Button? = null
    private var pkProgressBar: PKProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)
        btnstart = findViewById(R.id.btn_start)
        pkProgressBar = findViewById(R.id.progress)
        pkProgressBar?.setAnimProgress(100,100)
        pkProgressBar?.leftTeamName = "卡卡特罗队"
        pkProgressBar?.leftTeamCount = "38人"
        with(btnstart) {
            this?.setOnClickListener(View.OnClickListener {
                val executor = Executors.newScheduledThreadPool(1)
                executor.scheduleAtFixedRate(
                    {
                        var countLeft = Random.nextInt(100)
                        val countRight = 100 - countLeft
                        Log.d("qq", "onCreate: $countLeft,$countRight")
                        runOnUiThread {
                            pkProgressBar?.setAnimProgress(countLeft.toLong(), countRight.toLong())
                        }
                    },
                    0,
                    1000,
                    TimeUnit.MILLISECONDS
                )
            })
        }
    }
}
