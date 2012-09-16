package com.oyvindio.sh.events

import org.joda.time.{DateTimeZone, DateTime}
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoConnection
import com.oyvindio.sh.Scalahugs
import org.bson.types.ObjectId
import com.mongodb.casbah.commons.MongoDBObject

case class PrivMsg(channel: String, nick: String,
                   login: String, hostname: String,message: String,
                   timestamp: DateTime = new DateTime(DateTimeZone.UTC),
                   @Key("_id") id: ObjectId = new ObjectId) extends IrcEvent(timestamp)

object PrivMsgDAO extends SalatDAO[PrivMsg, ObjectId](collection = Scalahugs.db(PrivMsg.toString)) {
  def findMostRecentPrivMsg(nick: String): Option[PrivMsg] = {
    val results = find(MongoDBObject("nick" -> nick)).sort(MongoDBObject("timestamp" -> -1)).limit(1).toList
    if (results.isEmpty) {
      None
    } else {
      Some(results(0))
    }
  }
}
