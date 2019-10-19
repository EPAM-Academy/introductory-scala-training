version := "0.1"
scalaVersion := "2.13.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "introductory-scala-training"
  )

lazy val W2 = project
  .in(file("W2"))
  .settings()
