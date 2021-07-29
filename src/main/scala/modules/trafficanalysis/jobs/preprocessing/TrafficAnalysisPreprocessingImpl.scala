package modules.trafficanalysis.jobs.preprocessing

import modules.common.log.LogUtils.errorLogger
import modules.trafficanalysis.jobs.TrafficAnalysisUtils
import modules.trafficanalysis.jobs.preprocessing.interfaces.TrafficAnalysisPreprocessing
import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

import java.time.ZonedDateTime
import scala.util.{Failure, Success, Try}

protected[jobs] class TrafficAnalysisPreprocessingImpl extends TrafficAnalysisPreprocessing {
  override def preprocessHttpTrafficLogData(httpTrafficStream: DataStream[String])(implicit
      flinkEnv: StreamExecutionEnvironment
  ): DataStream[ApacheCombinedLogFormat] = {
    httpTrafficStream.map(line => parseLineToObject(line))
  }

  private def parseLineToObject(line: String): ApacheCombinedLogFormat = {
    val lineAsStrings = parseLineToLog(line)
    Try(
      ApacheCombinedLogFormat(
        clientIP = convertStringValueToOption(lineAsStrings.head),
        clientIdentId = convertStringValueToOption(lineAsStrings(1)),
        userName = convertStringValueToOption(lineAsStrings(2)),
        httpRequestDate = convertStringValueToOption(lineAsStrings(3)).map(ZonedDateTime.parse),
        request = convertStringValueToOption(lineAsStrings(4)),
        statusCode = convertStringValueToOption(lineAsStrings(5)),
        responseSize = convertStringValueToOption(lineAsStrings(6)).map(_.toLong),
        referer = convertStringValueToOption(lineAsStrings(7)),
        userAgent = convertStringValueToOption(lineAsStrings(8))
      )
    ) match {
      case Failure(exception) =>
        errorLogger.error("error in flink preprocessing, invalid data entry", exception)
        ApacheCombinedLogFormat()
      case Success(logObject) => logObject
    }
  }

  private def convertStringValueToOption(stringValue: String): Option[String] = {
    if (stringValue.isEmpty || stringValue == "-") {
      None
    } else {
      Some(stringValue)
    }
  }

  private def parseLineToLog(line: String): Seq[String] = {
    val (log, _, _) = line.foldLeft((Seq.empty[String], "", false)) {
      case ((logs, parsingString, isEscaped), char) =>
        val isStartEscape = isStartEscapeChar(char, isEscaped)
        val isEndEscape = isEndEscapeChar(char, isEscaped)
        val isCutting = isCuttingChar(char, isEscaped)

        val newIsEscapedValue = getNewIsEscapedValue(isEscaped, isStartEscape, isEndEscape)
        val newLog = getNewLog(isCutting, parsingString, logs)
        val newParsingString = getNewParsingString(isCutting, isStartEscape, isEndEscape, char, parsingString)
        (newLog, newParsingString, newIsEscapedValue)
    }
    log.reverse
  }

  private def isStartEscapeChar(char: Char, isEscaped: Boolean): Boolean = {
    if (isEscaped) {
      false
    } else {
      TrafficAnalysisUtils.startEscapeChars.contains(char)
    }
  }

  private def isEndEscapeChar(char: Char, isEscaped: Boolean): Boolean = {
    if (isEscaped) {
      TrafficAnalysisUtils.endEscapeChars.contains(char)
    } else {
      false
    }
  }

  private def isCuttingChar(char: Char, isEscaped: Boolean): Boolean = {
    if (isEscaped) {
      false
    } else {
      char == ' '
    }
  }

  private def getNewIsEscapedValue(
      isEscaped: Boolean,
      isStartEscapeChar: Boolean,
      isEndEscapeChar: Boolean
  ): Boolean = {
    if (isEscaped && isEndEscapeChar) {
      false
    } else {
      isStartEscapeChar
    }
  }

  private def getNewLog(isCutting: Boolean, parsingString: String, logs: Seq[String]): Seq[String] = {
    if (isCutting) { parsingString.reverse +: logs }
    else logs
  }

  private def getNewParsingString(
      isCutting: Boolean,
      isStartEscape: Boolean,
      isEndEscape: Boolean,
      char: Char,
      parsingString: String
  ): String = {
    if (isCutting) { "" }
    else if (!isStartEscape && !isEndEscape) { char +: parsingString }
    else parsingString
  }
}
