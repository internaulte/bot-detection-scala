package modules.trafficanalysis.jobs.writing

import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat
import modules.trafficanalysis.jobs.writing.interfaces.TrafficAnalysisWriting
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

protected[jobs] class TrafficAnalysisWritingImpl extends TrafficAnalysisWriting {
  override def sendAnalysisResultToStore(analysisResults: DataStream[(Boolean, ApacheCombinedLogFormat)])(implicit
      flinkEnv: StreamExecutionEnvironment
  ): Unit = {
    analysisResults
      .print()
  }
}
