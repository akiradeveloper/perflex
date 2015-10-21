package perflex.app

import java.io.{File, InputStream, ByteArrayInputStream}
import java.nio.file.{Paths, Path, Files}
import java.util.concurrent.TimeUnit

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.{S3ClientOptions, AmazonS3Client}
import com.google.common.base.Stopwatch
import perflex.{reporter, ReportMaker, Runner}
import perflex.statkind.Time

import scala.util.Random

// Given:
// An empty bucket

object S3Put extends App {
  case class Config(endpoint: String = "",
                    bucketName: String = "",
                    accessKey: String = "BCDEFGHIJKLMNOPQRSTA", // my default
                    secretKey: String = "bcdefghijklmnopqrstuvwxyzabcdefghijklmna",
                    fileSize: Int = 4096)

  val parser = new scopt.OptionParser[Config]("MakeUser") {
    arg[String]("<endpoint>") action { (x, c) => c.copy(endpoint = x) } text("e.g. http://localhost:8080")
    arg[String]("<bucketName>") action { (x, c) => c.copy(bucketName = x) }
    opt[String]("accessKey") action { (x, c) => c.copy(accessKey = x) }
    opt[String]("secretKey") action { (x, c) => c.copy(secretKey = x) }
    opt[Int]("fileSize") action { (x, c) => c.copy(fileSize = x) }
  }
  def run(config: Config) {
    case class Type(time: Float) extends Time

    // make a random file
    val tmpFileName = "/tmp/perflex-file"
    Files.deleteIfExists(Paths.get(tmpFileName))
    val p = Files.createFile(Paths.get(tmpFileName))
    val bytes: Array[Byte] = Array.fill(config.fileSize)(127)
    Files.write(p, bytes)
    val file: File = p.toFile

    val tasks = Stream.fill(10000) { (_:Any) =>
      val conf = new ClientConfiguration
      conf.setSignerOverride("S3SignerType")
      val cli = new AmazonS3Client(new BasicAWSCredentials(config.accessKey, config.secretKey), conf)
      cli.setEndpoint(config.endpoint)
      cli.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true))
      // randname
      val name = Random.alphanumeric.take(32).mkString

      val sw = Stopwatch.createStarted
      cli.putObject(config.bucketName, name, file)
      sw.stop

      Type(sw.elapsed(TimeUnit.MICROSECONDS).toFloat)
    }

    val result = new Runner(tasks).concurrentNumber(32).run

    val report = new ReportMaker(result)
      .withReporters(reporter.TimeAll)
      .make

    println(report)
  }
  parser.parse(args, Config()) match {
    case Some(config) => run(config)
    case _ => println("bye!")
  }
}