package modules.webserversmock.adapters.repositories.interfaces

import scala.concurrent.Future

trait WebServerMockRepository {
  def sendLogToBotDetection(log: String): Future[Unit]
}
