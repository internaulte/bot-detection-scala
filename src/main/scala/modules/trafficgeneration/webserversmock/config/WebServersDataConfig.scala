package modules.trafficgeneration.webserversmock.config

import modules.trafficgeneration.webserversmock.domain.entities.WebServerLogs

protected[webServersMock] object WebServersDataConfig {
  val iterationsNb: Int = sys.env.getOrElse("BOT_DETECTION_MAX_ITERATIONS", "100").toInt

  private val firstWebServerLogs: WebServerLogs = WebServerLogs(List())
  private val secondWebServerLogs: WebServerLogs = WebServerLogs(List())
  private val thirdWebServerLogs: WebServerLogs = WebServerLogs(List())

  val webServersLogs: Seq[WebServerLogs] = Seq(firstWebServerLogs, secondWebServerLogs, thirdWebServerLogs)
}
