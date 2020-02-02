package com.alan.app.mvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alan.app.mvvm.R
import com.alan.app.mvvm.ui.first.FirstFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_go_first.setOnClickListener {
            val intent = OpenActivity.getIntent(applicationContext, FirstFragment::class.java)
            startActivity(intent)
        }
    }
}
