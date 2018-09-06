name := "naiad"
version := "1.0"
scalaVersion := "2.12.6"

scalacOptions += "-Ypartial-unification"

resolvers ++= Seq(
 Resolver.sonatypeRepo("releases")
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")

addSbtPlugin("org.lyranthe.sbt" % "partial-unification" % "1.1.2")

scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.3.0",
  "org.typelevel" %% "cats-free" % "1.3.0",
  "org.typelevel" %% "cats-effect" % "0.10.1",
  "org.typelevel" %% "cats-effect-laws" % "0.10.1" % Test,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
