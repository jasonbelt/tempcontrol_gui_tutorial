import mill._
import scalalib._

// Example mill build -- the contents of this file will not be overwritten
//
// mill is included with Sireum Kekinian: https://github.com/sireum/kekinian#installing
//
// To open the following project in VSCode, first follow Sireum Kekinian's
// instructions for setting up a development environment using Scala Metals:
// https://github.com/sireum/kekinian#scala-metals
//
// Then open the folder containing this file in VSCode and import the
// mill build when asked.
//
// To run the demo from the command line:
//   $SIREUM_HOME/bin/mill bc.run
//
// To run the example unit tests:
//   $SIRUEM_HOME/bin/mill bc.test

trait SlangEmbeddedModule extends ScalaModule {

  // refer to https://github.com/sireum/kekinian/blob/master/versions.properties
  // to get the most recent versions of the following dependencies

  // versions.properties key: org.sireum.version.scala
  val scalaVer = "2.13.3"

  // versions.properties key: org.sireum.version.scalatest
  val scalaTestVersion = "3.2.3"

  // versions.properties key: org.sireum.version.scalac-plugin
  // https://github.com/sireum/scalac-plugin/tree/4.20201221.73c7e64
  val sireumScalacVersion = "4.20201221.73c7e64"


  // refer to https://github.com/sireum/kekinian/releases to get the latest
  // Sireum Kekinian release
  // https://github.com/sireum/kekinian/tree/4.20201221.b159c6f
  val kekinianVersion = "4.20201221.b159c6f"


  val inspectorVersion = "0.6-SNAPSHOT"

  val formsRtVersion = "7.0.3"



  def scalaVersion = scalaVer

  override def javacOptions = T { Seq("-source", "1.8", "-target", "1.8", "-encoding", "utf8") }

  override def scalacOptions = T { Seq(
    "-target:jvm-1.8",
    "-deprecation",
    "-Yrangepos",
    "-Ydelambdafy:method",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-language:postfixOps"
  ) }

  override def ivyDeps = Agg(ivy"org.sireum.kekinian::library::${kekinianVersion}")

  override def scalacPluginIvyDeps = Agg(ivy"org.sireum::scalac-plugin::${sireumScalacVersion}")

  override def repositories = super.repositories ++ Seq(
    coursier.maven.MavenRepository("https://jitpack.io/"),
  )
}

trait AadlModule extends SlangEmbeddedModule {
  override def sources = T.sources (
    millSourcePath / os.up / "src" / "main" / "architecture",
    millSourcePath / os.up / "src" / "main" / "art",
    millSourcePath / os.up / "src" / "main" / "bridge",
    millSourcePath / os.up / "src" / "main" / "component",
    millSourcePath / os.up / "src" / "main" / "data",
    millSourcePath / os.up / "src" / "main" / "nix",
    millSourcePath / os.up / "src" / "main" / "seL4Nix"
  )
}

trait AadlTestModule extends AadlModule {
  object test extends Tests {

    final override def millSourcePath =
      super.millSourcePath / os.up / os.up / "src" / "test"

    override def sources = T.sources(
      millSourcePath / "bridge",
      millSourcePath / "util"
    )

    override def ivyDeps = Agg(ivy"org.scalatest::scalatest::${scalaTestVersion}")

    override def testFrameworks = T { Seq("org.scalatest.tools.Framework") }
  }
}

object `bc` extends AadlTestModule {
  override def mainClass = T { Some("bc.Demo") }
}
