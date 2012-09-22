package com.oyvindio.sh.modules

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import akka.testkit.{TestKit, TestActorRef}
import akka.actor.ActorSystem
import io.Source


class EpisodeInfoTest extends TestKit(ActorSystem("test")) with FunSuite
  with ShouldMatchers {
  val episodeInfo = TestActorRef(new EpisodeInfo(testActor.path)).underlyingActor
  val quickInfo = Source.fromURL(getClass.getResource("/tvrage-quickinfo.txt"),
    "UTF-8").mkString

  test("parseQuickInfo should return Show Name") {
    val result = episodeInfo.parseQuickInfo(quickInfo)
    result.get("Show Name").get should equal("Breaking Bad")
  }

  test("parseQuickInfo should return Latest Episode") {
      val result = episodeInfo.parseQuickInfo(quickInfo)

      result.get("Latest Episode").get should equal(
        "05x08, Gliding Over All, Sep/02/2012")
    }

  test("parseQuickInfo should return Next Episode") {
      val result = episodeInfo.parseQuickInfo(quickInfo)

      result.get("Next Episode").get should equal("05x09, TBA, Jul/2013")
    }
}