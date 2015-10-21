package perflex.app

import java.io.File
import java.nio.file.{Path, Paths, Files}

object Utils {
  def ensureFile(fileName: String, size: Int): Path = {
    // make a random file
    Files.deleteIfExists(Paths.get(fileName))
    val p = Files.createFile(Paths.get(fileName))
    val bytes: Array[Byte] = Array.fill(size)(127)
    Files.write(p, bytes)
    p
  }
}
