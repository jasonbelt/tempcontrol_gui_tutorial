// #Sireum

package bc.BuildingControl

import org.sireum._
import art._
import bc._

@sig trait OperatorInterface_i_Api {
  def id: Art.BridgeId
  def currentTemp_Id : Art.PortId
  def setPoint_Id : Art.PortId
  def alarm_Id : Art.PortId
  def tempChanged_Id : Art.PortId
  def clearAlarm_Id : Art.PortId

  def sendsetPoint(value : BuildingControl.SetPoint_i) : Unit = {
    Art.putValue(setPoint_Id, BuildingControl.SetPoint_i_Payload(value))
  }

  def sendclearAlarm() : Unit = {
    Art.putValue(clearAlarm_Id, art.Empty())
  }

  def logInfo(msg: String): Unit = {
    Art.logInfo(id, msg)
  }

  def logDebug(msg: String): Unit = {
    Art.logDebug(id, msg)
  }

  def logError(msg: String): Unit = {
    Art.logError(id, msg)
  }
}

@datatype class OperatorInterface_i_Initialization_Api (
  val id: Art.BridgeId,
  val currentTemp_Id : Art.PortId,
  val setPoint_Id : Art.PortId,
  val alarm_Id : Art.PortId,
  val tempChanged_Id : Art.PortId,
  val clearAlarm_Id : Art.PortId) extends OperatorInterface_i_Api

@datatype class OperatorInterface_i_Operational_Api (
  val id: Art.BridgeId,
  val currentTemp_Id : Art.PortId,
  val setPoint_Id : Art.PortId,
  val alarm_Id : Art.PortId,
  val tempChanged_Id : Art.PortId,
  val clearAlarm_Id : Art.PortId) extends OperatorInterface_i_Api {

  def getcurrentTemp() : Option[BuildingControl.Temperature_i] = {
    val value : Option[BuildingControl.Temperature_i] = Art.getValue(currentTemp_Id) match {
      case Some(BuildingControl.Temperature_i_Payload(v)) => Some(v)
      case Some(v) =>
        Art.logError(id, s"Unexpected payload on port currentTemp.  Expecting 'BuildingControl.Temperature_i_Payload' but received ${v}")
        None[BuildingControl.Temperature_i]()
      case _ => None[BuildingControl.Temperature_i]()
    }
    return value
  }

  def getalarm() : Option[BuildingControl.Alarm.Type] = {
    val value : Option[BuildingControl.Alarm.Type] = Art.getValue(alarm_Id) match {
      case Some(BuildingControl.Alarm_Payload(v)) => Some(v)
      case Some(v) =>
        Art.logError(id, s"Unexpected payload on port alarm.  Expecting 'BuildingControl.Alarm_Payload' but received ${v}")
        None[BuildingControl.Alarm.Type]()
      case _ => None[BuildingControl.Alarm.Type]()
    }
    return value
  }

  def gettempChanged() : Option[art.Empty] = {
    val value : Option[art.Empty] = Art.getValue(tempChanged_Id) match {
      case Some(Empty()) => Some(Empty())
      case Some(v) =>
        Art.logError(id, s"Unexpected payload on port tempChanged.  Expecting 'Empty' but received ${v}")
        None[art.Empty]()
      case _ => None[art.Empty]()
    }
    return value
  }
}
