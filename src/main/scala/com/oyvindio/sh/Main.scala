package com.oyvindio.sh

import akka.actor.{Props, ActorSystem}
import com.oyvindio.sh.modules.{Seen, MongoLogger}
import com.oyvindio.sh.events._

object Main extends App {
  val system = ActorSystem("scalahugs")
  val bot = new Scalahugs(system)
  val botActor = system.actorOf(Props(new BotActor(bot)), "bot")

  val mongoLogger = system.actorOf(Props(new MongoLogger(botActor.path)), "mongoLogger")
  system.eventStream.subscribe(mongoLogger, classOf[PrivMsg])
  system.eventStream.subscribe(mongoLogger, classOf[Action])
  system.eventStream.subscribe(system.actorOf(Props(new Seen(botActor.path)), "seen"), classOf[PrivMsg])

  bot.start()
}