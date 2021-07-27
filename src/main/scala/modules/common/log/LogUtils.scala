package modules.common.log

import org.slf4j.{Logger, LoggerFactory}

object LogUtils {
  val errorLogger: Logger = LoggerFactory.getLogger("errorLogger")
  val noBotsLogger: Logger = LoggerFactory.getLogger("nobotsLogger")
  val botsLogger: Logger = LoggerFactory.getLogger("botsLogger")
}
