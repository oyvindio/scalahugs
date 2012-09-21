package com.oyvindio.sh.modules

import org.scalatest.{PrivateMethodTester, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import akka.testkit.{TestActorRef, TestKit}
import akka.actor.{ActorPath, ActorSystem}
import io.Source
import org.jsoup.Jsoup
import java.net.URL

class GoogleSearchTest extends TestKit(ActorSystem("test")) with FunSuite with ShouldMatchers with PrivateMethodTester {
  val google = TestActorRef(new GoogleSearch(ActorPath.fromString("akka://test"))).underlyingActor
  val googleSoup = Jsoup.parse(Source.fromURL(getClass.getResource("/google-search.html"), "UTF-8").mkString)

  test("searchResult should return 10 Elements") {
    google.searchResults(googleSoup) should have length(10)
  }

  test("searchResult should return 'URL - title'") {
    val results = google.searchResults(googleSoup)
    results(0) should equal("http://www.speedtest.net/ - Speedtest.net - The Global Broadband Speed Test")
    results(1) should equal("http://en.wikipedia.org/wiki/Test_cricket - Test cricket - Wikipedia, the free encyclopedia")
    results(2) should equal("http://en.wikipedia.org/wiki/Test - Test - Wikipedia, the free encyclopedia")
    results(3) should equal("http://test-ipv6.com/ - Test your IPv6.")
    results(4) should equal("http://www.humanmetrics.com/cgi-win/jtypes2.asp - Personality test based on Jung and Briggs Myers typology")
    results(5) should equal("http://www.speakeasy.net/speedtest/ - Speakeasy Speed Test")
    results(6) should equal("http://html5test.com/ - The HTML5 test - How well does your browser support HTML5?")
    results(7) should equal("http://www.adobe.com/shockwave/welcome/ - Adobe - Test Adobe Shockwave Player")
    results(8) should equal("http://acid3.acidtests.org/ - The Acid3 Test")
    results(9) should equal("http://test.org.uk/ - TEST")
  }
}