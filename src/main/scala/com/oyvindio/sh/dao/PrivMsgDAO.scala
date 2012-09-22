package com.oyvindio.sh.dao

import com.novus.salat.dao.SalatDAO
import org.bson.types.ObjectId
import com.oyvindio.sh.Scalahugs
import com.mongodb.casbah.commons.MongoDBObject
import com.oyvindio.sh.events.PrivMsg
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._


object PrivMsgDAO extends SalatDAO[PrivMsg, ObjectId](collection =
  Scalahugs.db(PrivMsg.toString)) {
  def findMostRecentPrivMsgFor(nick: String): Option[PrivMsg] = {
    val results = find(MongoDBObject("nick" -> nick))
                    .sort(MongoDBObject("timestamp" -> -1))
                    .limit(1)
                    .toList

    if (results.isEmpty) {
      None
    } else {
      Some(results(0))
    }
  }
}