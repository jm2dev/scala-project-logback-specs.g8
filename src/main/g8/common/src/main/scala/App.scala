package $organization$.$name;format="lower,word"$

import org.slf4j.{Logger,LoggerFactory}

object App {
  def main(args: Array[String]) {
    val logger = LoggerFactory.getLogger("App")
    logger.info("Hello $organization$.$name$!")
  }
}
