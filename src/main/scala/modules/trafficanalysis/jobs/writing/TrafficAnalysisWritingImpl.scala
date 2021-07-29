package modules.trafficanalysis.jobs.writing

import modules.common.log.LogUtils.{botsLogger, noBotsLogger}
import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat
import modules.trafficanalysis.jobs.writing.interfaces.TrafficAnalysisWriting
import org.apache.flink.streaming.api.scala.{DataStream, createTypeInformation}

protected[jobs] class TrafficAnalysisWritingImpl extends TrafficAnalysisWriting {
  override def sendAnalysisResultToStore(analysisResults: DataStream[(Boolean, ApacheCombinedLogFormat)]): Unit = {
    analysisResults.map {
      case (isBot: Boolean, log: ApacheCombinedLogFormat) =>
        if (isBot) {
          botsLogger.info(log.toString)
        } else {
          noBotsLogger.info(log.toString)
        }
    }
  }
}
