package no.oyvindio.sh

import org.apache.log4j.{BasicConfigurator, Level, Logger}

trait Logging {
  val logger = Logger.getLogger(this.getClass)
  logger.setLevel(Level.DEBUG)
}