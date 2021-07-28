package modules.webserversmock.config

import modules.webserversmock.domain.entities.WebServerLogs

protected[webServersMock] object WebServersDataConfig {
  val botDetectionTopic: String = sys.env.getOrElse("BOT_DETECTION_TOPIC", "botDetection")
  val iterationsNb: Int = sys.env.getOrElse("BOT_DETECTION_MAX_ITERATIONS", "1000").toInt

  private val firstWebServerLogs: WebServerLogs = WebServerLogs(List())
  private val secondWebServerLogs: WebServerLogs = WebServerLogs(List())
  private val thirdWebServerLogs: WebServerLogs = WebServerLogs(List())

  val webServersLogs: Seq[WebServerLogs] = Seq(firstWebServerLogs, secondWebServerLogs, thirdWebServerLogs)
}
