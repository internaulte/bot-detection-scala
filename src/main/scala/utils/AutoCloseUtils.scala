package utils

import java.io.Closeable
import scala.util.{Failure, Success, Try}

object AutoCloseUtils {

  /**
    * Executes a block with a closeable resource, and closes it after the block runs
    * Suppress after upgrade to Scala 2.13 and use Scala Using object
    *
    * Warning: do not use with future function as resource is closed as soon as function returned.
    *
    * @tparam A the return type of the block
    * @tparam B the closeable resource type
    * @param closeable the closeable resource
    * @param functionToApply the function that must be applied
    */
  def using[A, B <: Closeable](closeable: B)(functionToApply: B => A): A = {
    Try(functionToApply(closeable)) match {
      case Failure(exception) =>
        Try(closeable.close())
        throw exception
      case Success(value) =>
        Try(closeable.close())
        value
    }
  }
}
