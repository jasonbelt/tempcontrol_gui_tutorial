package bc.BuildingControl.exts

import org.sireum._
import bc.BuildingControl._
import bc.BuildingControl.guis.OperatorInterface

import java.awt.Window
import java.util.concurrent.atomic.{AtomicBoolean, AtomicReference}
import javax.swing.{JComponent, JFrame, JSpinner, SwingUtilities, WindowConstants}

object OperatorInterface_GUI {
  private val setPoint: AtomicReference[SetPoint_i] = new AtomicReference[SetPoint_i](Util.initialSetPoint)
  private val alarmCleared: AtomicBoolean = new AtomicBoolean(F)

  private var gui: OperatorInterface = _
  private var frame: Option[JFrame] = None()

  // uncomment the following if it should be up to the component
  // to control its GUI (i.e. making it visible, disposing it)
  SwingUtilities.invokeLater(() => createAndShow())

  def create(): JComponent = {
    assert(SwingUtilities.isEventDispatchThread, "Not on EDT")

    val initTemp = Util.initialTemp.degrees.value
    val lowSetpoint = Util.initialSetPoint.low.degrees.value
    val highSetpoint = Util.initialSetPoint.high.degrees.value

    gui = new OperatorInterface()

    val lowSpinner = gui.spnLowSetPoint
    lowSpinner.addChangeListener(e => setSetPoint())
    lowSpinner.setEditor(new JSpinner.NumberEditor(lowSpinner, "0.0"))
    lowSpinner.setValue(lowSetpoint)

    val highSpinner = gui.spnHighSetPoint
    highSpinner.addChangeListener(e => setSetPoint())
    highSpinner.setEditor(new JSpinner.NumberEditor(gui.spnHighSetPoint, "0.0"))
    highSpinner.setValue(highSetpoint)

    gui.btnClearAlarm.setEnabled(F)
    gui.btnClearAlarm.addActionListener(e => alarmCleared.set(T))

    gui.simpleTempDisplay1.addListeners(lowSpinner, highSpinner)
    gui.simpleTempDisplay1.setCurrentTemp(initTemp)

    return gui.pnlOperatorInterface
  }

  def createAndShow(): JFrame = {
    if(frame.isEmpty) {
      val p = create()

      val f = new JFrame("Operator Interface")
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

        // dispose of the other frames
        for(w <- Window.getWindows()) w.dispose()
      })
    }
  }

  def init(initialTemperature: Temperature_i,
           initialSetPoint: SetPoint_i): Unit = {
    SwingUtilities.invokeLater(() => {
      gui.simpleTempDisplay1.setCurrentTemp(initialTemperature.degrees.value)
    })
  }

  def setCurrentTemp(v: Temperature_i): Unit = {
    SwingUtilities.invokeLater(() => {
      gui.simpleTempDisplay1.setCurrentTemp(v.degrees.value)
    })
  }

  def setAlarm(v: Alarm.Type): Unit = {
    SwingUtilities.invokeLater(() => {
      v match {
        case Alarm.TempOutOfRange =>
          gui.lblAlarmMessage.setText("Temperature out of range")
          gui.btnClearAlarm.setEnabled(T)
        case Alarm.NoAlarm =>
          gui.lblAlarmMessage.setText("")
          gui.btnClearAlarm.setEnabled(F)
      }
    })
  }

  def getSetPoint(): SetPoint_i = {
    return setPoint.get()
  }

  def setSetPoint(): Unit = {
    assert(SwingUtilities.isEventDispatchThread, "Not on EDT")

    val low = F32(gui.spnLowSetPoint.getValue.toString).get
    val high = F32(gui.spnHighSetPoint.getValue.toString).get

    val lowT = Temperature_i(low, TempUnit.Fahrenheit)
    val highT = Temperature_i(high, TempUnit.Fahrenheit)

    setPoint.set(SetPoint_i(lowT, highT))
  }

  def getAlarmCleared(): B = {
    val ret = alarmCleared.get()
    alarmCleared.set(F)
    return ret
  }

  def setAlarmCleared(b: B): Unit = {
    alarmCleared.set(b)
  }
}
