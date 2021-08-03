package modules.trafficanalysis.jobs.transformations

import core.MockitoUnitTestSpec
import modules.trafficanalysis.jobs.schemas.ApacheCombinedLogFormat

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale

final class TrafficAnalysisTransformationImplTest extends MockitoUnitTestSpec {
  private val trafficAnalysisTransformation = new TrafficAnalysisTransformationImpl

  describe("isStringValidUrl") {
    it("should return true if valid URL") {
      assert(trafficAnalysisTransformation.isStringValidUrl("http://www.bing.com/bingbot.htm"))
    }

    it("should return false if invalid URL") {
      assert(!trafficAnalysisTransformation.isStringValidUrl("http://www.bing.com/bingbot.htm "))
    }
  }

  describe("isStringOptionEmpty") {
    it("should return true if option none or value is empty string or only spaces") {
      assert(trafficAnalysisTransformation.isStringOptionEmpty(None))
      assert(trafficAnalysisTransformation.isStringOptionEmpty(Some("")))
      assert(trafficAnalysisTransformation.isStringOptionEmpty(Some("   ")))
    }
    it("should return false if string non empty and non only spaces") {
      assert(!trafficAnalysisTransformation.isStringOptionEmpty(Some(" a ")))
    }
  }

  describe("isSimpleBotLog") {
    it("should return true if mandatory data missing") {
      val dtf =
        new DateTimeFormatterBuilder()
          .appendPattern("dd/MMM/yyyy:HH:mm:ss Z")
          .toFormatter(Locale.ENGLISH)
      val botLog = ApacheCombinedLogFormat(
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

      assert(trafficAnalysisTransformation.isSimpleBotLog(botLog))
    }

    it("should return true if referer id not a valid url") {
      val dtf =
        new DateTimeFormatterBuilder()
          .appendPattern("dd/MMM/yyyy:HH:mm:ss Z")
          .toFormatter(Locale.ENGLISH)
      val botLog = ApacheCombinedLogFormat(
        clientIP = Some("13.66.139.0"),
        clientIdentId = None,
        userName = None,
        httpRequestDate = Some(ZonedDateTime.parse("19/Dec/2020:13:57:26 +0100", dtf)),
        request = Some("GET /index.php?option=com_phocagallery&view=category&id=1:almhuette-raith&Itemid=53 HTTP/1.1"),
        statusCode = Some("200"),
        responseSize = Some(32653),
        referer = Some("http://www.bing.com/bingbot.htm "),
        userAgent = Some("Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)")
      )

      assert(trafficAnalysisTransformation.isSimpleBotLog(botLog))
    }

    it("should return false if referer id a valid url and no missing mandatory data") {
      val dtf =
        new DateTimeFormatterBuilder()
          .appendPattern("dd/MMM/yyyy:HH:mm:ss Z")
          .toFormatter(Locale.ENGLISH)
      val botLog = ApacheCombinedLogFormat(
        clientIP = Some("13.66.139.0"),
        clientIdentId = None,
        userName = None,
        httpRequestDate = Some(ZonedDateTime.parse("19/Dec/2020:13:57:26 +0100", dtf)),
        request = Some("GET /index.php?option=com_phocagallery&view=category&id=1:almhuette-raith&Itemid=53 HTTP/1.1"),
        statusCode = Some("200"),
        responseSize = Some(32653),
        referer = Some("http://www.bing.com/bingbot.htm"),
        userAgent = Some("Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)")
      )

      assert(!trafficAnalysisTransformation.isSimpleBotLog(botLog))
    }
  }
}
