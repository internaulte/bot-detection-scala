package utils

import scala.util.Random

object RandomUtils {
  private val randomNumberGeneratorSingleton: Random = new Random()

  def getRandomInt: Int = {
    randomNumberGeneratorSingleton.nextInt()
  }

  def getRandomInt(maxExcludedValue: Int): Int = {
    randomNumberGeneratorSingleton.nextInt(maxExcludedValue)
  }
}
