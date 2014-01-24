import java.io.File
import play.api._
import utils.EmbeddedESServer

object Global extends GlobalSettings {

  var esServer: EmbeddedESServer = _

  var esDataDirectory: File = _

  override def onStart(app: Application) {
    println("Starting Elastic Search")
    esDataDirectory = new File(app.path, "elasticsearch-data")
    esServer = new EmbeddedESServer(esDataDirectory)
  }

  override def onStop(app: Application) {
    esServer.shutdown
  }
}