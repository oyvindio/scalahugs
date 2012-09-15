package com.oyvindio.sh

import org.jibble.pircbot.{NickAlreadyInUseException, PircBot}
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._
import akka.actor._
import org.slf4j.LoggerFactory
import org.joda.time.{DateTimeZone, DateTime}
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._


class Scalahugs(actorSystem: ActorSystem) extends PircBot {
  private val config = ConfigFactory.load("application.conf")
  private val log = LoggerFactory.getLogger(this.getClass)
  private val system = actorSystem

  // Lifecycle
 def start(): Boolean = {
    log.debug("Starting bot")
    connect()
    if (isConnected) {
      joinChannels()
    }

    isConnected
  }

  private def getServers = {
    val servers = Scalahugs.config.getObject("sh.servers")
    servers.keySet.map {key => servers.toConfig.getConfig(key)}
  }

  def connect(): Boolean = {
    for (server <- getServers) {
      for (nick <- config.getStringList("sh.nicks")) {
        setName(nick)
        try {
          val host = server.getString("host")
          val port = server.getInt("port")
          log.debug("Connecting to %s:%d".format(host, port))
          if (server.hasPath("password")) {
            connect(host, port, server.getString("password"))
          } else {
            connect(host, port)
          }
        } catch {
          case e: NickAlreadyInUseException => log.warn("Nick '%s' is already in use!".format(nick))
          case e: Throwable => log.error("Caught fatal exception while connecting!", e)
        } finally {
          if (isConnected) return isConnected
        }
      }
    }

    isConnected
  }

  def joinChannels() {
    joinChannel("#scalahugs")
  }

  // Events
  override def onMessage(channel: String, sender: String, login: String, hostname: String, message: String) {
    val msg = PrivMsg(new DateTime(DateTimeZone.UTC), channel, sender, login, hostname, message)
    system.eventStream.publish(msg)
    log.debug(msg.toString)

  }

  def sendMessage(msg: PrivMsg) {
    sendMessage(msg.channel, msg.message)
  }
}

object Scalahugs {
  lazy val config = ConfigFactory.load("application.conf")
  RegisterJodaTimeConversionHelpers()
  lazy val db = MongoConnection()("scalahugs")
}

class BotActor(bot: Scalahugs) extends Actor with ActorLogging {
  protected def receive = {
    case msg: PrivMsg => bot.sendMessage(msg)
    case _ => log.warning("got unexpected message")
  }
}
