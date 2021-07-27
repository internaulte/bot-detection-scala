package modules.trafficloadbalancingmock.domain.usecases

import modules.common.utils.RandomUtils
import modules.trafficloadbalancingmock.adapters.repositories.interfaces.HttpTrafficSendRepository
import modules.trafficloadbalancingmock.config.TargetServersConfig

import scala.concurrent.Future

class HttpTrafficGenerationUseCases(private val httpTrafficSendRepository: HttpTrafficSendRepository) {
  def createTopic(topicName: String, numPartitions: Int, replicationFactor: Short): Future[Unit] = {
    httpTrafficSendRepository.createTopic(topicName, numPartitions, replicationFactor)
  }

  def closeTopic(topicName: String): Future[Unit] = {
    httpTrafficSendRepository.closeTopic(topicName)
  }

  def pushToTopic(message: String, topicName: String): Future[Unit] = {
    val server = getLoadBalancedTargetServer
    httpTrafficSendRepository.pushToTopic(message, topicName, server)
  }

  private def getLoadBalancedTargetServer: String = {
    val indexOfServer = RandomUtils.getRandomInt(maxExcludedValue = TargetServersConfig.targetServers.size)
    TargetServersConfig.targetServers(indexOfServer)
  }
}
