ThisBuild / resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  "MavenRepository" at "https://mvnrepository.com"
)

name := "bot-detection"

version := "0.0.1-SNAPSHOT"

organization := "Laurent Nault"

ThisBuild / scalaVersion := "2.12.14"

val flinkVersion = "1.13.1"
val kafkaVersion = "2.8.0"
val logbackVersion = "1.2.3"
val scalaTestMockitoVersion = "1.16.37"

lazy val dependencies = Seq(
  "org.apache.flink" %% "flink-clients"           % flinkVersion            % "provided",
  "org.apache.flink" %% "flink-scala"             % flinkVersion            % "provided",
  "org.apache.flink" %% "flink-streaming-scala"   % flinkVersion            % "provided",
  "org.apache.flink" %% "flink-connector-kafka"   % flinkVersion            % "provided",
  "org.apache.flink"  % "flink-connector-base"    % flinkVersion            % "provided",
  "org.apache.kafka"  % "kafka-clients"           % kafkaVersion,
  "ch.qos.logback"    % "logback-classic"         % logbackVersion          % "runtime",
  "org.mockito"      %% "mockito-scala-scalatest" % scalaTestMockitoVersion % Test
)

lazy val root = (project in file(".")).settings(
  libraryDependencies ++= dependencies
)
