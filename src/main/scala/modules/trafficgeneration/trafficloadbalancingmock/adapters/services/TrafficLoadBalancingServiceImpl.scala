package modules.trafficgeneration.trafficloadbalancingmock.adapters.services

import modules.trafficgeneration.trafficloadbalancingmock.adapters.services.interfaces.TrafficLoadBalancingService
import modules.trafficgeneration.trafficloadbalancingmock.domain.usecases.HttpTrafficGenerationUseCasesImpl
import modules.trafficgeneration.trafficloadbalancingmock.domain.usecases.interfaces.HttpTrafficGenerationUseCases

import scala.concurrent.Future

protected[this] class TrafficLoadBalancingServiceImpl(
    private val httpTrafficGenerationUseCases: HttpTrafficGenerationUseCases
) extends TrafficLoadBalancingService {

  def sendWebServerLog(log: String, destination: String): Future[Unit] = {
    httpTrafficGenerationUseCases.pushToTopic(message = log, topicName = destination)
  }
}

protected[trafficloadbalancingmock] object TrafficLoadBalancingServiceImpl {
  val trafficLoadBalancingServiceImplSingleton: TrafficLoadBalancingServiceImpl = new TrafficLoadBalancingServiceImpl(
    HttpTrafficGenerationUseCases.httpTrafficGenerationUseCasesSingleton
  )
}
