package modules.trafficanalysis.jobs.preprocessing

import core.MockitoUnitTestSpec
import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale

final class TrafficAnalysisPreprocessingImplTest extends MockitoUnitTestSpec {
  private val trafficAnalysisPreprocessing = new TrafficAnalysisPreprocessingImpl

  describe("parseLineToObject") {
    it("should parse a string line to an ApacheCombinedLogFormat") {
      val line =
        """13.66.139.0 - - [19/Dec/2020:13:57:26 +0100] "GET /index.php?option=com_phocagallery&view=category&id=1:almhuette-raith&Itemid=53 HTTP/1.1" 200 32653 "-" "Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)" "-""""

      val dtf =
        new DateTimeFormatterBuilder()
          .appendPattern("dd/MMM/yyyy:HH:mm:ss Z")
          .toFormatter(Locale.ENGLISH)
      val expectedResult = ApacheCombinedLogFormat(
        clientIP = Some("13.66.139.0"),
        clientIdentId = None,
        userName = None,
        httpRequestDate = Some(ZonedDateTime.parse("19/Dec/2020:13:57:26 +0100", dtf)),
        request = Some("GET /index.php?option=com_phocagallery&view=category&id=1:almhuette-raith&Itemid=53 HTTP/1.1"),
        statusCode = Some("200"),
        responseSize = Some(32653),
        referer = None,
        userAgent = Some("Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)")
      )

      assert(trafficAnalysisPreprocessing.parseLineToObject(line) == expectedResult)
    }
  }

  describe("convertStringValueToOption") {
    it("it should return an empty option if empty string") {
      assert(trafficAnalysisPreprocessing.convertStringValueToOption("").isEmpty)
    }

    it("should return an empty option if string == -") {
      assert(trafficAnalysisPreprocessing.convertStringValueToOption("-").isEmpty)
    }

    it("should return some value if string is valid") {
      assert(trafficAnalysisPreprocessing.convertStringValueToOption("a").nonEmpty)
    }
  }

  describe("parseLineToLog") {
    it("should return the line correctly parsed") {
      val line =
        """13.66.139.0 - - [19/Dec/2020:13:57:26 +0100] "GET /index.php?option=com_phocagallery&view=category&id=1:almhuette-raith&Itemid=53 HTTP/1.1" 200 32653 "-" "Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)" "-""""

      val expectedReturn = Seq(
        "13.66.139.0",
        "-",
        "-",
        "19/Dec/2020:13:57:26 +0100",
        "GET /index.php?option=com_phocagallery&view=category&id=1:almhuette-raith&Itemid=53 HTTP/1.1",
        "200",
        "32653",
        "-",
        "Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)",
        "-"
      )

      assert(trafficAnalysisPreprocessing.parseLineToLog(line) == expectedReturn)
    }
  }

  describe("getNewIsEscapedValue") {
    it("should return true if start escape") {
      assert(
        trafficAnalysisPreprocessing.getNewIsEscapedValue(
          isEscaped = true,
          isStartEscapeChar = true,
          isEndEscapeChar = false
        )
      )
      assert(
        trafficAnalysisPreprocessing.getNewIsEscapedValue(
          isEscaped = false,
          isStartEscapeChar = true,
          isEndEscapeChar = false
        )
      )
    }
    it("should return true if was escaped and not end escaping") {
      assert(
        trafficAnalysisPreprocessing.getNewIsEscapedValue(
          isEscaped = true,
          isStartEscapeChar = false,
          isEndEscapeChar = false
        )
      )
      assert(
        trafficAnalysisPreprocessing.getNewIsEscapedValue(
          isEscaped = true,
          isStartEscapeChar = true,
          isEndEscapeChar = false
        )
      )
    }

    it("should return false if end escaping") {
      assert(
        !trafficAnalysisPreprocessing.getNewIsEscapedValue(
          isEscaped = true,
          isStartEscapeChar = false,
          isEndEscapeChar = true
        )
      )
      assert(
        !trafficAnalysisPreprocessing.getNewIsEscapedValue(
          isEscaped = false,
          isStartEscapeChar = false,
          isEndEscapeChar = true
        )
      )
    }
  }

  describe("isStartEscapeChar") {
    it("should return false if char is not start escape") {
      assert(!trafficAnalysisPreprocessing.isStartEscapeChar('t'))
    }

    it("should return true if char is start escape") {
      assert(trafficAnalysisPreprocessing.isStartEscapeChar('['))
      assert(trafficAnalysisPreprocessing.isStartEscapeChar('"'))
    }
  }

  describe("isEndEscapeChar") {
    it("should return false if char is not end escape") {
      assert(!trafficAnalysisPreprocessing.isEndEscapeChar('t'))
    }

    it("should return true if char is end escape") {
      assert(trafficAnalysisPreprocessing.isEndEscapeChar(']'))
      assert(trafficAnalysisPreprocessing.isEndEscapeChar('"'))
    }
  }

  describe("isCuttingChar") {
    it("should return true if char is cutting char and not escaped") {
      assert(trafficAnalysisPreprocessing.isCuttingChar(' ', isEscaped = false))
    }

    it("should return false if char is cutting char but escaped") {
      assert(!trafficAnalysisPreprocessing.isCuttingChar(' ', isEscaped = true))
    }

    it("should return false if char is not cutting char") {
      assert(!trafficAnalysisPreprocessing.isCuttingChar('a', isEscaped = true))
      assert(!trafficAnalysisPreprocessing.isCuttingChar('a', isEscaped = false))
    }
  }

  describe("getNewLog") {
    it("should return initial logs if not cut char") {
      val parsingString = "test"
      val initialLogs = Seq("testi, testu")
      assert(
        trafficAnalysisPreprocessing.getNewLog(
          isCutting = false,
          parsingString = parsingString,
          logs = initialLogs
        ) == initialLogs
      )
    }

    it("should concat parsing string to logs if cut char") {
      val parsingString = "test"
      val initialLogs = Seq("testi", "testu")
      val expectedResult = Seq("tset", "testi", "testu")
      assert(
        trafficAnalysisPreprocessing.getNewLog(
          isCutting = true,
          parsingString = parsingString,
          logs = initialLogs
        ) == expectedResult
      )
    }
  }

  describe("getNewParsingString") {
    it("should return an empty string if at cut position") {
      assert(
        trafficAnalysisPreprocessing.getNewParsingString(
          isCutting = true,
          isStartEscape = true,
          isEndEscape = true,
          char = 'a',
          parsingString = "test"
        ) == ""
      )
    }

    it("should return parsingString if not at cut position but escaping char") {
      val parsingString = "test"
      assert(
        trafficAnalysisPreprocessing.getNewParsingString(
          isCutting = false,
          isStartEscape = true,
          isEndEscape = false,
          char = 'a',
          parsingString = parsingString
        ) == parsingString
      )

      assert(
        trafficAnalysisPreprocessing.getNewParsingString(
          isCutting = false,
          isStartEscape = false,
          isEndEscape = true,
          char = 'a',
          parsingString = parsingString
        ) == parsingString
      )
    }

    it("should return parsingString with char if not at cut position and not escaping char") {
      val parsingString = "test"
      val char = 'a'
      assert(
        trafficAnalysisPreprocessing.getNewParsingString(
          isCutting = false,
          isStartEscape = false,
          isEndEscape = false,
          char = 'a',
          parsingString = parsingString
        ) == s"${char.toString}$parsingString"
      )
    }
  }
}
