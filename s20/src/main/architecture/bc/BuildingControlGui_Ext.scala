package bc

import bc.BuildingControl.exts.GuiController

object BuildingControlGui_Ext {
  def startup(): Unit = {
    GuiController.createAndShowUnified()
  }

  def shutdown(): Unit = {
    GuiController.finalise()
  }
}
