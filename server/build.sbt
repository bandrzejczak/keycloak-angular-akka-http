name := "keycloak-akka-http"

version := "1.0"

scalaVersion := "2.11.7"

val keycloakDependencies = Seq (
  "org.keycloak" % "keycloak-adapter-core" % "1.6.1.Final",
  // we include all necessary transitive dependencies,
  // because they're marked provided in keycloak pom.xml
  "org.keycloak" % "keycloak-core" % "1.6.1.Final",
  "org.jboss.logging" % "jboss-logging" % "3.3.0.Final",
  "org.apache.httpcomponents" % "httpclient" % "4.5.1"
)

libraryDependencies ++= {
  val akkaV = "2.4.1"
  val akkaStreamV = "2.0-M1"
  val scalaTestV = "2.2.5"
  Seq(
    "org.slf4j" % "slf4j-api" % "1.7.12",
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-testkit-experimental" % akkaStreamV,
    "org.scalatest" %% "scalatest" % scalaTestV % "test"

  ) ++ keycloakDependencies
}