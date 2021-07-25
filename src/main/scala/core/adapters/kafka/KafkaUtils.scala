package core.adapters.kafka

import akka.actor.ActorSystem
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import utils.{AutoCloseUtils, RandomUtils}

import java.util.Properties

object KafkaUtils {
  implicit private val actorSystem: ActorSystem = ActorSystem("kafkaClient")

  def useRandomKafkaProducer[T](functionToApply: KafkaProducer[String, String] => T): T = {
    val kafkaServerAddresses = Vector(kafkaServerOneProducerProperties, kafkaServerTwoProducerProperties)

    val indexOfKafkaServer = RandomUtils.getRandomInt(maxExcludedValue = kafkaServerAddresses.size)
    AutoCloseUtils.using(new KafkaProducer[String, String](kafkaServerAddresses(indexOfKafkaServer)))(functionToApply)
  }

  private val kafkaServerOneProducerProperties: Properties = {
    val props = new Properties()
    props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.kafkaServerOne)

    setAdditionalKafkaProperties(props)
  }

  private val kafkaServerTwoProducerProperties: Properties = {
    val props = new Properties()
    props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.kafkaServerTwo)

    setAdditionalKafkaProperties(props)
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
