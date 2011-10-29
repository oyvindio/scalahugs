package no.oyvindio.sh.handlers

import actors.Actor
import no.oyvindio.sh.{Scalahugs, IRCMessage, Logging}

class Echo(bot: Scalahugs) extends Actor with Logging {
  def act() {
    loop {
      react {
        case msg: IRCMessage  => {
          logger.debug("channel: %s, message: %s".format(msg.channel, msg.message))
          bot ! IRCMessage(msg.channel, msg.message)
        }
      }
    }
  }
}