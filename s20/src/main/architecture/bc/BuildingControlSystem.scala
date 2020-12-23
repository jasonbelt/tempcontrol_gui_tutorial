// #Sireum

package bc

import org.sireum._
import art.ArtSystem

@datatype class BuildingControlSystem extends ArtSystem {
  override def startup(): Unit = { BuildingControlGui.startup() }
  override def shutdown(): Unit = { BuildingControlGui.shutdown() }
}

@ext object BuildingControlGui {
  def startup(): Unit = $
  def shutdown(): Unit = $
}