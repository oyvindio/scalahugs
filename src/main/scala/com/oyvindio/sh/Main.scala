package com.oyvindio.sh

import akka.actor.{Props, ActorSystem}
import com.oyvindio.sh.modules.MongoLogger

object Main extends App {
  val system = ActorSystem("scalahugs")
  val bot = new Scalahugs(system)
  val botActor = system.actorOf(Props(new BotActor(bot)), "bot")

  system.eventStream.subscribe(system.actorOf(Props(new MongoLogger(botActor.path)), "mongoLogger"), classOf[PrivMsg])

  bot.start()
}