package com.acme.FinatraAngularApp.models

import com.acme.FinatraAngularApp.DB
import com.acme.FinatraAngularApp.models.database.UserEntity
import com.acme.FinatraAngularApp.models.database.UsersDatabase._

import scala.slick.driver.H2Driver.simple._

object UsersModel {
  def add(token: String) = DB.connection.withSession { implicit session =>
    users += UserEntity(token)
  }

  def getAll(limit: Option[Int]) = DB.connection.withSession { implicit session =>
    users.take(limit.getOrElse(10)).run
  }

  def getUserByToken(token: String) = DB.connection.withSession { implicit session =>
    users.filter(_.token === token).run
  }
}