import sbt._
import Keys._

object PlaySlickPlugin extends Plugin {

  override lazy val projectSettings = Seq(
    sourceGenerators in Compile <+= (baseDirectory, sourceManaged in Compile) map { (baseDir, sourceManagedDir) =>
      val confDir = baseDir / "conf" // configuration directory of the current Play app
      val cachedEvolutionsGenerator = FileFunction.cached(baseDir / "target" / "slick-code-cache", FilesInfo.lastModified, FilesInfo.exists) {
        inFiles => PlaySlickCodeGenerator.generate(sourceManagedDir, confDir)
      }
      val allEvolutionFiles = recursiveListFiles(confDir)
      cachedEvolutionsGenerator(allEvolutionFiles.toSet).toSeq   // we're monitoring file changes in the conf folder
    }
  )

  // get a list of all files in directory, recursively
  private def recursiveListFiles(f: File): Seq[File] = {
    val files = Option(f.listFiles).toSeq.flatten
    files ++ files.filter(_.isDirectory).flatMap(recursiveListFiles)
  }
}
