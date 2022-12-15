package org.wit.eventtime.models

interface EventStore {
    fun findAll(): List<EventModel>
    fun create(event: EventModel)
    fun update(event: EventModel)
}