package modules.trafficgeneration.trafficloadbalancingmock.adapters.messaging

import modules.common.config.MessagingServersConfig
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer

import java.util.Properties

object KafkaUtils {
  val kafkaProducers: Map[String, KafkaProducer[String, String]] = {
    val kafkaServerOneProducerProperties: Properties = {
      val props = new Properties()
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, MessagingServersConfig.messagingServerOne)

      setAdditionalKafkaProperties(props)
    }

    val kafkaServerTwoProducerProperties: Properties = {
      val props = new Properties()
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, MessagingServersConfig.messagingServerTwo)

      setAdditionalKafkaProperties(props)
    }

    Map(
      // those kafka producers are assumed to stay permanently opened
      MessagingServersConfig.messagingServerOne -> new KafkaProducer[String, String](kafkaServerOneProducerProperties),
      MessagingServersConfig.messagingServerTwo -> new KafkaProducer[String, String](kafkaServerTwoProducerProperties)
    )
  }

  /**
    * WARNING: non pure function : props parameter is muted !
    * @param props: Properties object to add new properties.
    * @return the props parameter mutated with serialization properties
    */
  private def setAdditionalKafkaProperties(props: Properties): Properties = {
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props
  }
}
