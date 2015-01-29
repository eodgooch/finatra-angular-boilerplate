package com.acme.FinatraAngularApp

import com.acme.FinatraAngularApp.controllers.IndexApp
import com.acme.FinatraAngularApp.controllers.Api.{UsersApi}
import com.twitter.finatra._

object App extends FinatraServer {
  register(new IndexApp)
  register(new UsersApi)
  /* Register new APIs here*/
}
