// #Sireum

package bc.BuildingControl

import org.sireum._
import bc._

// This file will not be overwritten so is safe to edit
object OperatorInterface_i_tcp_operatorInterface {

  var lastSetPoint: SetPoint_i = Util.initialSetPoint

  var lastTemperature: Temperature_i = Util.initialTemp

  def initialise(api: OperatorInterface_i_Initialization_Api): Unit = {

    OperatorInterface.init(lastTemperature, lastSetPoint) // initialise components in the panel

    api.sendsetPoint(lastSetPoint) // send initial setpoint to the temp controller
  }

  def timeTriggered(api: OperatorInterface_i_Operational_Api): Unit = {

    val currTemp = Util.toFahrenheit(api.getcurrentTemp().get) // data port should have a value

    if(lastTemperature != currTemp) {
      lastTemperature = currTemp
      api.logInfo(s"Received sensed temperature ${lastTemperature}")
      OperatorInterface.setCurrentTemp(lastTemperature)
    }

    val currSetPoint = OperatorInterface.getSetPoint()
    if(lastSetPoint != currSetPoint) {
      lastSetPoint = currSetPoint
      api.logInfo(s"User requested a setpoint of: ${lastSetPoint}")
      api.sendsetPoint(lastSetPoint)
    }

    if(OperatorInterface.getAlarmCleared()) {
      api.logInfo("User cleared the alarm")
      api.sendclearAlarm()
    }

    if(api.getalarm().nonEmpty) {
      api.logInfo("Alarm is on")
      OperatorInterface.setAlarm(api.getalarm().get)
    }
  }

  def activate(api: OperatorInterface_i_Operational_Api): Unit = { }

  def deactivate(api: OperatorInterface_i_Operational_Api): Unit = { }

  def finalise(api: OperatorInterface_i_Operational_Api): Unit = {
    OperatorInterface.finalise()
  }

  def recover(api: OperatorInterface_i_Operational_Api): Unit = { }
}

@ext(
  "exts.OperatorInterface_GUI"
) object OperatorInterface {

  def init(initialTemperature: Temperature_i,
           setPoint: BuildingControl.SetPoint_i): Unit = $
  def finalise(): Unit = $

  def setCurrentTemp(v: BuildingControl.Temperature_i): Unit = $
  def setAlarm(v: BuildingControl.Alarm.Type): Unit = $

  def getSetPoint(): BuildingControl.SetPoint_i = $
  def getAlarmCleared(): B = $
}