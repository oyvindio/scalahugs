package com.oyvindio.sh.modules


import akka.actor.ActorPath
import com.oyvindio.sh.events.{PrivMsgDAO, ActionDAO, Action, PrivMsg}

class MongoLogger(botPath: ActorPath) extends AbstractScalahugsActor(botPath) {
  protected def receive = {
    case msg: PrivMsg => {
      log.debug("Received PrivMsg: " + msg.toString)
      val id = PrivMsgDAO.insert(msg)
      if (id.isDefined) log.debug("Inserted PrivMsg: " + msg.toString + " with id: ", id.get.toString)
    }
    case action: Action => {
      log.debug("Received Action: " + action.toString)
      val id = ActionDAO.insert(action)
      if (id.isDefined) log.debug("Inserted Action: " + action.toString + " with id: ", id.get.toStringMongod)
    }
  }
}