name := "naiad"
version := "1.0"
scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.github.mpilquist" % "simulacrum_2.11" % "0.12.0",
  "org.scalaz" %% "scalaz-core" % "7.2.26",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
