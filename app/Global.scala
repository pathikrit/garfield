import java.io.File

import play.api.Mode.Mode
import play.api._

object Global extends GlobalSettings {

//  override def onLoadConfig(config: Configuration, path: File, classloader: ClassLoader, mode: Mode) = {
//    val transformed = config.keys.filter(key => config.getString(key).exists(_.startsWith("postgres://")))
//                            .map(key => key -> config.getString(key).map(_.replace("postgres://", "jdbc:postgresql://")))
//                            .toMap
//    super.onLoadConfig(config, path, classloader, mode) ++ Configuration.from(transformed)
//  }
}
