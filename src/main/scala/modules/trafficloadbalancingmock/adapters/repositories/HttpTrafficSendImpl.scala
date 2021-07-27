package modules.trafficloadbalancingmock.adapters.repositories

import modules.trafficloadbalancingmock.adapters.kafka.interfaces.KafkaClient
import modules.trafficloadbalancingmock.adapters.repositories.interfaces.HttpTrafficSend
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.{Collections, Properties}
import scala.concurrent.Future

class HttpTrafficSendImpl(private val kafkaClient: KafkaClient) extends HttpTrafficSend {
  override def createTopic(
      topicName: String,
      serverName: String,
      numPartitions: Int,
      replicationFactor: Short
  ): Future[Unit] = {
    Future {
      val props = new Properties()
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, serverName)
      val adminClient: AdminClient = AdminClient.create(props)

      if (!adminClient.listTopics().names().get().contains(topicName)) {
        val topic = new NewTopic(topicName, numPartitions, replicationFactor)
        adminClient.createTopics(Collections.singleton(topic))
      }
    }
  }

  override def closeTopic(topicName: String, serverName: String): Future[Unit] = {
    Future {
      val props = new Properties()
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, s"$serverName")
      val adminClient: AdminClient = AdminClient.create(props)
      if (adminClient.listTopics().names().get().contains(topicName)) {
        adminClient.deleteTopics(Collections.singleton(topicName))
      }
    }
  }

  override def pushToTopic(
      message: String,
      topicName: String,
      kafkaProducer: KafkaProducer[String, String]
  ): Future[Unit] = {
    Future {
      val record = new ProducerRecord(topicName, "key", message)

      kafkaProducer.send(record)
    }
  }
}
