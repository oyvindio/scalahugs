package no.oyvindio.sh.handlers

import actors.Actor
import no.oyvindio.sh.{Reply, IRCJoin, Scalahugs, Logging}


class JoinNotifier(bot: Scalahugs) extends Actor with Logging {
  def act() {
    loop {
      react {
        case join: IRCJoin => {
          bot ! join.reply (join.channel, "%s joined %s".format(join.nick, join.channel))
        }
      }
    }
  }
}

