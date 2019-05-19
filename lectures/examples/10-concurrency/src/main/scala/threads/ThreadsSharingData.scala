package threads

object ThreadsSharingData extends App {
  var improveCalculation = true

  val thread = new Thread(new Runnable {
    def run(): Unit = {
      var i = 0L

      while (improveCalculation) {
        i += 1
      }

      println(s"Thread exiting: $i")
    }
  })

  thread.start()

  println("Main going to sleep...")
  Thread.sleep(1000)
  println("Main waking up...")

  improveCalculation = false

  thread.join()

  println("Main exiting")
}
