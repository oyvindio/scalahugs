package no.oyvindio.sh.handlers

import actors.Actor
import no.oyvindio.sh.{Scalahugs, IRCMessage, Logging}

class Echo(bot: Actor) extends Actor with Logging {
  def act() {
    loop {
      react {
        case msg: IRCMessage  => {
          log.debug("channel: %s, message: %s".format(msg.channel, msg.message))
          bot ! msg.reply(msg.message)
        }
      }
    }
  }
}