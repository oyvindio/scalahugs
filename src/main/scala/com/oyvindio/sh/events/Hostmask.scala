package com.oyvindio.sh.events

trait Hostmask {
  val nick: String
  val login: String
  val hostname: String
    /**
     * @param hostmaskExpr glob that matches a hostmask. Allowed wildcards:
     *             * (matches any n characters)
     *             ? (matches any one character)
     */
    def matches(hostmaskExpr: String): Boolean = {
      hostmask.matches(hostmaskExpr.replaceAllLiterally("*", ".+") .replaceAllLiterally("?", "."))
    }

    def hostmask: String = {
      nick + "!" + login + "@" + hostname
    }
  }