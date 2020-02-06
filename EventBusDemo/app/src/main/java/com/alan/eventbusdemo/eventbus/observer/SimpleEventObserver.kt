package com.alan.eventbusdemo.eventbus.observer

import com.alan.eventbusdemo.event.HelloVietnamEvent
import com.alan.eventbusdemo.event.HelloWorldEvent

open class SimpleEventObserver : EventObserver {

    override fun onEvent(event: HelloWorldEvent) {
        /* Override here */
    }

    override fun onEvent(event: HelloVietnamEvent) {
        /* Override here */
    }
}