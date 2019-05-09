name := "zioErrs"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "org.scalaz" %% "scalaz-zio" % "1.0-RC4"


libraryDependencies += scalaOrganization.value % "scala-compiler" % scalaVersion.value
libraryDependencies += scalaOrganization.value % "scala-reflect" % scalaVersion.value