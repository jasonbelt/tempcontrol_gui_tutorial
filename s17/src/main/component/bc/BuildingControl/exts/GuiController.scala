package bc.BuildingControl.exts

import org.sireum.ISZ

import java.awt.{FlowLayout, Window}
import javax.swing.{JFrame, JPanel, JScrollPane, SwingUtilities, WindowConstants}

object GuiController {
  var frames: ISZ[JFrame] = ISZ()

  def createAndShow(): Unit = {

    SwingUtilities.invokeLater(() => {
      frames = ISZ(
        FanNative_GUI.createAndShow(),
        TempSensorNative_GUI.createAndShow(),
        OperatorInterface_GUI.createAndShow(),
      )

      var x = 0
      for(f <- frames) {
        f.setLocation(x, x)
        x = x + 100
      }
    })
  }

  def createAndShowUnified(): Unit = {
    SwingUtilities.invokeLater(() => {
      val panel = new JPanel()
      panel.setLayout(new FlowLayout())
      panel.add(FanNative_GUI.create())
      panel.add(TempSensorNative_GUI.create())
      panel.add(OperatorInterface_GUI.create())

      val sp = new JScrollPane(panel)
      val f = new JFrame()
      f.setContentPane(sp)
      f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
      f.pack()
      f.setVisible(true)

      frames = ISZ(f)
    })
  }

  def finalise(): Unit = {
    SwingUtilities.invokeLater(() => for(f <- frames) f.dispose())

    // or
    //SwingUtilities.invokeLater(() => for(w <- Window.getWindows) w.dispose())
  }
}
