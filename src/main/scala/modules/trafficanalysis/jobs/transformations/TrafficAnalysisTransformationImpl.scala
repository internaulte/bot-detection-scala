package modules.trafficanalysis.jobs.transformations

import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat
import modules.trafficanalysis.jobs.transformations.interfaces.TrafficAnalysisTransformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

import java.util.regex.Pattern

protected[jobs] class TrafficAnalysisTransformationImpl extends TrafficAnalysisTransformation {
  def addIsTrafficFromBotFlag(httpTrafficStream: DataStream[ApacheCombinedLogFormat])(implicit
      flinkEnv: StreamExecutionEnvironment
  ): DataStream[(Boolean, ApacheCombinedLogFormat)] = {
    httpTrafficStream.map(log => (isSimpleBotLog(log), log))
  }

  private[transformations] def isSimpleBotLog(log: ApacheCombinedLogFormat): Boolean = {
    if (
      isStringOptionEmpty(log.clientIP) || isStringOptionEmpty(log.request) ||
      log.httpRequestDate.isEmpty || isStringOptionEmpty(log.userAgent)
    ) {
      true
    } else {
      log.referer match {
        case Some(refererValue: String) => !isStringValidUrl(refererValue)
        case None => true
      }
    }
  }

  private[transformations] def isStringOptionEmpty(stringOpt: Option[String]): Boolean = {
    stringOpt match {
      case Some(string) => string.trim.isEmpty
      case None => true
    }
  }

  private[transformations] def isStringValidUrl(string: String): Boolean = {
    val owaspUrlRegExp =
      "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)([).!';/?:,][[:blank:]])?$"
    val pattern = Pattern.compile(owaspUrlRegExp)
    pattern.matcher(string).matches()
  }
}
