package com.acme.FinatraAngularApp.controllers.Api

import com.acme.FinatraAngularApp.controllers.ResponseController
import com.acme.FinatraAngularApp.helpers.BearerTokenGeneratorHelper
import com.acme.FinatraAngularApp.API
import com.acme.FinatraAngularApp.models.UsersModel
import com.twitter.util.Future
import scala.util.{Success, Failure, Try}

class UsersApi extends ResponseController {

  /**
   * Get authentication token
   *
   * curl -i -H Accept:application/json -X POST http://localhost:7070/api/v1/users/authentication
   */
  post(s"${API.getBaseUrl}/users/authentication")(checkRequestType(_) { request =>
    (for {
      token <- Try((new BearerTokenGeneratorHelper).generateSHAToken("FinatraAngularApp"))
      addedRows <- Try(UsersModel.add(token))
    } yield Map("token" -> token)) match {
      case Failure(error) => Future.exception(error)
      case Success(response) => renderResponse(request, render, Some(201), Some(response))
    }
  })

  /**
   * Method only for debugging - will return list of users
   *
   * curl -i -X GET -G http://localhost:7070/api/v1/users -d limit={limit.?}
   */
  get(s"${API.getBaseUrl}/users")(checkRequestType(_) { request =>
    (for {
      limit <- Try(request.params.getInt("limit"))
      users <- Try(UsersModel.getAll(limit))
    } yield users) match {
      case Failure(error) => Future.exception(error)
      case Success(users) => renderResponse(request, render, Some(200), Some(users))
    }
  })
}
