package com.oyvindio.sh.modules

import akka.actor.ActorPath
import com.oyvindio.sh.events.Trigger
import dispatch._
import com.oyvindio.sh.exception.InvalidResponseException
import org.jibble.pircbot.{Colors, PircBot}


class EpisodeInfo(botPath: ActorPath) extends AbstractScalahugsActor(botPath)
with HttpSupport {

  protected def receive = {
    case msg: Trigger if !msg.hasArgs => privMsg(msg.channel, "Usage: !ep SHOW")
    case msg: Trigger if msg.hasArgs && msg.trigger == "ep" => {
      Http(request("http://services.tvrage.com/tools/quickinfo.php")
        .addQueryParameter("show", msg.allArgs) OK as.String) onComplete {
        case Right(response) => {
          val info = parseQuickInfo(response)
          val showName = info.getOrElse("Show Name",
            throw InvalidResponseException("Show Name was missing!"))
          val latestEpisode = info.getOrElse("Latest Episode",
            throw InvalidResponseException("'Latest Episode' was missing!"))
          val nextEpisode = info.get("Next Episode")
          val ended = info.get("Ended")

          if (nextEpisode.isEmpty && ended.isEmpty)
            throw InvalidResponseException("'Next Episode' and 'Ended' was missing!")

          val nextOrEnded = if (nextEpisode.isDefined) {
            Colors.BOLD + "Next Episode: " + Colors.NORMAL + nextEpisode.get
          } else {
            Colors.BOLD + "Ended: " + Colors.NORMAL + ended.get
          }

          val reply = Colors.BOLD + "Show Name: "  + Colors.NORMAL + showName + " " +
            Colors.BOLD + "Latest Episode: "  + Colors.NORMAL + latestEpisode + " " +
            nextOrEnded

          privMsg(msg.channel, reply)
        }
        case Left(exception) => log.error(exception,
          "Caught exception while looking up show: '%s'".format(msg.allArgs))
      }
    }
  }

  private def parseQuickInfo(quickInfo: String): Map[String, String] = {
    quickInfo.split("\n").flatMap(parseQuickInfoLine(_)).toMap
  }

  private def parseQuickInfoLine(line: String): Option[(String, String)] = {
    val parts = line.split("@")
    if (parts.length == 2) {
      Some(parts(0), parts(1).replaceAllLiterally("^", ", "))
    } else {
      None
    }
  }
}