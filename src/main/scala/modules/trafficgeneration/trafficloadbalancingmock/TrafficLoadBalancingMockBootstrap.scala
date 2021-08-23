package modules.trafficgeneration.trafficloadbalancingmock

import modules.common.config.MessagingServersConfig
import modules.trafficgeneration.trafficloadbalancingmock.adapters.messaging.config.KafkaConfig
import modules.trafficgeneration.trafficloadbalancingmock.domain.usecases.interfaces.HttpTrafficGenerationUseCases

import scala.concurrent.Future

protected[trafficgeneration] object TrafficLoadBalancingMockBootstrap {
  def bootstrap(): Future[Unit] = {
    HttpTrafficGenerationUseCases.httpTrafficGenerationUseCasesSingleton.createTopic(
      topicName = MessagingServersConfig.botDetectionTopic,
      numPartitions = KafkaConfig.topicNumberOfPartitions,
      replicationFactor = KafkaConfig.topicReplicationFactor
    )
  }
}
