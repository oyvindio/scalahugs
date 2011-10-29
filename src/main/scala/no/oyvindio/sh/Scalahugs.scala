package no.oyvindio.sh

import handlers.Echo
import java.io.IOException
import org.jibble.pircbot.{NickAlreadyInUseException, IrcException, PircBot}
import java.lang.String
import actors.Actor
import org.apache.log4j.BasicConfigurator

case class IRCMessage(channel: String,  message: String) {
  def hasTrigger = message.startsWith("!")
  def trigger = message.split(" ").head
  def args = message.substring(message.indexOf(" "))
}

class Scalahugs extends PircBot with Actor with Logging {
  private val nicks = Seq("sh", "sh_", "scalahugs")

  val echo = new Echo(this)
  echo.start()

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
      if (success) {
        logger.info("Connected as %s".format(getName))
      } else {
        logger.error("Unable to connect to %s:%d, none of the configured nicks were available. Tried: %s".format(host, port, nicks))
      }

    } catch {
      case ie: IrcException => logger.error("Unable to join server", ie)
      case ioe: IOException => logger.error("Unable to connect to server", ioe)
    }
  }

  override def onMessage(channel: String, sender: String, login: String, hostname: String, message: String) {
    logger.debug("[%s] %s: %s".format(channel, sender, message))
    val msg = IRCMessage(channel, message)
    echo ! msg
  }

  def act() {
    loop {
      react {
        case msg: IRCMessage => {
          sendMessage(msg.channel, msg.message)
        }
      }
    }
  }
}

object Scalahugs extends App {
  override def main(args: Array[String]) {
    BasicConfigurator.configure()
    val bot = new Scalahugs()
    bot.connect()
    bot.start()
    bot.joinChannel("#grouphugs")
  }
}