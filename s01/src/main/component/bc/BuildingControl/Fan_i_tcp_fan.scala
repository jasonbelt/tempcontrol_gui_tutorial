// #Sireum

package bc.BuildingControl

import org.sireum._
import bc._

// This file will not be overwritten so is safe to edit
object Fan_i_tcp_fan {

  def initialise(api: Fan_i_Initialization_Api): Unit = {
    // outgoing ports are all 'event data' so do nothing
  }

  def handlefanCmd(api: Fan_i_Operational_Api, value : FanCmd.Type): Unit = {
    api.logInfo(s"Received fanCmd $value")

    val ack = FanNative.fanCmdActuate(value)

    api.sendfanAck(ack)

    api.logInfo(s"Actuation result: ${ack}")
  }

  def activate(api: Fan_i_Operational_Api): Unit = { }

  def deactivate(api: Fan_i_Operational_Api): Unit = { }

  def finalise(api: Fan_i_Operational_Api): Unit = { }

  def recover(api: Fan_i_Operational_Api): Unit = { }
}

@ext("exts.FanNative_Random") object FanNative {
  def fanCmdActuate(cmd: FanCmd.Type): FanAck.Type = $
}
