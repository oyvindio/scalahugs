package com.oyvindio.sh.events

import org.joda.time.{DateTimeZone, DateTime}
import com.novus.salat.annotations.raw.Salat
import com.oyvindio.sh.Scalahugs

@Salat
abstract class IrcEvent(timestamp: DateTime) {
  def timestampAsLocalTime = timestamp.toDateTime(DateTimeZone.forID(IrcEvent.timezone))
  def timestampAsUTC = timestamp.toDateTime(DateTimeZone.UTC)
}

object IrcEvent {
  private lazy val timezone = Scalahugs.config.getString("sh.timezone")
}
