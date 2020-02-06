package com.alan.eventbusdemo.event

import com.alan.eventbusdemo.eventbus.event.BaseEvent
import com.alan.eventbusdemo.eventbus.observer.EventObserver

class HelloVietnamEvent
constructor(data: String) : BaseEvent<String>(data) {

    override fun fire(observers: List<EventObserver>) {
        observers.forEach { it.onEvent(this) }
    }

    override fun getType(): String {
        return TYPE
    }

    companion object {
        var TYPE = this::class.java.name
    }
}