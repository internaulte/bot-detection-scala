package modules.trafficgeneration.trafficloadbalancingmock.adapters.messaging

import modules.trafficgeneration.trafficloadbalancingmock.adapters.messaging.interfaces.MessagingClient
import modules.common.config.MessagingServersConfig
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import org.apache.kafka.clients.producer.ProducerRecord

import java.util.{Collections, Properties}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

protected[this] class KafkaClientImpl extends MessagingClient {
  override def createTopic(
      topicName: String,
      numPartitions: Int,
      replicationFactor: Short
  ): Future[Unit] = {
    Future {
      MessagingServersConfig.messagingServers.map { messagingServer =>
        val props = new Properties()
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, messagingServer)
        val adminClient: AdminClient = AdminClient.create(props)

        if (!adminClient.listTopics().names().get().contains(topicName)) {
          val topic = new NewTopic(topicName, numPartitions, replicationFactor)
          adminClient.createTopics(Collections.singleton(topic))
        }
      }
    }
  }

  override def closeTopic(topicName: String): Future[Unit] = {
    Future {
      MessagingServersConfig.messagingServers.map { messagingServer =>
        val props = new Properties()
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, messagingServer)
        val adminClient: AdminClient = AdminClient.create(props)
        if (adminClient.listTopics().names().get().contains(topicName)) {
          adminClient.deleteTopics(Collections.singleton(topicName))
        }
      }
    }
  }

  override def pushToTopic(message: String, topicName: String, server: String): Future[Unit] = {
    Future {
      val record = new ProducerRecord(topicName, "key", message)

      KafkaUtils.kafkaProducers(server).send(record)
    }
  }
}

protected[messaging] object KafkaClientImpl {
  val kafkaClientSingleton = new KafkaClientImpl
}
