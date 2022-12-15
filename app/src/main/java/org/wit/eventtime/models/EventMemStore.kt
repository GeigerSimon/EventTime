package org.wit.eventtime.models

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class EventMemStore: EventStore {
    val events = ArrayList<EventModel>()

    override fun findAll(): List<EventModel> {
        return events
    }

    override fun create(event: EventModel) {
        event.id = getId()
        events.add(event)
    }

    override fun update(event: EventModel) {
        var foundEvent: EventModel? = events.find { p -> p.id == event.id }
        if (foundEvent != null) {
            foundEvent.title = event.title
            foundEvent.description = event.description
            foundEvent.image = event.image
            foundEvent.startMinute = event.startMinute
            foundEvent.startHour = event.startHour
            foundEvent.startDay = event.startDay
            foundEvent.startMonth = event.startMonth
            foundEvent.startYear = event.startYear
            foundEvent.endMinute = event.endMinute
            foundEvent.endHour = event.endHour
            foundEvent.endDay = event.endDay
            foundEvent.endMonth = event.endMonth
            foundEvent.endYear = event.endYear
        }
    }
}