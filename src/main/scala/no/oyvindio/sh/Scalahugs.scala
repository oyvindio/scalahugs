package no.oyvindio.sh

import com.weiglewilczek.slf4s.Logging
import java.io.IOException
import org.jibble.pircbot.{NickAlreadyInUseException, IrcException, PircBot}
import java.lang.String

class Scalahugs extends PircBot with Logging {
  private val nicks = Seq("sh", "sh_", "scalahugs")

  private def doConnect(host: String, port: Int): Boolean = {
    logger.info("Connecting to %s:%d".format(host, port))
    for (nick <- nicks) {
      setName(nick)
      logger.info("Trying nick '%s'".format(nick))
      try {
        connect(host, port)
        return true
      } catch {
        case naiue: NickAlreadyInUseException => logger.error("Nick %s already in use".format(getName))
      }
    }
    false
  }

  def connect() {
    val host = "localhost" // TODO: need some sort of configuration
    val port = 6667
    try {
      val success = doConnect(host, port)
      if (!success) {
        logger.error("Unable to connect to %s:%d, none of the configured nicks were available. Tried: %s".format(host, port, nicks))
      }
    } catch {
      case ie: IrcException => logger.error("Unable to join server", ie)
      case ioe: IOException => logger.error("Unable to connect to server", ioe)
    }
  }

  override def onMessage(channel: String, sender: String, login: String, hostname: String, message: String) {
    sendMessage(channel, message)
  }
}

object Scalahugs extends App {
  override def main(args: Array[String]) {
    val bot = new Scalahugs()
    bot.connect()
    bot.joinChannel("#grouphugs")
  }
}