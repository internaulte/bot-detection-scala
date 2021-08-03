package modules.trafficanalysis.jobs.schemas

import java.time.ZonedDateTime

protected[jobs] final case class ApacheCombinedLogFormat(
    clientIP: Option[String] = None,
    clientIdentId: Option[String] = None,
    userName: Option[String] = None,
    httpRequestDate: Option[ZonedDateTime] = None,
    request: Option[String] = None,
    statusCode: Option[String] = None,
    responseSize: Option[Long] = None,
    referer: Option[String] = None,
    userAgent: Option[String] = None
)
