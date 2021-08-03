package modules.trafficgeneration.webserversmock.adapters.repositories

import modules.common.config.MessagingServersConfig
import modules.trafficgeneration.trafficloadbalancingmock.adapters.services.interfaces.TrafficLoadBalancingService
import modules.trafficgeneration.webserversmock.adapters.repositories.interfaces.WebServerMockRepository

import scala.concurrent.Future

class WebServerMockRepositoryImpl(private val trafficLoadBalancingService: TrafficLoadBalancingService)
    extends WebServerMockRepository {
  override def sendLogToBotDetection(log: String): Future[Unit] = {
    trafficLoadBalancingService.sendWebServerLog(
      log = log,
      destination = MessagingServersConfig.botDetectionTopic
    )
  }
}
