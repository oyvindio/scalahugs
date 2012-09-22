package com.oyvindio.sh

import akka.actor.{Props, ActorSystem}
import modules.{EpisodeInfo, GoogleSearch, Seen, MongoLogger}
import com.oyvindio.sh.events._
import akka.event.EventStream

object Main extends App {
  val system = ActorSystem("scalahugs")
  val bot = new Scalahugs(system)
  val events = system.eventStream
  val botActor = system.actorOf(Props(new BotActor(bot)), "bot")
  val mongoLogger = system.actorOf(Props(new MongoLogger(botActor.path)), "mongo-logger")

  events.subscribe(mongoLogger, classOf[PrivMsg])
  events.subscribe(mongoLogger, classOf[Action])
  events.subscribe(mongoLogger, classOf[Trigger])
  events.subscribe(system.actorOf(Props(new Seen(botActor.path)), "seen"), classOf[Trigger])
  events.subscribe(system.actorOf(Props(new GoogleSearch(botActor.path)), "google-search"), classOf[Trigger])
  events.subscribe(system.actorOf(Props(new EpisodeInfo(botActor.path)), "episode-info"), classOf[Trigger])

  bot.start()
}