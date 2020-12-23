// #Sireum

package bc.BuildingControl

import org.sireum._
import bc._

// This file will not be overwritten so is safe to edit
object TempSensor_i_tcp_tempSensor {

  def initialise(api: TempSensor_i_Initialization_Api): Unit = {
    // initialize outgoing data port
    api.setcurrentTemp(TempSensorNative.currentTempGet())
  }

  def timeTriggered(api: TempSensor_i_Operational_Api): Unit = {
    val temp = TempSensorNative.currentTempGet()

    api.setcurrentTemp(temp)
    api.sendtempChanged()

    val tempInF = Util.toFahrenheit(temp).degrees
    api.logInfo(s"Sensed temperature: $tempInF F")
  }

  def activate(api: TempSensor_i_Operational_Api): Unit = { }

  def deactivate(api: TempSensor_i_Operational_Api): Unit = { }

  def finalise(api: TempSensor_i_Operational_Api): Unit = { }

  def recover(api: TempSensor_i_Operational_Api): Unit = { }
}

@ext(
   "exts.TempSensorNative_GUI"
  //"exts.TempSensorNative_Random"
) object TempSensorNative {
  def currentTempGet(): Temperature_i = $
}