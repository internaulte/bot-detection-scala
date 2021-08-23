package modules.trafficgeneration

import modules.trafficgeneration.trafficloadbalancingmock.TrafficLoadBalancingMockBootstrap

import scala.concurrent.Future

object TrafficGenerationBootstrap {
  def bootstrap(): Future[Unit] = {
    TrafficLoadBalancingMockBootstrap.bootstrap()
  }
}
