package com.bandrzejczak.sso

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.{ContentType, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.bandrzejczak.sso.oauth2.{KeycloakTokenVerifier, OAuth2Authorization, OAuth2Token}
import org.keycloak.adapters.KeycloakDeploymentBuilder
import spray.json.{RootJsonFormat, DefaultJsonProtocol}

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

trait Jsonp {
  def jsonpWithParameter(parameterName: String) =
    parameter(parameterName.?).flatMap {
      case Some(wrapper) =>
        mapResponseEntity {
          case HttpEntity.Strict(ct @ ContentType(`application/json`, _), data) =>
            HttpEntity.Strict(
              ct.withMediaType(`application/javascript`),
              ByteString(wrapper + "(") ++ data ++ ByteString(")")
            )
          case entity => entity
        }
      case None => pass
    }
}

trait JsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit def OAuth2TokenFormat: RootJsonFormat[OAuth2Token] = jsonFormat2(OAuth2Token)
}
