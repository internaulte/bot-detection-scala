package modules.trafficloadbalancingmock.config

object TargetServersConfig {
  val targetServerOne: String = sys.env.getOrElse("TARGET_SERVER_ONE_URL", "localhost:9092")
  val targetServerTwo: String = sys.env.getOrElse("TARGET_SERVER_TWO_URL", "localhost:9093")
  val targetServers: Vector[String] = Vector(targetServerOne, targetServerTwo)
}
