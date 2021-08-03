package modules.trafficanalysis.jobs.preprocessing

import modules.common.log.LogUtils.errorLogger
import modules.trafficanalysis.jobs.TrafficAnalysisUtils
import modules.trafficanalysis.jobs.preprocessing.interfaces.TrafficAnalysisPreprocessing
import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale
import scala.util.{Failure, Success, Try}

protected[jobs] class TrafficAnalysisPreprocessingImpl extends TrafficAnalysisPreprocessing {
  override def preprocessHttpTrafficLogData(httpTrafficStream: DataStream[String])(implicit
      flinkEnv: StreamExecutionEnvironment
  ): DataStream[ApacheCombinedLogFormat] = {
    httpTrafficStream.map(line => parseLineToObject(line))
  }

  private[preprocessing] def parseLineToObject(line: String): ApacheCombinedLogFormat = {
    val lineAsStrings = parseLineToLog(line)
    val dateTimeFormater = new DateTimeFormatterBuilder()
      .appendPattern("dd/MMM/yyyy:HH:mm:ss Z")
      .toFormatter(Locale.ENGLISH)

    Try(
      ApacheCombinedLogFormat(
        clientIP = convertStringValueToOption(lineAsStrings.head),
        clientIdentId = convertStringValueToOption(lineAsStrings(1)),
        userName = convertStringValueToOption(lineAsStrings(2)),
        httpRequestDate = convertStringValueToOption(lineAsStrings(3)).map(ZonedDateTime.parse(_, dateTimeFormater)),
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

  private[preprocessing] def convertStringValueToOption(stringValue: String): Option[String] = {
    if (stringValue.isEmpty || stringValue == "-") {
      None
    } else {
      Some(stringValue)
    }
  }

  private[preprocessing] def parseLineToLog(line: String): Seq[String] = {
    val (log, lastParsingString, _) = line.foldLeft((Seq.empty[String], "", false)) {
      case ((logs, parsingString, isEscaped), char) =>
        val isStartEscape = isStartEscapeChar(char)
        val isEndEscape = isEndEscapeChar(char)
        val isCutting = isCuttingChar(char, isEscaped)

        val newIsEscapedValue = getNewIsEscapedValue(isEscaped, isStartEscape, isEndEscape)
        val newLog = getNewLog(isCutting, parsingString, logs)
        val newParsingString = getNewParsingString(isCutting, isStartEscape, isEndEscape, char, parsingString)
        (newLog, newParsingString, newIsEscapedValue)
    }
    (lastParsingString +: log).reverse
  }

  private[preprocessing] def isStartEscapeChar(char: Char): Boolean = {
    TrafficAnalysisUtils.startEscapeChars.contains(char)
  }

  private[preprocessing] def isEndEscapeChar(char: Char): Boolean = {
    TrafficAnalysisUtils.endEscapeChars.contains(char)
  }

  private[preprocessing] def isCuttingChar(char: Char, isEscaped: Boolean): Boolean = {
    if (isEscaped) {
      false
    } else {
      char == ' '
    }
  }

  private[preprocessing] def getNewIsEscapedValue(
      isEscaped: Boolean,
      isStartEscapeChar: Boolean,
      isEndEscapeChar: Boolean
  ): Boolean = {
    if (isEscaped && isEndEscapeChar) {
      false
    } else {
      isEscaped || isStartEscapeChar
    }
  }

  private[preprocessing] def getNewLog(isCutting: Boolean, parsingString: String, logs: Seq[String]): Seq[String] = {
    if (isCutting) { parsingString.reverse +: logs }
    else logs
  }

  private[preprocessing] def getNewParsingString(
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
