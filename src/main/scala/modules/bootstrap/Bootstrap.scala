package modules.bootstrap

import modules.trafficloadbalancingmock.adapters.kafka.KafkaClientImpl
import modules.trafficloadbalancingmock.adapters.kafka.config.KafkaConfig
import modules.trafficloadbalancingmock.adapters.kafka.interfaces.KafkaClient
import modules.trafficloadbalancingmock.adapters.repositories.HttpTrafficSendRepositoryImpl
import modules.trafficloadbalancingmock.adapters.services.TrafficLoadBalancingServiceImpl
import modules.trafficloadbalancingmock.domain.usecases.HttpTrafficGenerationUseCases
import modules.webserversmock.adapters.repositories.WebServerMockRepositoryImpl
import modules.webserversmock.config.WebServersDataConfig
import modules.webserversmock.domain.usecases.WebServerMockUseCases

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Bootstrap {
  def main(args: Array[String]): Unit = {

    // Instances initialization
    lazy val kafkaClient: KafkaClient = new KafkaClientImpl
    lazy val httpTrafficSendRepository = new HttpTrafficSendRepositoryImpl(kafkaClient)
    lazy val httpTrafficGenerationUseCases = new HttpTrafficGenerationUseCases(httpTrafficSendRepository)
    lazy val trafficLoadBalancingService = new TrafficLoadBalancingServiceImpl(httpTrafficGenerationUseCases)
    lazy val webServerMockRepository = new WebServerMockRepositoryImpl(trafficLoadBalancingService)
    lazy val webServerMockUseCases = new WebServerMockUseCases(webServerMockRepository)

    for {
      //kafka topics creation
      _ <- httpTrafficGenerationUseCases.createTopic(
        topicName = WebServersDataConfig.botDetectionTopic,
        numPartitions = KafkaConfig.topicNumberOfPartitions,
        replicationFactor = KafkaConfig.topicReplicationFactor
      )
      startTreatment = System.currentTimeMillis()

      // Start sending Data in parallel for each list of logs:
      _ <- Future.sequence(
        WebServersDataConfig.webServersLogs.map(webServerMockUseCases.sendAllWebServerLogsToBotDetection)
      )
      treatmentDuration = System.currentTimeMillis() - startTreatment

      //kafka topics close
      _ <- httpTrafficGenerationUseCases.closeTopic(topicName = WebServersDataConfig.botDetectionTopic)
    } yield {
      println(treatmentDuration)
      System.exit(0)
    }
  }
}
