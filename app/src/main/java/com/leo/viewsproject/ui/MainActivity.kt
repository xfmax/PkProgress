package com.leo.viewsproject.ui

import android.content.Intent
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.leo.viewsproject.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    private var dialog: LiveSendContentDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            window.decorView.getWindowVisibleDisplayFrame(rect)
            val keyboardHeight = window.decorView.rootView.height - rect.bottom
            Log.e("Leo", "screen height: " + window.decorView.rootView.height)
            Log.e("Leo", "screen width: " + window.decorView.rootView.width)
            Log.e("Leo", "bottom: " + rect.bottom.toString())
            Log.e("Leo", "top: " + rect.top.toString())

            // todo 非全面屏情况需减去虚拟物理键高度
//            dialog?.setFlContainer(-1, keyboardHeight)
        }

        btn_progress.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProgressActivity::class.java))
        }

        btn_switch.setOnClickListener {
            //            startActivity(Intent(this@MainActivity, IndicatorActivity::class.java))
            if (dialog == null) {
                dialog = LiveSendContentDialog(this@MainActivity, "", 0,
                    LiveSendContentDialog.SendTextListener { _, text, chatFlag, targetName, targetUid ->
                        if (!TextUtils.isEmpty(text)) {
                        }
                        false
                    })
            }

            dialog?.show()
        }
    }

    private fun getIndex(i: Int): String {
        return if (i < 10) {
            "0$i"
        } else {
            i.toString()
        }
    }
}
