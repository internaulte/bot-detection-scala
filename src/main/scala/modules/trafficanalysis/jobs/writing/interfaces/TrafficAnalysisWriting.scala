package modules.trafficanalysis.jobs.writing.interfaces

import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

protected[jobs] trait TrafficAnalysisWriting {
  def sendAnalysisResultToStore(analysisResults: DataStream[(Boolean, ApacheCombinedLogFormat)])(implicit
      flinkEnv: StreamExecutionEnvironment
  ): Unit
}
