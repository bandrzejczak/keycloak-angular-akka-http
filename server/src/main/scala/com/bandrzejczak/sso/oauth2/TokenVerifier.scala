package com.bandrzejczak.sso.oauth2

import scala.util.Try

trait TokenVerifier {
  def verifyToken(token: String): Try[String]
}

