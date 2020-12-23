// #Sireum

package bc.BuildingControl

import org.sireum._
import bc._

// This file will not be overwritten so is safe to edit
object TempControl_i_tcp_tempControl {

  var setPoint: SetPoint_i = Util.initialSetPoint

  def initialise(api: TempControl_i_Initialization_Api): Unit = {
    // outgoing ports are 'event data' so do nothing
  }

  def handlefanAck(api: TempControl_i_Operational_Api, value : FanAck.Type): Unit = {
    api.logInfo(s"received fanAck $value")
    if(value == FanAck.Error) {
      // mitigate
      api.logError("Actuation failed!")
    } else {
      api.logInfo("Actuation worked.")
    }
  }

  def handlesetPoint(api: TempControl_i_Operational_Api, value : SetPoint_i): Unit = {
    api.logInfo(s"received setPoint $value")
    setPoint = SetPoint_i(
      low = Util.toFahrenheit(value.low),
      high = Util.toFahrenheit(value.high))
  }

  def handletempChanged(api: TempControl_i_Operational_Api): Unit = {
    val tempInF = Util.toFahrenheit(api.getcurrentTemp().get)
    val cmdOpt: Option[FanCmd.Type] =
      if (tempInF.degrees > setPoint.high.degrees) Some(FanCmd.On)
      else if (tempInF.degrees < setPoint.low.degrees) Some(FanCmd.Off)
      else None[FanCmd.Type]()
    cmdOpt match {
      case Some(cmd) =>
        api.sendfanCmd(cmd)
        api.logInfo(s"Sent fan command: $cmd")
      case _ =>
        api.logInfo(s"Temperature ok: ${tempInF.degrees} F")
    }
  }

  def activate(api: TempControl_i_Operational_Api): Unit = { }

  def deactivate(api: TempControl_i_Operational_Api): Unit = { }

  def finalise(api: TempControl_i_Operational_Api): Unit = { }

  def recover(api: TempControl_i_Operational_Api): Unit = { }
}