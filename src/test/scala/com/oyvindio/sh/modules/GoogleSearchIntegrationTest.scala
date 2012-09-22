package com.oyvindio.sh.modules

import org.scalatest.{Tag, BeforeAndAfterAll, FunSuite}
import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import akka.actor.{Props, ActorSystem}
import org.scalatest.matchers.ShouldMatchers
import com.oyvindio.sh.events.{Trigger, PrivMsg}
import com.oyvindio.sh.events.bot.BotMsg
import java.net.{MalformedURLException, URI}
import com.oyvindio.sh.Tags
import akka.util.duration._

class GoogleSearchIntegrationTest extends TestKit(ActorSystem("test"))
  with FunSuite with ShouldMatchers with ImplicitSender with BeforeAndAfterAll
  with Tags {
  val google = system.actorOf(Props(new GoogleSearch(testActor.path)))

  test("GoogleSearch should return one result with a valid URI", Integration) {
    google ! Trigger("#test", "tester", "tester", "localhost", "!g test")
    expectMsgPF() {
      // should throw exception and fail the test if we don't get a good URI
      case m: BotMsg => {
        val split = m.message.split('-')
        new URI(split(0).trim) // m.message should be "uri — title"
      }
      case _ => fail("Received wrong message type!")
    }
  }

  override protected def afterAll() {
    super.afterAll()
    system.shutdown()
  }
}