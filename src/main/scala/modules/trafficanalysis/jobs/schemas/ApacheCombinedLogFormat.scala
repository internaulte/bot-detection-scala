package modules.trafficanalysis.jobs.schemas

import java.time.ZonedDateTime

protected[jobs] final case class ApacheCombinedLogFormat(
    clientIP: Option[String],
    clientIdentId: Option[String],
    userName: Option[String],
    httpRequestDate: Option[ZonedDateTime],
    request: Option[String],
    statusCode: Option[String],
    responseSize: Option[Long],
    referer: Option[String],
    userAgent: Option[String]
)
