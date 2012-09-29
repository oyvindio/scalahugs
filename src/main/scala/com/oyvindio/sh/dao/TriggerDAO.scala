package com.oyvindio.sh.dao

import com.novus.salat.dao.SalatDAO
import org.bson.types.ObjectId
import com.oyvindio.sh.events.{Action, Trigger}
import com.oyvindio.sh.Scalahugs
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._



object TriggerDAO extends SalatDAO[Trigger, ObjectId](
  collection = Scalahugs.db("Triggers"))