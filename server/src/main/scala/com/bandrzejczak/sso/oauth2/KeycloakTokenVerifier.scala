package com.bandrzejczak.sso.oauth2

import org.keycloak.RSATokenVerifier
import org.keycloak.adapters.KeycloakDeployment

import scala.util.Try

class KeycloakTokenVerifier(keycloakDeployment: KeycloakDeployment) extends TokenVerifier {
  def verifyToken(token: String): Try[String] = {
    Try {
      RSATokenVerifier.verifyToken(
        token,
        keycloakDeployment.getRealmKey,
        keycloakDeployment.getRealmInfoUrl
      ).getPreferredUsername
    }
  }
}
