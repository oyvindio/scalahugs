package com.oyvindio.sh.modules

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.oyvindio.sh.Tags
import com.oyvindio.sh.events.Trigger
import com.oyvindio.sh.events.bot.BotMsg
import org.jibble.pircbot.Colors


class EpisodeInfoIntegrationTest extends TestKit(ActorSystem("test"))
with FunSuite with ShouldMatchers with ImplicitSender with BeforeAndAfterAll
  with Tags {
  val episodeInfo = system.actorOf(Props(new EpisodeInfo(testActor.path)))

  test("EpisodeInfo should return a show summary", Integration) {
      episodeInfo ! Trigger("#test", "foo", "bar", "localhost" , "!ep the saint")
      expectMsgPF() {
        case m: BotMsg => {
          Colors.removeFormattingAndColors(m.message) should
            equal("Show Name: The Saint Latest Episode: 06x03, Invitation to " +
              "Danger, Mar/12/1969 Ended: Mar/05/1969")
        }
        case _ => fail("Received wrong message type!")
      }
    }

    override protected def afterAll() {
      super.afterAll()
      system.shutdown()
    }
}