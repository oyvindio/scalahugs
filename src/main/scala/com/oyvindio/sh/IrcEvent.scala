package com.oyvindio.sh


trait IrcEvent
case class PrivMsg(channel: String, sender: String, login: String, hostname: String, message: String)