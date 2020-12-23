package bc.BuildingControl

import org.sireum._
import art.{ArtNative_Ext, Empty}
import bc._

// This file was auto-generated.  Do not edit
abstract class OperatorInterface_i_tcp_operatorInterface_TestApi extends BridgeTestSuite[OperatorInterface_i_tcp_operatorInterface_Bridge](Arch.BuildingControlDemo_i_Instance_tcp_operatorInterface) {

  /** helper function to set the values of all input ports.
   * @param currentTemp payload for data port currentTemp
   * @param alarm payloads for event data port alarm.
   *   ART currently supports single element event data queues so
   *   only the last element of alarm will be used
   * @param tempChanged the number of events to place in the tempChanged event port queue.
   *   ART currently supports single element event queues so at most
   *   one event will be placed in the queue.
   */
  def put_concrete_inputs(currentTemp : BuildingControl.Temperature_i,
                          alarm : ISZ[BuildingControl.Alarm.Type],
                          tempChanged : Z): Unit = {
    put_currentTemp(currentTemp)
    for(v <- alarm){
      put_alarm(v)
    }
    for(i <- 0 until tempChanged) {
      put_tempChanged()
    }
  }


  /** helper function to check OperatorInterface_i_tcp_operatorInterface's
   * output ports.  Use named arguments to check subsets of the output ports.
   * @param setPoint method that will be called with the payloads to be sent
   *        on the outgoing event data port 'setPoint'.
   * @param clearAlarm method that will be called with the number of events to be sent
   *        on the outgoing event port 'clearAlarm'.
   */
  def check_concrete_output(setPoint: ISZ[BuildingControl.SetPoint_i] => B = setPointParam => {T},
                            clearAlarm: Z => B = clearAlarmParam => {T}): Unit = {
    var testFailures: ISZ[ST] = ISZ()

    var setPointValue: ISZ[BuildingControl.SetPoint_i] = ISZ()
    // TODO: event data port getter should return all of the events/payloads
    //       received on event data ports when queue sizes > 1 support is added
    //       to ART
    if(get_setPoint().nonEmpty) setPointValue = setPointValue :+ get_setPoint().get
    if(!setPoint(setPointValue)) {
      testFailures = testFailures :+ st"'setPoint' did not match expected: received ${setPointValue.size} events with the following payloads ${setPointValue}"
    }
    // TODO: event port getter should return the number of events in
    //       the output queue when queue sizes > 1 support is added to ART
    val clearAlarmValue: Z = if(get_clearAlarm().nonEmpty) z"1" else z"0"
    if(!clearAlarm(clearAlarmValue)) {
      testFailures = testFailures :+ st"'clearAlarm' did not match expected: ${clearAlarmValue} events were in the outgoing event queue"
    }

    assert(testFailures.isEmpty, st"${(testFailures, "\n")}".render)
  }


  // setter for in DataPort
  def put_currentTemp(value : BuildingControl.Temperature_i): Unit = {
    ArtNative_Ext.insertInPortValue(bridge.operational_api.currentTemp_Id, BuildingControl.Temperature_i_Payload(value))
  }

  // setter for in EventDataPort
  def put_alarm(value : BuildingControl.Alarm.Type): Unit = {
    ArtNative_Ext.insertInPortValue(bridge.operational_api.alarm_Id, BuildingControl.Alarm_Payload(value))
  }

  // setter for in EventPort
  def put_tempChanged(): Unit = {
    ArtNative_Ext.insertInPortValue(bridge.operational_api.tempChanged_Id, Empty())
  }

  // getter for out EventDataPort
  def get_setPoint(): Option[BuildingControl.SetPoint_i] = {
    val value: Option[BuildingControl.SetPoint_i] = get_setPoint_payload() match {
      case Some(BuildingControl.SetPoint_i_Payload(v)) => Some(v)
      case Some(v) => fail(s"Unexpected payload on port setPoint.  Expecting 'BuildingControl.SetPoint_i_Payload' but received ${v}")
      case _ => None[BuildingControl.SetPoint_i]()
    }
    return value
  }

  // payload getter for out EventDataPort
  def get_setPoint_payload(): Option[BuildingControl.SetPoint_i_Payload] = {
    return ArtNative_Ext.observeOutPortValue(bridge.initialization_api.setPoint_Id).asInstanceOf[Option[BuildingControl.SetPoint_i_Payload]]
  }

  // getter for out EventPort
  def get_clearAlarm(): Option[art.Empty] = {
    val value: Option[art.Empty] = get_clearAlarm_payload() match {
      case Some(Empty()) => Some(Empty())
      case Some(v) => fail(s"Unexpected payload on port clearAlarm.  Expecting 'Empty' but received ${v}")
      case _ => None[art.Empty]()
    }
    return value
  }

  // payload getter for out EventPort
  def get_clearAlarm_payload(): Option[Empty] = {
    return ArtNative_Ext.observeOutPortValue(bridge.initialization_api.clearAlarm_Id).asInstanceOf[Option[Empty]]
  }

}
