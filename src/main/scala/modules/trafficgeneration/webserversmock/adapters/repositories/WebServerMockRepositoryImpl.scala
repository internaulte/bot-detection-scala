package modules.trafficgeneration.webserversmock.adapters.repositories

import modules.trafficgeneration.trafficloadbalancingmock.adapters.services.interfaces.TrafficLoadBalancingService
import modules.trafficgeneration.webserversmock.adapters.repositories.interfaces.WebServerMockRepository
import modules.trafficgeneration.webserversmock.config.WebServersDataConfig

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
