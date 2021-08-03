package modules.trafficanalysis.jobs.preprocessing.interfaces

import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

protected[jobs] trait TrafficAnalysisPreprocessing {
  def preprocessHttpTrafficLogData(httpTrafficStream: DataStream[String])(implicit
      flinkEnv: StreamExecutionEnvironment
  ): DataStream[ApacheCombinedLogFormat]
}
