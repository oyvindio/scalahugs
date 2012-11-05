package com.oyvindio.sh.modules

import akka.actor.ActorPath
import com.oyvindio.sh.events.{Action, PrivMsg}
import dispatch._
import org.jsoup.nodes.Document


class Title(botPath: ActorPath) extends AbstractScalahugsActor(botPath) with HttpSupport {
  protected def receive = {
    case msg: PrivMsg => titleMsgForUrls(msg.channel, msg.message)
    case action: Action => titleMsgForUrls(action.channel, action.action)
  }

  private def titleMsgForUrls(channel: String, message: String) {
    findURLs(message).map(titleMsgForUrl(channel, _))
  }

  private def findURLs(corpus: String): List[String] = {
    corpus.split(" ").filter(_.matches( """^http(s)?://[^ ]+""")).toList
  }

  private def titleMsgForUrl(channel: String, url: String) {
    Http(request(url) OK as.jsoup.Document) onComplete {
      case Right(document) => {
        extractTitle(document) match {
          case Some(title: String) => privMsg(channel, "Title: %s".format(title))
          case None => log.info("Couldn't find title for %s".format(url))
        }
      }
      case Left(exception) => log.error(exception,
          "Caught exception while looking up <title> for %s!".format(url))

    }
  }

  private def extractTitle(soup: Document): Option[String] = {
    val elements = soup.select("html > head > title")
    if (elements.isEmpty) None else Some(elements.first.text)
  }
}