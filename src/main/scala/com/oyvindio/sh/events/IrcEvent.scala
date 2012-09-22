package com.oyvindio.sh.events

import org.joda.time.{DateTimeZone, DateTime}
import com.novus.salat.annotations.raw.Salat
import com.oyvindio.sh.Scalahugs
import com.novus.salat.annotations._
import org.bson.types.ObjectId

@Salat
abstract class IrcEvent(timestamp: DateTime = new DateTime(DateTimeZone.UTC),
                        @Key("_id") id: ObjectId = new ObjectId) {
  def timestampAsLocalTime = timestamp.toDateTime(DateTimeZone.forID(IrcEvent.timezone))
  def timestampAsUTC = timestamp.toDateTime(DateTimeZone.UTC)
}

object IrcEvent {
  private lazy val timezone = Scalahugs.config.getString("sh.timezone")
}
