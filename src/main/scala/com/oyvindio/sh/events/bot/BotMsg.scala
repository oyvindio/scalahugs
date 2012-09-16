package com.oyvindio.sh.events.bot

import org.joda.time.{DateTimeZone, DateTime}

case class BotMsg(channel: String, message: String, timestamp: DateTime = new DateTime(DateTimeZone.UTC))