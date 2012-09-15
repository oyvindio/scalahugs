package com.oyvindio.sh.modules

import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._



import akka.actor.ActorPath
import com.oyvindio.sh.{Scalahugs, PrivMsg}

class MongoLogger(botPath: ActorPath) extends ScalahugsActor(botPath) {
  private lazy val lines = Scalahugs.db("lines") // TODO: config setting for mongo host/port (default: localhost, 27017)
  protected def receive = {
    case msg: PrivMsg => {
      log.debug("Received PrivMsg: " + msg.toString)
      lines += grater[PrivMsg].asDBObject(msg)
    }
  }
}