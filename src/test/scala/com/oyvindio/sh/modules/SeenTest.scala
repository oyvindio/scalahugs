package com.oyvindio.sh.modules

import org.scalatest.{PrivateMethodTester, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import com.oyvindio.sh.events.{Action, PrivMsg, IrcEvent}
import org.joda.time.{DateTimeZone, DateTime}
import akka.actor.{ActorSystem, ActorPath}
import akka.testkit.TestActorRef
import akka.testkit.TestKit

class SeenTest extends TestKit(ActorSystem("test")) with FunSuite
  with ShouldMatchers with PrivateMethodTester {
  val seen = TestActorRef(new Seen(testActor.path)).underlyingActor
  val mostRecentEvent = PrivateMethod[Option[IrcEvent]]('mostRecentEvent)

  test("mostRecentEvent should return the most recent event") {
    val events = List(
      PrivMsg("#foo", "bar", "baz", "quez.tld", "123"),
      Action("#foo", "bar", "baz", "quez.tld", "test"),
      PrivMsg("#foo", "bar", "baz", "quez.tld", "message")
    )

    seen invokePrivate mostRecentEvent(events) should equal(Some(events.last))
  }

  test("mostRecentEvent should return None for empty input") {
    seen invokePrivate mostRecentEvent(List()) should equal(None)
  }
}