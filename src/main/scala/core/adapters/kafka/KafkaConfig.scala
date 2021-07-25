package core.adapters.kafka

object KafkaConfig {
  val kafkaServerOne: String = sys.env.getOrElse("KAFKA_BROKER_ONE_URL", "localhost:9092")
  val kafkaServerTwo: String = sys.env.getOrElse("KAFKA_BROKER_TWO_URL", "localhost:9093")
}
