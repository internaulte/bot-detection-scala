package modules.trafficanalysis.jobs.transformations.interfaces

import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

protected[jobs] trait TrafficAnalysisTransformation {
  def addIsTrafficFromBotFlag(httpTrafficStream: DataStream[ApacheCombinedLogFormat])(implicit
      flinkEnv: StreamExecutionEnvironment
  ): DataStream[(Boolean, ApacheCombinedLogFormat)]
}
