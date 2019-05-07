import concurrent.Http

object HttpRequests extends App {
  import concurrent.Executors.defaultExecutor

  val result = for {
    (a, b) <- Http.get("https://google.com").zip(Http.get("https://www.scala-lang.org/"))

    aLength = a.getResponseBody.length
    bLength = b.getResponseBody.length

    randomNumber <- Http.get(s"https://www.random.org/integers/?num=1&min=1&max=${aLength + bLength}&col=1&base=10&format=plain")
  } yield randomNumber.getResponseBody

  result.foreach(println)
}
