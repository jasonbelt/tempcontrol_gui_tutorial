// #Sireum

package bc.BuildingControl

import org.sireum._

object Util {

  val minTemp: Temperature_i = Temperature_i(70f, TempUnit.Fahrenheit)
  val maxTemp: Temperature_i = Temperature_i(90f, TempUnit.Fahrenheit)

  val initialTemp: Temperature_i = Temperature_i(80f, TempUnit.Fahrenheit)

  val initialSetPoint: SetPoint_i = SetPoint_i(minTemp, maxTemp)

  @pure def toFahrenheit(value: Temperature_i) : Temperature_i = {
    if(value.unit == TempUnit.Fahrenheit) {
      return value
    } else if (value.unit == TempUnit.Celsius) {
      return Temperature_i(value.degrees * 9f / 5f + 32f, TempUnit.Fahrenheit)
    } else {
      return Temperature_i(value.degrees * 9f / 5f - 459.67f, TempUnit.Fahrenheit)
    }
  }
}
