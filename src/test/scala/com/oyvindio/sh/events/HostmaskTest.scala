package com.oyvindio.sh.events

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

case class HostmaskTester(nick: String, login: String,
                          hostname: String) extends Hostmask

class HostmaskTest extends FunSuite {
  val hostmask = HostmaskTester("foo", "bar", "baz")

  test("hostmask should match exact hostmask") {
    assert(hostmask.matches("foo!bar@baz"))
  }

  test("hostmask should match ? wildcard in nick") {
    assert(hostmask.matches("fo?!bar@baz"))
  }

  test("hostmask should match ? wildcard in login") {
    assert(hostmask.matches("foo!b?r@baz"))
  }

  test("hostmask should match ? wildcard in hostname") {
    assert(hostmask.matches("foo!bar@?az"))
  }

  test("hostmask should match * wildcard as nick") {
    assert(hostmask.matches("*!bar@baz"))
  }

  test("hostmask should match * wildcard as login") {
    assert(hostmask.matches("foo!*@baz"))
  }

  test("hostmask should match * wildcard as hostname") {
    assert(hostmask.matches("foo!bar@*"))
  }

  test("hostmask should match any matcher (*!*@*)") {
    assert(hostmask.matches("*!*@*"))
  }
}