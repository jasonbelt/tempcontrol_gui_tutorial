// #Sireum

package bc

import org.sireum._
import art.ArtSystem

@datatype class BuildingControlSystem extends ArtSystem {
  override def startup(): Unit = { BuildingControlGUI.startup() }
  override def shutdown(): Unit = { BuildingControlGUI.shutdown() }
}

@ext(
// Option 1: Individual gui frames,components control their own frame
//   REQ: each extension should call createAndShow during initialization
//   ISSUES:
//     -- frame won't appear until component can start it (e.g. during its init phase)
//     -- need to dispose of the frame so either have shutdown method do it
//        or have component do it via finalize (odd for physical hardware to have a finalize method)
//"BuildingControl.guis.GUIJustDisposeWindows"

// Option 2: Individual gui frames, controlled by
//"BuildingControl.guis.GUIIndividualFrames"

// Option 3: Single frame
"BuildingControl.guis.GUIUnifiedFrame"
) object BuildingControlGUI {
  def startup(): Unit = $
  def shutdown(): Unit = $
}
