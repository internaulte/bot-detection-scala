package modules.trafficgeneration.webserversmock.adapters.repositories.interfaces

import scala.concurrent.Future

protected[webserversmock] trait WebServerMockRepository {
  def sendLogToBotDetection(log: String): Future[Unit]
}
