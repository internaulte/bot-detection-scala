package modules.webserversmock.config

protected[webServersMock] object WebServersDataConfig {
  lazy val webServersLogs: Seq[Seq[String]] = Seq(firstWebServerLogs, secondWebServerLogs, thirdWebServerLogs)
  private val firstWebServerLogs: Seq[String] = Seq()
  private val secondWebServerLogs: Seq[String] = Seq()
  private val thirdWebServerLogs: Seq[String] = Seq()
}
