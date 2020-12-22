package bc.BuildingControl.exts

import org.sireum._
import bc.BuildingControl
import bc.BuildingControl.{SetPoint_i, Temperature_i, Util}

object OperatorInterface_GUI {

  var setPoint: SetPoint_i = Util.initialSetPoint

  def init(initialTemperature: Temperature_i,
           initialSetPoint: BuildingControl.SetPoint_i): Unit = {
    setPoint = initialSetPoint
  }

  def finalise(): Unit = {
    // do nothing for now
  }

  def setCurrentTemp(v: BuildingControl.Temperature_i): Unit = {
    // do nothing for now
  }

  def setAlarm(v: BuildingControl.Alarm.Type): Unit = {
    // do nothing for now
  }

  def getSetPoint(): BuildingControl.SetPoint_i = {
    return setPoint
  }

  def getAlarmCleared(): B = {
    return F
  }
}
