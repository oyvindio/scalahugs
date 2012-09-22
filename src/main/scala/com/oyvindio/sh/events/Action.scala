package com.oyvindio.sh.events


case class Action(channel: String, nick: String,login: String, hostname: String,
                  action: String) extends IrcEvent


