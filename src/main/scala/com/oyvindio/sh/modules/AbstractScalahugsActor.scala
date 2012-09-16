package com.oyvindio.sh.modules

import akka.actor.{ActorPath, Actor, ActorLogging}

abstract class AbstractScalahugsActor(botPath: ActorPath) extends Actor with ActorLogging {
  protected lazy val bot = context.system.actorFor(botPath)
}