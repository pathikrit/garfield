import scala.util.control.NonFatal

import java.io.File

import play.api._
import play.api.db.BoneCPPlugin
import play.api.db.evolutions.Evolutions
import play.api.Application

import scala.slick.jdbc.meta.{MTable, createModel}
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.driver.{PostgresDriver, H2Driver}
import scala.slick.model.Model

import com.typesafe.config.{ConfigFactory, Config}

/**
 *  This code generator runs Play Framework Evolutions against a provided database (in config)
 *  and then generates code from this database using the Slick code generator
 *  All configs are taken from conf/application.conf
 */
object PlaySlickCodeGenerator {

  def generate(outputDir: File, confDir: File): Set[File] = {
    println("Running codegen ...")
    try  {
      val configFile = new File(confDir, "application.conf")
      val config = Configuration(ConfigFactory.parseFileAnySyntax(configFile))
      val databases = config.getConfig("db").getOrElse(Configuration.empty).subKeys
      databases flatMap {database =>
        generateDatabase(outputDir, config)(database)
      }
    } catch {
      case NonFatal(e) => throw new PlayException("Codegen error", e.getMessage, e)
    }
  }

  /**
   * @param config Configuration params are nested under db.$databaseName.codegen
   * @return None if no evolutions are configured for this database
   */
  private def generateDatabase(outputDir: File, config: Configuration)(database: String): Option[File] = {
    val dbConfig = config.getConfig(s"db.$database").get
    println(s"Generating slick code for db = $database ...")

    val outputPackage = dbConfig.getString("codegen.package").getOrElse(s"db.$database")
    val outputContainer = dbConfig.getString("codegen.container").getOrElse("dao")
    val outputProfile = dbConfig.getString(s"driver") match {
      case Some("org.postgresql.Driver") => "scala.slick.driver.PostgresDriver"
      case d => throw new IllegalArgumentException(s"Unknown db.$database.driver = $d")
    }

    // config for generator database
    val driver = dbConfig.getString("codegen.driver").getOrElse("org.h2.Driver")
    val maybeMode = dbConfig.getString("codegen.mode")
    val baseUrl = dbConfig.getString("codegen.url").getOrElse("jdbc:h2:mem:generator")
    val url = Seq(Some(baseUrl), maybeMode).flatten.mkString(";MODE=")

    // create fake application using in-memory database
    val app = FakeApplication(
      path = new File("dbgen").getCanonicalFile,
      configuration = Configuration.from(Map(
        s"db.$database.url" -> url,
        s"db.$database.driver" -> driver)))
    val dbPlugin = new BoneCPPlugin(app)
    try  {
      val script = Evolutions.evolutionScript(dbPlugin.api, new File("."), dbPlugin.getClass.getClassLoader, database)

      if (script.isEmpty) {
        None
      } else  {
        Evolutions.applyScript(dbPlugin.api, database, script, autocommit = true)
        val db = Database.forDataSource(dbPlugin.api.getDataSource(database))
        val model = db.withSession {implicit session =>
          outputProfile match {
            case "scala.slick.driver.PostgresDriver" =>PostgresDriver.createModel()
            case p => throw new IllegalStateException(s"Unknown profile: $p")
          }
        }

        // generate slick db code and write to file
        val codeGen = new CustomCodeGenerator(model, database, config)
        val fileName = s"$outputContainer.scala"
        codeGen.writeToFile(outputProfile, outputDir.getPath, outputPackage, outputContainer, fileName)

        // return path of generated file
        Some(new File(outputDir.getPath + "/" + outputPackage.replace(".","/") + "/" + fileName))
      }
    } finally  {
      dbPlugin.onStop()
    }
  }
}

/**
 * A Fake play app is used to run mock evolutions
 */
case class FakeApplication(override val path: java.io.File = new java.io.File("."),
                           override val classloader: ClassLoader = classOf[FakeApplication].getClassLoader,
                           override val configuration: Configuration) extends {
  override val sources = None
  override val mode = play.api.Mode.Dev
} with Application with WithDefaultConfiguration with WithDefaultGlobal with WithDefaultPlugins
