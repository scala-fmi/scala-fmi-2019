package http

object HttpRequests extends App {
  import concurrent.Executors.defaultExecutor

  val result = for {
    (a, b) <- HttpClient.get("https://google.com").zip(HttpClient.get("https://www.scala-lang.org/"))

    aLength = a.getResponseBody.length
    bLength = b.getResponseBody.length
    sum = aLength + bLength

    randomNumber <- HttpClient.get(s"https://www.random.org/integers/?num=1&min=1&max=$sum&col=1&base=10&format=plain")
  } yield randomNumber.getResponseBody

  result.foreach(println)
}
