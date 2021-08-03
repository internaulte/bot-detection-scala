package modules.trafficanalysis.jobs.reading

import org.apache.flink.streaming.api.scala._
import modules.common.config.MessagingServersConfig
import modules.trafficanalysis.jobs.TrafficAnalysisUtils
import modules.trafficanalysis.jobs.reading.interfaces.TrafficAnalysisReading
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

protected[jobs] class TrafficAnalysisReadingImpl extends TrafficAnalysisReading {
  def readHttpTrafficStream(implicit flinkEnv: StreamExecutionEnvironment): DataStream[String] = {
    val dataStreams = TrafficAnalysisUtils.consumersProperties.map(props =>
      flinkEnv.addSource(
        new FlinkKafkaConsumer[String](MessagingServersConfig.botDetectionTopic, new SimpleStringSchema(), props)
      )
    )

    dataStreams.tail.foldLeft(dataStreams.head)((mergedDataStreams, toMergeDataStream) =>
      mergedDataStreams.union(toMergeDataStream)
    )
  }
}
