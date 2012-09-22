package com.oyvindio.sh.events


case class Trigger(channel: String, nick: String, login: String,
                   hostname: String, message: String) extends IrcEvent {
  require(message.startsWith(Trigger.trigger),
    "Trigger.message must start with %s".format(Trigger.trigger))

  def trigger = tokens.head
  def args = tokens.tail
  def allArgs = tokens.tail.mkString(" ")
  def hasArgs = !tokens.tail.isEmpty
  def tokens = message.substring(1).split(" ").toList
}

object Trigger {
  val trigger = "!"
}