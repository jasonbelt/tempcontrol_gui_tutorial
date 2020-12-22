package bc.BuildingControl.exts

import bc.BuildingControl._

object FanNative_Random {
  val errorRate = 35
  val rand = new java.util.Random
  var isOn: Boolean = false

  def fanCmdActuate(cmd: FanCmd.Type): FanAck.Type = {
    val r = if (rand.nextInt(100) < 100 - errorRate) {
      isOn = cmd == FanCmd.On
      FanAck.Ok
    } else FanAck.Error
    return r
  }
}
