package com.alan.eventbusdemo.eventbus.event

import com.alan.eventbusdemo.eventbus.observer.EventObserver

interface AbstractEvent {

    fun fire(observers: List<EventObserver>)

    fun getType(): String
}