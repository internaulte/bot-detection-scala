package modules.trafficgeneration.trafficloadbalancingmock.adapters.services.interfaces

import scala.concurrent.Future

trait TrafficLoadBalancingService {
  def sendWebServerLog(log: String, destination: String): Future[Unit]
}
