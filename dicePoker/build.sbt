name          := "dicePoker"
organization  := "de.htwg.se"
version       := "0.0.1"
scalaVersion  := "2.11.8"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.jcenterRepo


libraryDependencies ++= {
  val scalaTestV       = "3.0.0-M15"
  val scalaMockV       = "3.2.2"
  Seq(
    "org.scalatest" %% "scalatest"                   % scalaTestV       % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockV       % "test",
    "org.slf4j" % "slf4j-api" % "1.7.5",
    "org.slf4j" % "slf4j-simple" % "1.7.5"
  )
  
}

libraryDependencies +=  "org.scala-lang" % "scala-swing" % "2.11+" 
libraryDependencies += "log4j" % "log4j" % "1.2.14"



fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true

fork in run := true