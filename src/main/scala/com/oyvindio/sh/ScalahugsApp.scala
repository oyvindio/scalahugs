package com.oyvindio.sh

import modules.{MongoLogger, Echo}
import org.jibble.pircbot.{NickAlreadyInUseException, PircBot}
import com.typesafe.config.{ConfigException, ConfigObject, Config, ConfigFactory}
import scala.collection.JavaConversions._
import akka.actor._
import akka.event.{EventStream, ActorEventBus, Logging}
import org.slf4j.LoggerFactory


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
    val servers = config.getObject("sh.servers")
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
    val msg = PrivMsg(channel, sender, login, hostname, message)
    system.eventStream.publish(msg)
    log.debug(msg.toString)

  }

  def sendMessage(msg: PrivMsg) {
    sendMessage(msg.channel, msg.message)
  }
}

class ScalahugsActor(bot: Scalahugs) extends Actor with ActorLogging {
  protected def receive = {
    case msg: PrivMsg => bot.sendMessage(msg)
    case _ => log.warning("got unexpected message")
  }
}

object ScalahugsApp extends App {
  val system = ActorSystem("scalahugs")
  val bot = new Scalahugs(system)
  val botActor = system.actorOf(Props(new ScalahugsActor(bot)), "bot")
  system.eventStream.subscribe(system.actorOf(Props(new Echo(botActor.path)), "echo"), classOf[PrivMsg])
  system.eventStream.subscribe(system.actorOf(Props(new MongoLogger(botActor.path)), "mongoLogger"), classOf[PrivMsg])

  bot.start()
}