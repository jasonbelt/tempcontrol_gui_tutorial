// #Sireum

package bc

import org.sireum._
import art._
import art.PortMode._
import art.DispatchPropertyProtocol._

// This file was auto-generated.  Do not edit

object Arch {
  val BuildingControlDemo_i_Instance_tcp_tempSensor : bc.BuildingControl.TempSensor_i_tcp_tempSensor_Bridge = {
    val currentTemp = Port[BuildingControl.Temperature_i] (id = 0, name = "BuildingControlDemo_i_Instance_tcp_tempSensor_currentTemp", mode = DataOut)
    val tempChanged = Port[art.Empty] (id = 1, name = "BuildingControlDemo_i_Instance_tcp_tempSensor_tempChanged", mode = EventOut)

    bc.BuildingControl.TempSensor_i_tcp_tempSensor_Bridge(
      id = 0,
      name = "BuildingControlDemo_i_Instance_tcp_tempSensor",
      dispatchProtocol = Periodic(period = 1000),
      dispatchTriggers = None(),

      currentTemp = currentTemp,
      tempChanged = tempChanged
    )
  }
  val BuildingControlDemo_i_Instance_tcp_tempControl : bc.BuildingControl.TempControl_i_tcp_tempControl_Bridge = {
    val currentTemp = Port[BuildingControl.Temperature_i] (id = 2, name = "BuildingControlDemo_i_Instance_tcp_tempControl_currentTemp", mode = DataIn)
    val fanAck = Port[BuildingControl.FanAck.Type] (id = 3, name = "BuildingControlDemo_i_Instance_tcp_tempControl_fanAck", mode = EventIn)
    val setPoint = Port[BuildingControl.SetPoint_i] (id = 4, name = "BuildingControlDemo_i_Instance_tcp_tempControl_setPoint", mode = EventIn)
    val fanCmd = Port[BuildingControl.FanCmd.Type] (id = 5, name = "BuildingControlDemo_i_Instance_tcp_tempControl_fanCmd", mode = EventOut)
    val tempChanged = Port[art.Empty] (id = 6, name = "BuildingControlDemo_i_Instance_tcp_tempControl_tempChanged", mode = EventIn)

    bc.BuildingControl.TempControl_i_tcp_tempControl_Bridge(
      id = 1,
      name = "BuildingControlDemo_i_Instance_tcp_tempControl",
      dispatchProtocol = Sporadic(min = 1000),
      dispatchTriggers = None(),

      currentTemp = currentTemp,
      fanAck = fanAck,
      setPoint = setPoint,
      fanCmd = fanCmd,
      tempChanged = tempChanged
    )
  }
  val BuildingControlDemo_i_Instance_tcp_fan : bc.BuildingControl.Fan_i_tcp_fan_Bridge = {
    val fanCmd = Port[BuildingControl.FanCmd.Type] (id = 7, name = "BuildingControlDemo_i_Instance_tcp_fan_fanCmd", mode = EventIn)
    val fanAck = Port[BuildingControl.FanAck.Type] (id = 8, name = "BuildingControlDemo_i_Instance_tcp_fan_fanAck", mode = EventOut)

    bc.BuildingControl.Fan_i_tcp_fan_Bridge(
      id = 2,
      name = "BuildingControlDemo_i_Instance_tcp_fan",
      dispatchProtocol = Sporadic(min = 1000),
      dispatchTriggers = None(),

      fanCmd = fanCmd,
      fanAck = fanAck
    )
  }
  val BuildingControlDemo_i_Instance_tcp_operatorInterface : bc.BuildingControl.OperatorInterface_i_tcp_operatorInterface_Bridge = {
    val currentTemp = Port[BuildingControl.Temperature_i] (id = 9, name = "BuildingControlDemo_i_Instance_tcp_operatorInterface_currentTemp", mode = DataIn)
    val setPoint = Port[BuildingControl.SetPoint_i] (id = 10, name = "BuildingControlDemo_i_Instance_tcp_operatorInterface_setPoint", mode = EventOut)
    val alarm = Port[BuildingControl.Alarm.Type] (id = 11, name = "BuildingControlDemo_i_Instance_tcp_operatorInterface_alarm", mode = EventIn)
    val tempChanged = Port[art.Empty] (id = 12, name = "BuildingControlDemo_i_Instance_tcp_operatorInterface_tempChanged", mode = EventIn)
    val clearAlarm = Port[art.Empty] (id = 13, name = "BuildingControlDemo_i_Instance_tcp_operatorInterface_clearAlarm", mode = EventOut)

    bc.BuildingControl.OperatorInterface_i_tcp_operatorInterface_Bridge(
      id = 3,
      name = "BuildingControlDemo_i_Instance_tcp_operatorInterface",
      dispatchProtocol = Periodic(period = 1000),
      dispatchTriggers = None(),

      currentTemp = currentTemp,
      setPoint = setPoint,
      alarm = alarm,
      tempChanged = tempChanged,
      clearAlarm = clearAlarm
    )
  }

  val ad : ArchitectureDescription = {

    ArchitectureDescription(

      components = MSZ (BuildingControlDemo_i_Instance_tcp_tempSensor, BuildingControlDemo_i_Instance_tcp_tempControl, BuildingControlDemo_i_Instance_tcp_fan, BuildingControlDemo_i_Instance_tcp_operatorInterface),

      connections = ISZ (Connection(from = BuildingControlDemo_i_Instance_tcp_tempSensor.currentTemp, to = BuildingControlDemo_i_Instance_tcp_tempControl.currentTemp),
                         Connection(from = BuildingControlDemo_i_Instance_tcp_tempSensor.currentTemp, to = BuildingControlDemo_i_Instance_tcp_operatorInterface.currentTemp),
                         Connection(from = BuildingControlDemo_i_Instance_tcp_tempSensor.tempChanged, to = BuildingControlDemo_i_Instance_tcp_tempControl.tempChanged),
                         Connection(from = BuildingControlDemo_i_Instance_tcp_tempSensor.tempChanged, to = BuildingControlDemo_i_Instance_tcp_operatorInterface.tempChanged),
                         Connection(from = BuildingControlDemo_i_Instance_tcp_tempControl.fanCmd, to = BuildingControlDemo_i_Instance_tcp_fan.fanCmd),
                         Connection(from = BuildingControlDemo_i_Instance_tcp_fan.fanAck, to = BuildingControlDemo_i_Instance_tcp_tempControl.fanAck),
                         Connection(from = BuildingControlDemo_i_Instance_tcp_operatorInterface.setPoint, to = BuildingControlDemo_i_Instance_tcp_tempControl.setPoint))
    )
  }
}