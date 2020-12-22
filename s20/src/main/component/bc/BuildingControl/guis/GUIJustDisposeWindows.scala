package bc.BuildingControl.guis

import java.awt.Window
import javax.swing.SwingUtilities

object GUIJustDisposeWindows {

  def startup(): Unit = {}

  def shutdown(): Unit = {
    SwingUtilities.invokeLater(() =>
      for(w <- Window.getWindows) {
        w.dispose()
      }
    )
  }
}
