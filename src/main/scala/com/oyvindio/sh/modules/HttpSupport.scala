package com.oyvindio.sh.modules

import dispatch.url

trait HttpSupport {
  def request(uri: String) = url(uri).setHeader("User-Agent",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:12.0) Gecko/20100101 Firefox/12.0")
}