package com.oyvindio.sh.modules


import akka.actor.ActorPath
import com.oyvindio.sh.dao.{TriggerDAO, PrivMsgDAO, ActionDAO}
import com.oyvindio.sh.events.{Trigger, Action, PrivMsg}

class MongoLogger(botPath: ActorPath) extends AbstractScalahugsActor(botPath) {
  protected def receive = {
    case msg: PrivMsg => {
      log.debug("Received PrivMsg: " + msg.toString)
      val id = PrivMsgDAO.insert(msg)
      if (id.isDefined) log.debug("Inserted PrivMsg: %s with id: '%s'".format(msg, id.get))
    }
    case action: Action => {
      log.debug("Received Action: " + action.toString)
      val id = ActionDAO.insert(action)
      if (id.isDefined) log.debug("Inserted Action: %s with id: '%s'".format(action, id.get))
    }
    case trigger: Trigger => {
      log.debug("Received PrivMsg: " + trigger.toString)
      val id = TriggerDAO.insert(trigger)
      if (id.isDefined) log.debug("Inserted Trigger: %s with id: '%s'".format(trigger, id.get))
    }
  }
}