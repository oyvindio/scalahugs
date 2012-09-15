package com.oyvindio.sh

import org.joda.time.{DateTimeZone, DateTime}
import com.novus.salat.annotations.raw.Salat
import com.typesafe.config.ConfigFactory

@Salat
abstract class IrcEvent(timestamp: DateTime) {
  def timestampAsLocalTime = timestamp.toDateTime(DateTimeZone.forID(IrcEvent.timezone))
}

object IrcEvent {
  private lazy val timezone = Scalahugs.config.getString("sh.timezone")
}

case class PrivMsg(timestamp: DateTime, channel: String, nick: String, login: String, hostname: String, message: String) extends IrcEvent(timestamp)