// #Sireum

package bc.BuildingControl

import org.sireum._
import bc._

// This file was auto-generated.  Do not edit

object Temperature_i {
  def empty(): BuildingControl.Temperature_i = {
    return BuildingControl.Temperature_i(Base_Types.Float_32_empty(), BuildingControl.TempUnit.byOrdinal(0).get)
  }
}

@datatype class Temperature_i(
  degrees : F32,
  unit : BuildingControl.TempUnit.Type) {
}

object Temperature_i_Payload {
  def empty(): Temperature_i_Payload = {
    return Temperature_i_Payload(BuildingControl.Temperature_i.empty())
  }
}

@datatype class Temperature_i_Payload(value: BuildingControl.Temperature_i) extends art.DataContent
