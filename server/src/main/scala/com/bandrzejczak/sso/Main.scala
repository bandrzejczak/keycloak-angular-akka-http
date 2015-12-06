package com.bandrzejczak.sso

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.bandrzejczak.sso.oauth2.{KeycloakTokenVerifier, OAuth2Authorization, OAuth2Token}
import org.keycloak.adapters.KeycloakDeploymentBuilder
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object Main extends App with Jsonp with JsonProtocol {
  implicit val system = ActorSystem("sso-system")
  implicit val actorMaterializer = ActorMaterializer()
  val oauth2 = new OAuth2Authorization(
    Logging(system, getClass),
    new KeycloakTokenVerifier(
      KeycloakDeploymentBuilder.build(
        getClass.getResourceAsStream("/keycloak.json")
      )
    )
  )
  import oauth2._

  val routes = authorized { token =>
    path("test") {
      get {
        jsonpWithParameter("callback") {
          complete(token)
        }
      }
    }
  }

  Http().bindAndHandle(routes, "localhost", 9000)
}

trait JsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit def OAuth2TokenFormat: RootJsonFormat[OAuth2Token] = jsonFormat2(OAuth2Token)
}
