package threads

import util.Utils

object ThreadsExample extends App {
  def doWork = Utils.doWork

  def createThread(work: => Unit) = new Thread(() => work)

  Utils.time("Without threads") {
    doWork
    doWork
//    doWork
//    doWork
//    doWork
//    doWork
//    doWork
//    doWork
  }

  Utils.time("With threads") {
    val thread1 = createThread(doWork)
    val thread2 = createThread(doWork)
//    val thread3 = createThread(doWork)
//    val thread4 = createThread(doWork)
//    val thread5 = createThread(doWork)
//    val thread6 = createThread(doWork)
//    val thread7 = createThread(doWork)
//    val thread8 = createThread(doWork)

    thread1.start()
    thread2.start()
//    thread3.start()
//    thread4.start()
//    thread5.start()
//    thread6.start()
//    thread7.start()
//    thread8.start()

    thread1.join()
    thread2.join()
//    thread3.join()
//    thread4.join()
//    thread5.join()
//    thread6.join()
//    thread7.join()
//    thread8.join()
  }
}
