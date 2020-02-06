package com.alan.eventbusdemo.eventbus.observer

import com.alan.eventbusdemo.event.HelloVietnamEvent
import com.alan.eventbusdemo.event.HelloWorldEvent

interface EventObserver {

    fun onEvent(event: HelloWorldEvent)

    fun onEvent(event: HelloVietnamEvent)
}