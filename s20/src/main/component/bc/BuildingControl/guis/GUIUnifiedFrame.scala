package bc.BuildingControl.guis

import bc.BuildingControl.exts.{FanGUI_Ext, OperatorInterface_GUI, TempSensor_GUI}
import org.sireum.ISZ

import java.awt.FlowLayout
import javax.swing._

object GUIUnifiedFrame {
  var frames: ISZ[JFrame] = ISZ()
  var x: Int = 0
  var y: Int = 0

  def startup(): Unit = {
    SwingUtilities.invokeLater(() => {
      val fan = FanGUI_Ext.create()
      val sensor = TempSensor_GUI.create()
      val interface = OperatorInterface_GUI.create()

      val panel = new JPanel()
      panel.setLayout(new FlowLayout())
      panel.add(fan)
      panel.add(sensor)
      panel.add(interface)

      val sp = new JScrollPane(panel)

      frames = ISZ(createFrame("Unified Frame", sp))
    })
  }

  def shutdown(): Unit = {
    SwingUtilities.invokeLater(() =>
      for(f <- frames) {
        f.dispose()
      }
    )
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
