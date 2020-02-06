package com.alan.eventbusdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alan.eventbusdemo.event.HelloVietnamEvent
import com.alan.eventbusdemo.event.HelloWorldEvent
import com.alan.eventbusdemo.eventbus.EventBus
import com.alan.eventbusdemo.eventbus.observer.SimpleEventObserver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var eventBus: EventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eventBus = EventBus.getInstace()

        registerClickEvent()
    }

    private fun registerClickEvent() {
        btn_start_listen.setOnClickListener {
            eventBus.register(HelloWorldEvent.TYPE, listener)
            eventBus.register(HelloVietnamEvent.TYPE, listener)
        }
        btn_stop_listen.setOnClickListener {
            eventBus.unregister(HelloWorldEvent.TYPE, listener)
            eventBus.unregister(HelloVietnamEvent.TYPE, listener)
            clearData()
        }
        btn_hello_world.setOnClickListener {
            eventBus.notify(HelloWorldEvent("Hello World!"), 1000)
        }
        btn_hello_viet_name.setOnClickListener {
            eventBus.notify(HelloVietnamEvent("Hello Vietnam!"))
        }
    }

    private fun clearData() {
        tv_content.text = ""
    }

    private val listener = object : SimpleEventObserver() {
        override fun onEvent(event: HelloWorldEvent) {
            tv_content.text = event.getData()
        }

        override fun onEvent(event: HelloVietnamEvent) {
            tv_content.text = event.getData()
        }
    }
}
