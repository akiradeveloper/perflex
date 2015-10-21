package perflex.app

import java.io.File
import java.nio.file.{Paths, Files}
import java.util.concurrent.TimeUnit

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.{S3ClientOptions, AmazonS3Client}
import com.google.common.base.Stopwatch
import perflex.{reporter, ReportMaker, Runner}
import perflex.statkind.Time

import scala.util.Random


// Given:
// A file in a bucket

object S3Get extends App {
  case class Config(endpoint: String = "",
                    bucketName: String = "",
                    keyName: String = "",
                    accessKey: String = "BCDEFGHIJKLMNOPQRSTA", // my default
                    secretKey: String = "bcdefghijklmnopqrstuvwxyzabcdefghijklmna")

  val parser = new scopt.OptionParser[Config]("MakeUser") {
    arg[String]("<endpoint>") action { (x, c) => c.copy(endpoint = x) } text("e.g. http://localhost:8080")
    opt[String]("bucketName") action { (x, c) => c.copy(bucketName = x) }
    opt[String]("keyName") action { (x, c) => c.copy(keyName = x) }
    opt[String]("accessKey") action { (x, c) => c.copy(accessKey = x) }
    opt[String]("secretKey") action { (x, c) => c.copy(secretKey = x) }
  }

  def run(config: Config) {
    def createCli = {
      val conf = new ClientConfiguration
      conf.setSignerOverride("S3SignerType")
      val cli = new AmazonS3Client(new BasicAWSCredentials(config.accessKey, config.secretKey), conf)
      cli.setEndpoint(config.endpoint)
      cli.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true))
      cli
    }

    case class Type(time: Float) extends Time

    var bucketName = config.bucketName
    var keyName = config.keyName

    val cli = createCli
    if (bucketName == "" || keyName == "") {
      bucketName = Random.alphanumeric.take(20).mkString.toLowerCase
      keyName = Random.alphanumeric.take(20).mkString
      cli.createBucket(bucketName)

      // make a random file
      cli.putObject(bucketName, keyName, Utils.ensureFile("/tmp/perflex-file", 4096).toFile)
    }

    val tasks = Stream.fill(1000) { (_: Unit) =>
      val cli = createCli
      val sw = Stopwatch.createStarted
      cli.getObject(bucketName, keyName)
      sw.stop

      Type(sw.elapsed(TimeUnit.MICROSECONDS).toFloat)
    }

    val result = new Runner(tasks).concurrentNumber(8).run

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
