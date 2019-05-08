package threads

class MutableData(var name: String, var shouldContinue: Boolean)

object ThreadsSharingData2 extends App {
  var improveCalculation = true
  var sharedData = new MutableData("Test", true)

  val thread = new Thread(new Runnable {
    def run(): Unit = {
      var i = 0L

      while (improveCalculation && sharedData.shouldContinue) {
        i += 1
      }

      println(s"Thread exiting: $i")
      println(s"Name: ${sharedData.name}")
    }
  })

  thread.start()

  println("Main going to sleep...")
  Thread.sleep(1000)
  println("Main waking up...")

  improveCalculation = false
  sharedData.name = "Test finished"
  sharedData.shouldContinue = false

  thread.join()

  println("Main exiting")
}
