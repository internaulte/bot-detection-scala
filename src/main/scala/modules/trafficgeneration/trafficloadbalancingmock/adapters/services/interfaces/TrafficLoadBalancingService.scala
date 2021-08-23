package modules.trafficgeneration.trafficloadbalancingmock.adapters.services.interfaces

import modules.trafficgeneration.trafficloadbalancingmock.adapters.services.TrafficLoadBalancingServiceImpl

import scala.concurrent.Future

protected[trafficgeneration] trait TrafficLoadBalancingService {
  def sendWebServerLog(log: String, destination: String): Future[Unit]
}

protected[trafficgeneration] object TrafficLoadBalancingService {
  val trafficLoadBalancingServiceSingleton: TrafficLoadBalancingService =
    TrafficLoadBalancingServiceImpl.trafficLoadBalancingServiceImplSingleton
}
