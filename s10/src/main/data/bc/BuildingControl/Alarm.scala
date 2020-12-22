// #Sireum

package bc.BuildingControl

import org.sireum._
import bc._

// This file was auto-generated.  Do not edit

@enum object Alarm {
  "TempOutOfRange"
  "NoAlarm"
}

object Alarm_Payload {
  def empty(): Alarm_Payload = {
    return Alarm_Payload(BuildingControl.Alarm.byOrdinal(0).get)
  }
}

@datatype class Alarm_Payload(value: BuildingControl.Alarm.Type) extends art.DataContent
