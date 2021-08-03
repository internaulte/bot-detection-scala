package modules.trafficanalysis.jobs

import modules.common.config.MessagingServersConfig
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.kafka.clients.consumer.ConsumerConfig

import java.util.Properties

protected[trafficanalysis] object TrafficAnalysisUtils {
  val flinkEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  val startEscapeChars = Vector('[', '"')
  val endEscapeChars = Vector(']', '"')

  val consumersProperties: Vector[Properties] = MessagingServersConfig.messagingServers.map { server =>
    val properties = generateProperties
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server)
    properties
  }

  private def generateProperties: Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "ConsumerGroup")
    props
  }
}
