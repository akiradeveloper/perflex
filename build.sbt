name := "perflex"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  Seq(
    "com.amazonaws" % "aws-java-sdk" % "1.10.12",
    "com.google.guava" % "guava" % "12.0",
    "com.github.scopt" %% "scopt" % "3.3.0"
  )
}
