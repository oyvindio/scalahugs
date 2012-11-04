package com.oyvindio.sh

import akka.actor.{ActorRef, Props, ActorSystem}
import events.Action
import events.PrivMsg
import modules._
import com.oyvindio.sh.events._
import akka.event.EventStream
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers

object Main extends App {
  RegisterJodaTimeConversionHelpers()

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
  val op = system.actorOf(Props(new Opers(botActor.path)), "opers")
  events.subscribe(op, classOf[PrivMsg])
  events.subscribe(op, classOf[Action])
  val title = system.actorOf(Props(new Title(botActor.path)), "title")
  events.subscribe(title, classOf[PrivMsg])
  events.subscribe(title, classOf[Action])

  bot.start()
}