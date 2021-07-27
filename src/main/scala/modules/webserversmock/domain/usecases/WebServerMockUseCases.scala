package modules.webserversmock.domain.usecases

import modules.webserversmock.adapters.repositories.interfaces.WebServerMockRepository
import modules.webserversmock.config.WebServersDataConfig
import modules.webserversmock.domain.entities.WebServerLogs

import scala.annotation.tailrec
import scala.concurrent.Future

class WebServerMockUseCases(private val webServerMockRepository: WebServerMockRepository) {
  def sendAllWebServerLogsToBotDetection(webServerLogs: WebServerLogs): Future[Unit] = {
    sendWebServerLogsToBotDetection(allLogs = webServerLogs.logs, nonSendLogs = webServerLogs.logs, iteration = 0)
  }

  @tailrec
  private def sendWebServerLogsToBotDetection(
      allLogs: List[String],
      nonSendLogs: List[String],
      iteration: Int
  ): Future[Unit] = {
    nonSendLogs.headOption match {
      case Some(logToSend) =>
        webServerMockRepository.sendLogToBotDetection(logToSend)
        sendWebServerLogsToBotDetection(allLogs, nonSendLogs = nonSendLogs.tail, iteration = iteration + 1)
      case None =>
        if (iteration < WebServersDataConfig.iterationsNb) {
          sendWebServerLogsToBotDetection(allLogs, nonSendLogs = allLogs, iteration = iteration + 1)
        } else {
          Future.successful()
        }
    }
  }
}
