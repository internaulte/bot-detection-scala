package modules.trafficgeneration.trafficloadbalancingmock.adapters.services

import modules.trafficgeneration.trafficloadbalancingmock.adapters.services.interfaces.TrafficLoadBalancingService
import modules.trafficgeneration.trafficloadbalancingmock.domain.usecases.HttpTrafficGenerationUseCases

import scala.concurrent.Future

class TrafficLoadBalancingServiceImpl(private val httpTrafficGenerationUseCases: HttpTrafficGenerationUseCases)
    extends TrafficLoadBalancingService {

  def sendWebServerLog(log: String, destination: String): Future[Unit] = {
    httpTrafficGenerationUseCases.pushToTopic(message = log, topicName = destination)
  }
}
