import sbt._
import Keys._

object PlaySlickEvolutionsCodegenPlugin extends Plugin {

  /**
   * Add custom source code generators in the compile step
   */
  override lazy val projectSettings = Seq(
    sourceGenerators in Compile <+= (baseDirectory, sourceManaged in Compile) map { (baseDir, sourceManagedDir) =>
      val cachedEvolutionsGenerator = FileFunction.cached(baseDir / "target" / "slick-code-cache", FilesInfo.lastModified, FilesInfo.exists) {
        inFiles => PlaySlickCodeGenerator.generate(sourceManagedDir, baseDir / "conf")
      }
      cachedEvolutionsGenerator(recursiveListFiles(baseDir / "conf" / "evolutions").toSet).toSeq
    }
  )

  /**
   * @return recursively list all files in f (empty if f is not a directory)
   */
  private def recursiveListFiles(f: File): Seq[File] = {
    val files = Option(f.listFiles).toSeq.flatten
    files ++ files.filter(_.isDirectory).flatMap(recursiveListFiles)
  }
}
