package no.oyvindio.sh

import org.apache.log4j.{BasicConfigurator, Level, Logger}

object Logging {
  var configured = false

  def configure() {
    if (!configured) {
      BasicConfigurator.configure()
      configured = true
    }
  }
}

trait Logging {
  val log = Logger.getLogger(this.getClass)
  log.setLevel(Level.DEBUG)
  Logging.configure()
}