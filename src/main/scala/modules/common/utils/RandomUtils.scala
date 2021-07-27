package modules.common.utils

import scala.util.Random

object RandomUtils {
  private val randomNumberGeneratorSingleton: Random = new Random()

  def getRandomInt(maxExcludedValue: Int): Int = {
    randomNumberGeneratorSingleton.nextInt(maxExcludedValue)
  }
}
