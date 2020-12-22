package bc.BuildingControl.guis

import java.awt.MultipleGradientPaint.CycleMethod
import java.awt._
import java.awt.event.{ComponentEvent, ComponentListener}
import java.awt.image.BufferedImage
import java.text.DecimalFormat
import javax.swing._
import javax.swing.event.{ChangeEvent, ChangeListener}
import javax.swing.text.{DefaultFormatter, NumberFormatter}

//remove if not needed

class SimpleTempDisplay(//dl: JSpinner,
                        //dh: JSpinner,
                        initTemp: Float,
                        var min: Float,
                        var max: Float)
  extends JComponent()
    with ComponentListener {

  private val forceCommitsOnEdit: Boolean = false

  private val useRedrawLimiter: Boolean = false

  var backgroundBuffer: BufferedImage = null

  private var currentTemp: Float = initTemp

  private val singleTaskTimer: Timer =
    new Timer(100, _ => SwingUtilities.invokeLater(() => repaint()))

  private val tempFmt: DecimalFormat = new DecimalFormat("0.0")

  private val formatter: NumberFormatter = new NumberFormatter(tempFmt)

  var desiredLow: Float = _

  var desiredHigh: Float = _

  var backgroundNeedsRedraw: Boolean = true

  addComponentListener(this)

  //addListeners(dl, dh)

  singleTaskTimer.setRepeats(false)

  def setCurrentTemp(currentTemp: Float): Unit = {
    SwingUtilities.invokeLater(() => {
      this.currentTemp = currentTemp
      repaint()
    })
  }

  def addListeners(dl: JSpinner, dh: JSpinner): Unit = {
    if (forceCommitsOnEdit) {
      forceCommitsOnEdit(dl)
      forceCommitsOnEdit(dh)
    }
    dl.addChangeListener(new ChangeListener() {
      override def stateChanged(e: ChangeEvent): Unit = {
        desiredLow = dl.getValue.toString.toFloat
        if (desiredLow >= desiredHigh) {
          dh.setValue(dl.getNextValue)
        }
        backgroundNeedsRedraw = true
        //repaint();
        scheduleRepaint()
      }
    })
    desiredLow = dl.getValue.toString.toFloat
    dh.addChangeListener(new ChangeListener() {
      override def stateChanged(e: ChangeEvent): Unit = {
        desiredHigh = dh.getValue.toString.toFloat
        if (desiredHigh <= desiredLow) {
          dl.setValue(dh.getPreviousValue)
        }
        backgroundNeedsRedraw = true
        //repaint();
        scheduleRepaint()
      }
    })
    desiredHigh = dh.getValue.toString.toFloat
  }

  private def forceCommitsOnEdit(s: JSpinner): Unit = {
    s.getEditor
      .getComponent(0)
      .asInstanceOf[JFormattedTextField]
      .getFormatter
      .asInstanceOf[DefaultFormatter]
      .setCommitsOnValidEdit(true)
  }

  private def scheduleRepaint(): Unit = {
    if (useRedrawLimiter) {
      if (!singleTaskTimer.isRunning) {
        // will do double repaints, but saves lots of draws when holding buttons down
        repaint()
        singleTaskTimer.restart()
      }
    } else {
      repaint()
    }
  }

  protected override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    // draw colored background to offscreen image as needed
    if (backgroundNeedsRedraw) {
      repaintBackground()
      backgroundNeedsRedraw = false
    }
    // use hardcoded val not ratio to avoid ugly scaling
    val lineHeight: Int = 12
    // amount of line to overlap onto temp gradient
    val lineOverlap: Int = 4
    // draw background
    val g2d: Graphics2D = g.asInstanceOf[Graphics2D]
    g2d.drawImage(backgroundBuffer, 0, 0, getWidth, getHeight, null)
    // draw items on screen
    drawCurrentTemp(g2d, lineHeight)
    drawBoundaryLines(g2d, lineHeight, lineOverlap)
    g.dispose()
  }

  private def drawCurrentTemp(g2d: Graphics2D, lineHeight: Int): Unit = {
    // draw indicator of current temp
    val currTempFrac: Float = fracDist(this.currentTemp)
    // draw line
    g2d.setColor(Color.BLACK)
    val x: Int = (currTempFrac * getWidth).toInt
    val y1: Int = 0
    val y2: Int = getHeight - lineHeight
    // width is magic num, make whatever looks good
    g2d.drawLine(x, y1, x, y2)
    // write value
    val tempString: String = tempFmt.format(currentTemp)
    val fontHeight: Int = g2d.getFontMetrics.getHeight
    val fontWidth: Int = g2d.getFontMetrics.stringWidth(tempString)
    g2d.drawString(tempString,
      Math.max(0, Math.min(getWidth - fontWidth, x)),
      Math.min(getHeight, getHeight / 2))
  }

  private def drawBoundaryLines(g2d: Graphics2D,
                                lineHeight: Int,
                                lineOverlap: Int): Unit = {
    g2d.setColor(Color.BLACK)
    val temps: Array[Float] = getTemps()
    val fds: Array[Float] = fractionalDistributions()
    for (i <- fds.indices) {
      val fd: Float = fds(i)
      // draw line
      val x: Int = (fd * getWidth).toInt
      val y1: Int = (getHeight - lineHeight - lineOverlap)
      val y2: Int = getHeight - lineOverlap
      g2d.drawLine(x, Math.max(0, y1), x, y2)
      // write value
      val tempString: String = tempFmt.format(temps(i))
      val fontHeight: Int = g2d.getFontMetrics.getHeight
      val fontWidth: Int = g2d.getFontMetrics.stringWidth(tempString)
      g2d.drawString(tempString,
        Math.max(0, Math.min(getWidth - fontWidth, x)),
        Math.max(0, y2 - fontHeight))
    }
  }

  override def componentResized(e: ComponentEvent): Unit = {
    repaintBackground()
  }

  override def componentMoved(e: ComponentEvent): Unit = {}

  override def componentShown(e: ComponentEvent): Unit = {}

  override def componentHidden(e: ComponentEvent): Unit = {}

  private def repaintBackground(): Unit = {
    if (getHeight <= 0 || getWidth <= 0) {
      return
    }
    backgroundBuffer = super.getGraphicsConfiguration
      .createCompatibleImage(getWidth, getHeight, Transparency.OPAQUE)
    val w: Int = backgroundBuffer.getWidth
    val h: Int = backgroundBuffer.getHeight
    val hw: Int = w / 2
    val hh: Int = h / 2
    val g2d: Graphics2D = backgroundBuffer.getGraphics.asInstanceOf[Graphics2D]
    // classy 1
    val colors: Array[Color] = Array(fromHex("5275A7"),
      fromHex("A3ABBD"),
      fromHex("A3ABBD"),
      fromHex("DD93A5"))
    // colors from L to R w.r.t LHS (left-hand side)
    val slice: Float = 1.0f / colors.length.toFloat
    val delta: Float = max - min
    val fractions: Array[Float] = fractionalDistributions()
    try {
      val lgp: LinearGradientPaint = new LinearGradientPaint(
        0,
        0,
        w.toFloat,
        0,
        fractions,
        colors,
        CycleMethod.NO_CYCLE)
      g2d.setPaint(lgp)
      g2d.fillRect(0, 0, w, h)
    } catch {
      case e: IllegalArgumentException =>
        System.err.println("unable to render background: illegal values " + e)

    }
    g2d.dispose()
  }

  private def fracDist(x: Float): Float = (x - min) / (max - min)

  private def tween(x1: Float, x2: Float, leftWeight: Float): Float = // leftWeight in [0.0, 1.0]
    leftWeight * x1 + (1 - leftWeight) * x2

  private def fractionalDistributions(): Array[Float] =
    Array(fracDist(min),
      fracDist(desiredLow),
      fracDist(desiredHigh),
      fracDist(max))

  private def getTemps(): Array[Float] = Array(min, desiredLow, desiredHigh, max)

  private def fromHex(rgbHex: String): Color = {
    if (rgbHex.length > 6) {
      // strip leading digits. for example many hex color hex strings start with "#"
      new Color(
        java.lang.Integer.parseInt(rgbHex.substring(rgbHex.length - 6), 16))
    }
    new Color(java.lang.Integer.parseInt(rgbHex, 16))
  }

  private def reverse(arr: Array[Float]): Unit = {
    val half: Int = arr.length / 2
    for (i <- 0 until half) {
      val copy: Float = arr(i)
      arr(i) = arr(i + half)
      arr(i + half) = copy
    }
  }

  private def copyThenReverse(arr: Array[Float]): Array[Float] = {
    val cpy: Array[Float] = Array.ofDim[Float](arr.length)
    System.arraycopy(arr, 0, cpy, 0, arr.length)
    reverse(cpy)
    cpy
  }

}
