package modules.trafficloadbalancingmock.adapters.kafka

import modules.common.utils.RandomUtils
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer

import java.util.Properties

protected object KafkaUtils {
  def getRandomKafkaProducer: KafkaProducer[String, String] = {
    val indexOfKafkaServer = RandomUtils.getRandomInt(maxExcludedValue = kafkaProducers.size)
    kafkaProducers(indexOfKafkaServer)
  }

  val kafkaProducers: Vector[KafkaProducer[String, String]] = {
    val kafkaServerOneProducerProperties: Properties = {
      val props = new Properties()
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.kafkaServerOne)

      setAdditionalKafkaProperties(props)
    }

    val kafkaServerTwoProducerProperties: Properties = {
      val props = new Properties()
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.kafkaServerTwo)

      setAdditionalKafkaProperties(props)
    }

    Vector(
      // those kafka producers are assumed to stay permanently opened
      new KafkaProducer[String, String](kafkaServerOneProducerProperties),
      new KafkaProducer[String, String](kafkaServerTwoProducerProperties)
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
