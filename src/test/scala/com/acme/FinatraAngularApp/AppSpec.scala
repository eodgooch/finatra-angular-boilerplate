package com.acme.FinatraAngularApp

import com.acme.FinatraAngularApp.controllers.Api.{UsersApi}
import com.twitter.finatra.test._
import com.twitter.finatra.FinatraServer
import com.acme.FinatraAngularApp.controllers.IndexApp
import scala.collection.Map
import scala.util.parsing.json.JSON

class AppSpec extends FlatSpecHelper {

  override val server = new FinatraServer

  server.register(new IndexApp())
  server.register(new UsersApi())

  /**
   * Overall tests
   */

  "GET /not-found" should "respond 404" in {
    get("/not-found", Map(), Map("Accept" -> "application/json"))
    response.body   should equal ("Not Found")
    response.code   should equal (404)
  }

  "POST /api/users/authentication" should "respond 400 with message `No matching accepted Response format could be determined!`" in {
    post(API.getBaseUrl ++ "/users/authentication", Map(), Map("Accept" -> "text/xml"))
    response.body.contains  ("No matching accepted Response format could be determined!") should equal(true)
    response.code   should equal (406)
  }

  "POST /api/users/authentication" should "respond 201" in {
    post(API.getBaseUrl ++ "/users/authentication", Map(), Map("Accept" -> "application/json"))
    response.body.contains ("token") should equal(true)
    response.code   should equal (201)
  }

  "GET /api/users" should "respond 400 with message `No matching accepted Response format could be determined!`" in {
    get(API.getBaseUrl ++ "/users", Map(), Map("Accept" -> "text/xml"))
    response.body.contains  ("No matching accepted Response format could be determined!") should equal(true)
    response.code   should equal (406)
  }

  "GET /api/users" should "respond 200" in {
    get(API.getBaseUrl ++ "/users", Map(), Map("Accept" -> "application/json"))
    response.code   should equal (200)
  }

  "GET /index.html" should "respond 200" in {
    get("/")
    response.body.contains ("Your current user auth token") should equal(true)
    response.code should equal (200)
  }

  def getAuthToken = {
    post(API.getBaseUrl ++ "/users/authentication", Map(), Map("Accept" -> "application/json"))
    response.code   should equal (201)

    type M = Map[String, String]

    JSON.parseFull(response.body) match {
      case Some(map: M) => map ++ Map("Accept" -> "application/json")
      case _ => Map("" -> "")
    }
  }
}
