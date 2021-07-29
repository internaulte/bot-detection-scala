package modules.trafficanalysis.jobs.preprocessing

import modules.trafficanalysis.jobs.preprocessing.interfaces.TrafficAnalysisPreprocessing
import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

protected[jobs] class TrafficAnalysisPreprocessingImpl extends TrafficAnalysisPreprocessing {
  override def preprocessHttpTrafficLogData(httpTrafficStream: DataStream[String])(implicit
      flinkEnv: StreamExecutionEnvironment
  ): DataStream[ApacheCombinedLogFormat] = {}
}
