package com.oyvindio.sh.modules

import org.scalatest.{PrivateMethodTester, FunSuite}
import akka.testkit.{TestActorRef, TestKit}
import akka.actor.ActorSystem
import org.scalatest.matchers.ShouldMatchers
import java.net.URI


class TitleTest extends TestKit(ActorSystem("test")) with FunSuite
with ShouldMatchers with PrivateMethodTester {
  val title = TestActorRef(new Title(testActor.path)).underlyingActor
  val findURLs = PrivateMethod[List[String]]('findURLs)

  test("findURLs should find one http URL") {
    val urls = title invokePrivate findURLs("http://google.com/search?q=foo")
    urls should have length (1)
    urls(0) should be("http://google.com/search?q=foo")
  }

  test("findURLs should find one https URL") {
    val urls = title invokePrivate findURLs("https://google.com/search?q=foo")
    urls should have length (1)
    urls(0) should be("https://google.com/search?q=foo")
  }

  test("findURLs should find multiple http and https URLs") {
    val urls = title invokePrivate findURLs(
      "a bunch of urls http://google.com/search?q=foo with https://google.com/search?q=foo random text https://github.com/oyvindio/scalahugs/blob/master/src/main/scala/com/oyvindio/sh/Scalahugs.scala inbetween")
    urls should have length (3)
    urls(0) should be("http://google.com/search?q=foo")
    urls(1) should be("https://google.com/search?q=foo")
    urls(2) should be("https://github.com/oyvindio/scalahugs/blob/master/src/main/scala/com/oyvindio/sh/Scalahugs.scala")
  }

  test("findURLs should find url with just a bunch of spaces around it URL") {
      val urls = title invokePrivate findURLs("                          https://google.com/search?q=foo             ")
      urls should have length (1)
      urls(0) should be("https://google.com/search?q=foo")
    }
}