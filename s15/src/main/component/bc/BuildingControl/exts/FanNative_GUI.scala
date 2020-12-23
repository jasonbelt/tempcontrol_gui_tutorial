package bc.BuildingControl.exts

import org.sireum._
import bc.BuildingControl._
import bc.BuildingControl.guis.{FanGui}

import java.util.concurrent.atomic.AtomicReference
import javax.swing.{JComponent, JFrame, SwingUtilities, WindowConstants}

object FanNative_GUI {

  private val fanAck: AtomicReference[FanAck.Type] = new AtomicReference[FanAck.Type](FanAck.Ok)

  private var frame: Option[JFrame] = None()

  // uncomment the following if it should be up to the component
  // to control its GUI (i.e. making it visible, disposing it)
  //SwingUtilities.invokeLater(() => createAndShow())

  def create(): JComponent = {
    assert (SwingUtilities.isEventDispatchThread(), "Not on EDT")

    import scala.jdk.CollectionConverters._
    val values = new java.util.Vector[FanAck.Type](FanAck.elements.elements.asJavaCollection)

    val fanGui = new FanGui(values)

    return fanGui.$$$getRootComponent$$$()
  }

  def createAndShow(): JFrame = {
    if(frame.isEmpty) {
      val p = create()

      val f = new JFrame("Fan")
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
        frame.get.dispose
        frame = None()
      })
    }
  }

  def setFanAck(a: FanAck.Type): Unit = {
    fanAck.set(a)
  }

  def getFanAck(): FanAck.Type = {
    return fanAck.get()
  }

  def fanCmdActuate(cmd: FanCmd.Type): FanAck.Type = {
    // ignore param
    return getFanAck()
  }
}
