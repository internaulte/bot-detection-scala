package modules.common.config

object MessagingServersConfig {
  val messagingServerOne: String = sys.env.getOrElse("MESSAGING_SERVER_ONE_URL", "localhost:9092")
  val messagingServerTwo: String = sys.env.getOrElse("MESSAGING_SERVER_TWO_URL", "localhost:9093")
  val messagingServers: Vector[String] = Vector(messagingServerOne, messagingServerTwo)

  val botDetectionTopic: String = sys.env.getOrElse("BOT_DETECTION_TOPIC", "botDetection")
}
