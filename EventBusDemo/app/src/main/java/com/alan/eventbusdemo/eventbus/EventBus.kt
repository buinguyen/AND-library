package com.alan.eventbusdemo.eventbus

import android.os.Handler
import com.alan.eventbusdemo.eventbus.event.AbstractEvent
import com.alan.eventbusdemo.eventbus.observer.EventObserver
import java.util.*

class EventBus private constructor() {

    private var mHandler: Handler = Handler()

    private var mObserverMap: MutableMap<String, MutableList<EventObserver>> =
        Collections.synchronizedMap(mutableMapOf<String, MutableList<EventObserver>>())

    @Synchronized
    fun register(eventType: String, eventObserver: EventObserver) {
        var observers = mObserverMap[eventType]
        if (observers == null) {
            observers = Collections.synchronizedList(mutableListOf<EventObserver>())
        }
        if (observers != null) {
            observers.add(eventObserver)
            mObserverMap[eventType] = observers
        }
    }

    fun unregister(eventType: String, eventObserver: EventObserver) {
        val observers = mObserverMap[eventType]
        observers?.remove(eventObserver)
        if (observers.isNullOrEmpty()) {
            mObserverMap.remove(eventType)
        }
    }

    fun notify(event: AbstractEvent) {
        synchronized(lock) {
            val observers = mObserverMap[event.getType()] ?: return@synchronized
            event.fire(observers)
        }
    }

    fun notify(event: AbstractEvent, delay: Long) {
        mHandler.postDelayed({ notify(event) }, delay)
    }

    companion object {
        private var mInstance: EventBus? = null
        private val lock = Object()

        fun getInstace(): EventBus {
            if (mInstance == null) {
                synchronized(lock) {
                    if (mInstance == null) {
                        mInstance =
                            EventBus()
                    }
                }
            }
            return mInstance ?: throw NullPointerException()
        }
    }
}