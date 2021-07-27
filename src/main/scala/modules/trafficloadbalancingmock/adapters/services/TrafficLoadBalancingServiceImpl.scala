package modules.trafficloadbalancingmock.adapters.services

import modules.trafficloadbalancingmock.adapters.services.interfaces.TrafficLoadBalancingService
import modules.trafficloadbalancingmock.domain.usecases.HttpTrafficGenerationUseCases

import scala.concurrent.Future

class TrafficLoadBalancingServiceImpl(private val httpTrafficGenerationUseCases: HttpTrafficGenerationUseCases)
    extends TrafficLoadBalancingService {

  def sendWebServerLog(log: String, destination: String): Future[Unit] = {
    httpTrafficGenerationUseCases.pushToTopic(message = log, topicName = destination)
  }
}
