package modules.bootstrap

import modules.trafficloadbalancingmock.adapters.kafka.KafkaClientImpl
import modules.trafficloadbalancingmock.adapters.kafka.interfaces.KafkaClient
import modules.trafficloadbalancingmock.adapters.repositories.HttpTrafficSendRepositoryImpl
import modules.trafficloadbalancingmock.adapters.services.TrafficLoadBalancingServiceImpl
import modules.trafficloadbalancingmock.domain.usecases.HttpTrafficGenerationUseCases
import modules.webserversmock.adapters.repositories.WebServerMockRepositoryImpl
import modules.webserversmock.config.WebServersDataConfig
import modules.webserversmock.domain.usecases.WebServerMockUseCases

object Bootstrap {
  def main(args: Array[String]): Unit = {

    // Instances initialization
    lazy val kafkaClient: KafkaClient = new KafkaClientImpl
    lazy val httpTrafficSendRepository = new HttpTrafficSendRepositoryImpl(kafkaClient)
    lazy val httpTrafficGenerationUseCases = new HttpTrafficGenerationUseCases(httpTrafficSendRepository)
    lazy val trafficLoadBalancingService = new TrafficLoadBalancingServiceImpl(httpTrafficGenerationUseCases)
    lazy val webServerMockRepository = new WebServerMockRepositoryImpl(trafficLoadBalancingService)
    lazy val webServerMockUseCases = new WebServerMockUseCases(webServerMockRepository)

    // Start sending Data in parallel for each list of logs:
    WebServersDataConfig.webServersLogs.foreach(webServerMockUseCases.sendAllWebServerLogsToBotDetection)
  }
}
