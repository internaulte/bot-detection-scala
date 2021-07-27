package modules.trafficloadbalancingmock.adapters.kafka

import modules.trafficloadbalancingmock.config.TargetServersConfig
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer

import java.util.Properties

protected[kafka] object KafkaUtils {
  val kafkaProducers: Map[String, KafkaProducer[String, String]] = {
    val kafkaServerOneProducerProperties: Properties = {
      val props = new Properties()
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, TargetServersConfig.targetServerOne)

      setAdditionalKafkaProperties(props)
    }

    val kafkaServerTwoProducerProperties: Properties = {
      val props = new Properties()
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, TargetServersConfig.targetServerTwo)

      setAdditionalKafkaProperties(props)
    }

    Map(
      // those kafka producers are assumed to stay permanently opened
      TargetServersConfig.targetServerOne -> new KafkaProducer[String, String](kafkaServerOneProducerProperties),
      TargetServersConfig.targetServerTwo -> new KafkaProducer[String, String](kafkaServerTwoProducerProperties)
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
