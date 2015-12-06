package com.bandrzejczak.sso.oauth2

import scala.concurrent.Future

trait TokenVerifier {
  def verifyToken(token: String): Future[String]
}

