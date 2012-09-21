package com.oyvindio.sh.modules

import akka.actor.ActorPath
import com.oyvindio.sh.events.PrivMsg
import dispatch._
import org.jsoup.nodes.Document
import scala.collection.JavaConversions._
import java.net.URLEncoder.encode

class GoogleSearch(botPath: ActorPath) extends AbstractScalahugsActor(botPath) with HttpSupport {
  protected def receive = {
    case msg: PrivMsg if msg.message.startsWith("!g ") => {
      val tokens = msg.message.split(" ")
      if (tokens.length < 2) {
        privMsg(msg.channel, "Usage: !g SEARCH-TERM")
      }

      val searchTerms = tokens.tail.mkString(" ")
      Http(request("http://www.google.com/search").addQueryParameter("q", searchTerms) OK as.jsoup.Document) onComplete {
        case Right(document) => {
          val results = searchResults(document)
          privMsg(msg.channel,
            if (results.isEmpty) "No results for '%s'".format(searchTerms) else results.head)
        }
        case Left(exception) => log.error(exception, "Caught exception while Googling!")
      }
    }
  }

  def searchResults(soup: Document): List[String]  = {
    soup.select("li.g  h3.r > a.l").map(el => "%s - %s".format(el.attr("abs:href"), el.text())).toList
  }
}