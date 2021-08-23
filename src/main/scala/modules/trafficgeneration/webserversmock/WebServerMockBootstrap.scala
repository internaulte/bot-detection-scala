package modules.trafficgeneration.webserversmock

import modules.trafficgeneration.trafficloadbalancingmock.adapters.services.interfaces.TrafficLoadBalancingService
import modules.trafficgeneration.webserversmock.adapters.repositories.WebServerMockRepositoryImpl
import modules.trafficgeneration.webserversmock.domain.usecases.WebServerMockUseCases

protected[trafficgeneration] object WebServerMockBootstrap {
  private lazy val webServerMockRepository = new WebServerMockRepositoryImpl(
    TrafficLoadBalancingService.trafficLoadBalancingServiceSingleton
  )
  private lazy val webServerMockUseCases = new WebServerMockUseCases(webServerMockRepository)

  def bootstrap(): Unit = {}
}
