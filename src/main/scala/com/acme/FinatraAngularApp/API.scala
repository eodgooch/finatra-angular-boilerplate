package com.acme.FinatraAngularApp

import com.typesafe.config._

object API {
  val config = ConfigFactory.load()
  val version = config.getString("api.version")
  lazy val baseUrl = s"/api/v$version"

  def getBaseUrl = baseUrl
}
