package org.wit.eventtime.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventModel(var id: Long = 0,
                      var title: String = "",
                      var description: String = "",
                      var image: Uri = Uri.EMPTY,
                      var startMinute: Int = -1,
                      var startHour: Int = -1,
                      var startDay: Int = -1,
                      var startMonth: Int = -1,
                      var startYear: Int = -1,
                      var endMinute : Int = -1,
                      var endHour: Int = -1,
                      var endDay: Int = -1,
                      var endMonth: Int = -1,
                      var endYear: Int = -1) : Parcelable