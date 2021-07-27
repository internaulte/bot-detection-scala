package modules.webserversmock.adapters.repositories

import modules.trafficloadbalancingmock.adapters.services.interfaces.TrafficLoadBalancingService
import modules.webserversmock.adapters.repositories.interfaces.WebServerMockRepository
import modules.webserversmock.config.WebServersDataConfig

import scala.concurrent.Future

class WebServerMockRepositoryImpl(trafficLoadBalancingService: TrafficLoadBalancingService)
    extends WebServerMockRepository {
  override def sendLogToBotDetection(log: String): Future[Unit] = {
    trafficLoadBalancingService.sendWebServerLog(
      log = log,
      destination = WebServersDataConfig.botDetectionTopic
    )
  }
}
