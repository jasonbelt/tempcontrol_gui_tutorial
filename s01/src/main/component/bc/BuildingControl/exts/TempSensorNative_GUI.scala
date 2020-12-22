package bc.BuildingControl.exts

import bc.BuildingControl.guis.TempSensorGui
import org.sireum._
import bc.BuildingControl.{TempUnit, Temperature_i, Util}

import java.util.concurrent.atomic.AtomicReference
import javax.swing._

object TempSensorNative_GUI {
  private var frame: Option[JFrame] = None()
  var lastSensedTemp: AtomicReference[Temperature_i] = new AtomicReference[Temperature_i](Util.initialTemp)

  // uncomment the following if it should be up to the component
  // to control its GUI (i.e. making it visible, disposing it)
  SwingUtilities.invokeLater(() => createAndShow())

  def create(): JComponent = {
    assert(SwingUtilities.isEventDispatchThread, "Not on EDT")

    val low = Util.initialSetPoint.low.degrees.value - 10f
    val high = Util.initialSetPoint.high.degrees.value + 10f
    val initial = Util.initialTemp.degrees.value
    val tempSensor_GUI = new TempSensorGui(low, high, initial)

    return tempSensor_GUI.$$$getRootComponent$$$()
  }

  def createAndShow(): Unit = {
    if(frame.isEmpty) {
      val p = create()

      val f = new JFrame("Temperature Sensor")
      f.setContentPane(p)
      f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
      f.pack()
      f.setVisible(true)

      frame = Some(f)
    }
  }

  /**
   * Optional method required if component is controlling its GUI.
   * This requires adding a finalise extension stub
   */
  def finalise(): Unit = {
    if(frame.nonEmpty) {
      SwingUtilities.invokeLater(() => {
        frame.get.dispose()
        frame = None()
      })
    }
  }

  def setSensedTemp(v: F32): Unit = {
    lastSensedTemp.set(Temperature_i(v, TempUnit.Fahrenheit))
  }

  def currentTempGet(): Temperature_i = {
    return lastSensedTemp.get()
  }
}
