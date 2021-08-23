package modules.trafficgeneration.trafficloadbalancingmock.domain.usecases.interfaces

import modules.trafficgeneration.trafficloadbalancingmock.domain.usecases.HttpTrafficGenerationUseCasesImpl

import scala.concurrent.Future

protected[trafficloadbalancingmock] trait HttpTrafficGenerationUseCases {
  def createTopic(topicName: String, numPartitions: Int, replicationFactor: Short): Future[Unit]

  def closeTopic(topicName: String): Future[Unit]

  def pushToTopic(message: String, topicName: String): Future[Unit]
}

protected[trafficloadbalancingmock] object HttpTrafficGenerationUseCases {
  val httpTrafficGenerationUseCasesSingleton: HttpTrafficGenerationUseCases =
    HttpTrafficGenerationUseCasesImpl.httpTrafficGenerationUseCasesImplSingleton
}
