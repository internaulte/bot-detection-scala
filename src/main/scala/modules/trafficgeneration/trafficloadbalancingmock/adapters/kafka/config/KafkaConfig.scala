package modules.trafficgeneration.trafficloadbalancingmock.adapters.kafka.config

object KafkaConfig {
  val topicNumberOfPartitions: Int = sys.env.getOrElse("KAFKA_TOPICS_NUMBER_PARTITIONS", "1").toInt
  val topicReplicationFactor: Short = sys.env.getOrElse("KAFKA_TOPICS_REPLICATION_FACTOR", "1").toShort
}
