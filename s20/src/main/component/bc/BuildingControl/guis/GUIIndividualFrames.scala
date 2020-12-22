package bc.BuildingControl.guis

import bc.BuildingControl.exts.{FanGUI_Ext, OperatorInterface_GUI, TempSensor_GUI}
import org.sireum.ISZ

import java.awt.Window
import javax.swing.{JComponent, JFrame, SwingUtilities, WindowConstants}

object GUIIndividualFrames {
  var frames: ISZ[JFrame] = ISZ()
  var x: Int = 0
  var y: Int = 0

  def startup(): Unit = {
    // Option 1: scala extension just returns the panel
    SwingUtilities.invokeLater(() => {
      frames = ISZ(
        createFrame("Fan", FanGUI_Ext.create()),
        createFrame("Temp Sensor", TempSensor_GUI.create()),
        createFrame("Operator Interface", OperatorInterface_GUI.create())
      )
    })
  }

  def shutdown(): Unit = {
    SwingUtilities.invokeLater(() =>
      for(f <- frames) {
        f.dispose()
      }
    )

    // or, just close all windows

    /*
    SwingUtilities.invokeLater(() =>
    for(w <- Window.getWindows()) {
      w.dispose()
    })
    */
  }

  def createFrame(title: String, p: JComponent): JFrame = {
    val frame = new JFrame(title)
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
    frame.setContentPane(p)
    frame.pack()
    frame.setLocation(x, y)
    x = x + 100
    y = y + 100
    frame.setVisible(true)
    return frame
  }
}
