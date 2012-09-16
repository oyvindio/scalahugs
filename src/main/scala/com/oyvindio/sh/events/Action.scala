package com.oyvindio.sh.events

import org.joda.time.{DateTimeZone, DateTime}
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoConnection
import org.bson.types.ObjectId
import com.oyvindio.sh.Scalahugs
import com.mongodb.casbah.commons.MongoDBObject

case class Action(channel: String, nick: String,
                  login: String, hostname: String, action: String,
                  timestamp: DateTime = new DateTime(DateTimeZone.UTC),
                  @Key("_id") id: ObjectId = new ObjectId) extends IrcEvent(timestamp)

object ActionDAO extends SalatDAO[Action, ObjectId](collection = Scalahugs.db(Action.toString)) {
  def findMostRecentAction(nick: String): Option[Action] = {
    val results = find(MongoDBObject("nick" -> nick)).sort(MongoDBObject("timestamp" -> -1)).limit(1).toList
    if (results.isEmpty) {
      None
    } else {
      Some(results(0))
    }
  }
}
