package perflex.app

import java.util.concurrent.TimeUnit

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.{S3ClientOptions, AmazonS3Client}
import com.google.common.base.Stopwatch
import perflex.{reporter, ReportMaker, Runner}
import perflex.statkind.Time

object S3Get extends App {
  case class Config(endpoint: String = "",
                    bucketName: String = "",
                    keyName: String = "",
                    accessKey: String = "BCDEFGHIJKLMNOPQRSTA", // my default
                    secretKey: String = "bcdefghijklmnopqrstuvwxyzabcdefghijklmna")

  val parser = new scopt.OptionParser[Config]("MakeUser") {
    arg[String]("<endpoint>") action { (x, c) => c.copy(endpoint = x) } text("e.g. http://localhost:8080")
    arg[String]("<bucketName>") action { (x, c) => c.copy(bucketName = x) }
    arg[String]("<keyName>") action { (x, c) => c.copy(keyName = x) }
    opt[String]("<accessKey>") action { (x, c) => c.copy(accessKey = x) }
    opt[String]("<secretKey>") action { (x, c) => c.copy(secretKey = x) }
  }
  def run(config: Config) {
    case class Type(time: Float) extends Time

    val tasks = Stream.fill(10000) { (_:Any) =>
      val conf = new ClientConfiguration
      conf.setSignerOverride("S3SignerType")
      val cli = new AmazonS3Client(new BasicAWSCredentials(config.accessKey, config.secretKey), conf)
      cli.setEndpoint(config.endpoint)
      cli.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true))
      val sw = Stopwatch.createStarted
      cli.getObject(config.bucketName, config.keyName)
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
