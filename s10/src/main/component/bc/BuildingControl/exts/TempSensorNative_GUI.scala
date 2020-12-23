package bc.BuildingControl.exts

import org.sireum._
import bc.BuildingControl.guis.{TempSensorGui}
import bc.BuildingControl.{TempUnit, Temperature_i, Util}

import java.util.concurrent.atomic.AtomicReference
import javax.swing.{JComponent, JFrame, SwingUtilities, WindowConstants}

object TempSensorNative_GUI {

  private val sensedTemp: AtomicReference[Temperature_i] = new AtomicReference[Temperature_i](Util.initialTemp);

  private var frame: Option[JFrame] = None()

  // uncomment the following if it should be up to the component
  // to control its GUI (i.e. making it visible, disposing it)
  SwingUtilities.invokeLater(() => createAndShow())

  def create(): JComponent = {
    assert(SwingUtilities.isEventDispatchThread, "Not on EDT")

    val initialTemp = Util.initialTemp.degrees.value
    val low = Util.initialSetPoint.low.degrees.value - 10f
    val high = Util.initialSetPoint.high.degrees.value + 10f

    val tempSensor_GUI = new TempSensorGui(initialTemp, low, high)

    return tempSensor_GUI.$$$getRootComponent$$$()
  }

  def createAndShow(): JFrame = {
    if(frame.isEmpty) {
      val p = create()

      val f = new JFrame("TempSensor")
      f.setContentPane(p)
      f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
      f.pack()
      f.setVisible(true)

      frame = Some(f)
    }
    return frame.get
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

  def setTemperature(v: F32): Unit = {
    sensedTemp.set(Temperature_i(v, TempUnit.Fahrenheit))
  }

  def currentTempGet(): Temperature_i = {
    return sensedTemp.get()
  }
}
