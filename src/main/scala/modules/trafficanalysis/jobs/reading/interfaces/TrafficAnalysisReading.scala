package modules.trafficanalysis.jobs.reading.interfaces

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

protected[jobs] trait TrafficAnalysisReading {
  def readHttpTrafficStream(implicit flinkEnv: StreamExecutionEnvironment): DataStream[String]
}
