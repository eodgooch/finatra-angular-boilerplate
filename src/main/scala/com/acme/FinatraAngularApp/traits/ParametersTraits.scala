package com.acme.FinatraAngularApp.traits

import com.acme.FinatraAngularApp.exceptions.{UnauthorizedException, NotFoundException, BadRequestException}
import com.acme.FinatraAngularApp.models.{UsersModel}
import scala.collection.Map
import scala.util.{Failure, Success}

sealed trait ParametersValidationTrait {
  def tryGetParameter(params: Map[String, String], paramKey: String) = params.get(paramKey) match {
    case Some(param) => Success(param)
    case _ => Failure(BadRequestException("Parameter '" ++ paramKey ++ "' is required!"))
  }
}

trait UsersTrait extends ParametersValidationTrait {
  def tryGetUserId(availableParams: Map[String, String]) = tryGetParameter(availableParams, "token") match {
    case Success(token) => UsersModel getUserByToken token match {
      case x +: xs => Success(x.id.get)
      case _ => Failure(UnauthorizedException())
    }
    case Failure(error) => error match {
      case exception: BadRequestException => Failure(UnauthorizedException())
      case _ => Failure(error)
    }
  }
}