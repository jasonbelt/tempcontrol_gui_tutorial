// #Sireum

package bc.BuildingControl

import org.sireum._
import art._
import bc._
import bc.BuildingControl.{OperatorInterface_i_tcp_operatorInterface => component}

// This file was auto-generated.  Do not edit

@record class OperatorInterface_i_tcp_operatorInterface_Bridge(
  val id: Art.BridgeId,
  val name: String,
  val dispatchProtocol: DispatchPropertyProtocol,
  val dispatchTriggers: Option[ISZ[Art.PortId]],

  currentTemp: Port[BuildingControl.Temperature_i],
  setPoint: Port[BuildingControl.SetPoint_i],
  alarm: Port[BuildingControl.Alarm.Type],
  tempChanged: Port[art.Empty],
  clearAlarm: Port[art.Empty]
  ) extends Bridge {

  val ports : Bridge.Ports = Bridge.Ports(
    all = ISZ(currentTemp,
              setPoint,
              alarm,
              tempChanged,
              clearAlarm),

    dataIns = ISZ(currentTemp),

    dataOuts = ISZ(),

    eventIns = ISZ(alarm,
                   tempChanged),

    eventOuts = ISZ(setPoint,
                    clearAlarm)
  )

  val initialization_api : OperatorInterface_i_Initialization_Api = {
    val api = OperatorInterface_i_Initialization_Api(
      id,
      currentTemp.id,
      setPoint.id,
      alarm.id,
      tempChanged.id,
      clearAlarm.id
    )
    OperatorInterface_i_tcp_operatorInterface_Bridge.c_initialization_api = Some(api)
    api
  }

  val operational_api : OperatorInterface_i_Operational_Api = {
    val api = OperatorInterface_i_Operational_Api(
      id,
      currentTemp.id,
      setPoint.id,
      alarm.id,
      tempChanged.id,
      clearAlarm.id
    )
    OperatorInterface_i_tcp_operatorInterface_Bridge.c_operational_api = Some(api)
    api
  }

  val entryPoints : Bridge.EntryPoints =
    OperatorInterface_i_tcp_operatorInterface_Bridge.EntryPoints(
      id,

      currentTemp.id,
      setPoint.id,
      alarm.id,
      tempChanged.id,
      clearAlarm.id,

      dispatchTriggers,

      initialization_api,
      operational_api)
}

object OperatorInterface_i_tcp_operatorInterface_Bridge {

  var c_initialization_api: Option[OperatorInterface_i_Initialization_Api] = None()
  var c_operational_api: Option[OperatorInterface_i_Operational_Api] = None()

  @record class EntryPoints(
    OperatorInterface_i_tcp_operatorInterface_BridgeId : Art.BridgeId,

    currentTemp_Id : Art.PortId,
    setPoint_Id : Art.PortId,
    alarm_Id : Art.PortId,
    tempChanged_Id : Art.PortId,
    clearAlarm_Id : Art.PortId,

    dispatchTriggers : Option[ISZ[Art.PortId]],

    initialization_api: OperatorInterface_i_Initialization_Api,
    operational_api: OperatorInterface_i_Operational_Api) extends Bridge.EntryPoints {

    val dataInPortIds: ISZ[Art.PortId] = ISZ(currentTemp_Id)

    val eventInPortIds: ISZ[Art.PortId] = ISZ(alarm_Id,
                                              tempChanged_Id)

    val dataOutPortIds: ISZ[Art.PortId] = ISZ()

    val eventOutPortIds: ISZ[Art.PortId] = ISZ(setPoint_Id,
                                               clearAlarm_Id)

    def compute(): Unit = {
      Art.receiveInput(eventInPortIds, dataInPortIds)
      component.timeTriggered(operational_api)
      Art.sendOutput(eventOutPortIds, dataOutPortIds)
    }

    override
    def testCompute(): Unit = {
      Art.receiveInput(eventInPortIds, dataInPortIds)
      component.timeTriggered(operational_api)
      Art.releaseOutput(eventOutPortIds, dataOutPortIds)
    }

    def activate(): Unit = {
      component.activate(operational_api)
    }

    def deactivate(): Unit = {
      component.deactivate(operational_api)
    }

    def finalise(): Unit = {
      component.finalise(operational_api)
    }

    def initialise(): Unit = {
      component.initialise(initialization_api)
      Art.sendOutput(eventOutPortIds, dataOutPortIds)
    }

    def recover(): Unit = {
      component.recover(operational_api)
    }
  }
}