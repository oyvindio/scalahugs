package com.oyvindio.sh.events

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers


class TriggerTest extends FunSuite with ShouldMatchers {

  test("trigger must start with %s".format(Trigger.trigger)) {
    intercept[IllegalArgumentException] {
      Trigger("#test", "foo", "bar", "localhost", "invalid trigger!")
    }
  }

  test("trigger.trigger should return trigger keyword") {
    val trigger = Trigger("#test", "foo", "bar", "localhost",
      "!test param1 param2 param3")

    trigger.trigger should equal("test")
  }

  test("trigger.args should return list of args") {
    val trigger = Trigger("#test", "foo", "bar", "localhost",
      "!test param1 param2 param3")

    trigger.args should equal(List("param1", "param2", "param3"))
  }

  test("trigger.tokens should return message split by ' '") {
    val trigger = Trigger("#test", "foo", "bar", "localhost",
      "!test param1 param2 param3")

    trigger.tokens should equal(List("test", "param1", "param2", "param3"))
  }

  test("trigger should accept trigger without arguments") {
    val trigger = Trigger("#test", "foo", "bar", "localhost",
          "!test")

    trigger.trigger should equal("test")
    trigger.args should equal(List())
    trigger.tokens should equal(List("test"))
  }
}