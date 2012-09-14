package com.oyvindio.sh.modules

import akka.actor.{ActorPath, Actor, ActorLogging}

abstract class ScalahugsActor(botPath: ActorPath) extends Actor with ActorLogging {
  protected lazy val BOT = context.system.actorFor(botPath)
}