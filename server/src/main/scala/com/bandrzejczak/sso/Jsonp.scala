package com.bandrzejczak.sso

import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.{ContentType, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.util.ByteString

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
