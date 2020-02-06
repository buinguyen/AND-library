package com.alan.eventbusdemo.eventbus.event

abstract class BaseEvent<T>
constructor(private val data: T) : AbstractEvent {

    fun getData(): T {
        return data
    }
}