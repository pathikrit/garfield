import play.api.Configuration

import scala.slick.model.Model
import scala.slick.codegen.SourceCodeGenerator

/**
 * Custom code generator. Override this to generate custom code
 */
class CustomCodeGenerator(model: Model, database: String, config: Configuration) extends SourceCodeGenerator(model)
