package bc.BuildingControl.exts

import org.sireum._
import bc.BuildingControl._

object TempSensorNative_Random {
  var lastTemperature = Temperature_i(68f, TempUnit.Fahrenheit)
  var rand = new java.util.Random

  def currentTempGet(): Temperature_i = {
    lastTemperature = if (rand.nextBoolean()) {
      val delta =
        F32((rand.nextGaussian() * 3).abs.min(2).toFloat *
          (if (FanNative_Random.isOn) -1 else 1))
      lastTemperature(degrees = lastTemperature.degrees + delta)
    } else lastTemperature
    lastTemperature
  }

}