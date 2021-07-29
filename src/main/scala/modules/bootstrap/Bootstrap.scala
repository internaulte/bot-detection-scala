package modules.bootstrap

import modules.common.config.MessagingServersConfig
import modules.trafficgeneration.trafficloadbalancingmock.adapters.kafka.{KafkaClientImpl, KafkaUtils}
import modules.trafficgeneration.trafficloadbalancingmock.adapters.kafka.config.KafkaConfig
import modules.trafficgeneration.trafficloadbalancingmock.adapters.kafka.interfaces.KafkaClient
import modules.trafficgeneration.trafficloadbalancingmock.adapters.repositories.HttpTrafficSendRepositoryImpl
import modules.trafficgeneration.trafficloadbalancingmock.adapters.services.TrafficLoadBalancingServiceImpl
import modules.trafficgeneration.trafficloadbalancingmock.domain.usecases.HttpTrafficGenerationUseCases
import modules.trafficgeneration.webserversmock.adapters.repositories.WebServerMockRepositoryImpl
import modules.trafficgeneration.webserversmock.config.WebServersDataConfig
import modules.trafficgeneration.webserversmock.domain.usecases.WebServerMockUseCases

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
        topicName = MessagingServersConfig.botDetectionTopic,
        numPartitions = KafkaConfig.topicNumberOfPartitions,
        replicationFactor = KafkaConfig.topicReplicationFactor
      )
      startTreatment = System.currentTimeMillis()

      // Start sending Data in parallel for each list of logs:
      _ <- Future.sequence(
        WebServersDataConfig.webServersLogs.map(webServerMockUseCases.sendAllWebServerLogsToBotDetection)
      )
      _ = KafkaUtils.kafkaProducers.values.foreach { kafkaProducer =>
        kafkaProducer.flush()
        kafkaProducer.close()
      }
      treatmentDuration = System.currentTimeMillis() - startTreatment

      //kafka topics close
      _ <- httpTrafficGenerationUseCases.closeTopic(topicName = MessagingServersConfig.botDetectionTopic)
    } yield {
      println(treatmentDuration)
      System.exit(0)
    }
  }
}
