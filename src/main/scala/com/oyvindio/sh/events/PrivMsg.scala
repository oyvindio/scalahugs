package com.oyvindio.sh.events


case class PrivMsg(channel: String, nick: String,
                   login: String, hostname: String,message: String) extends IrcEvent


