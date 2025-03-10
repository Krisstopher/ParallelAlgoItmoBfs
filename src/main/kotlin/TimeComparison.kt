import CubeGraphCreator.createCube
import java.util.concurrent.ForkJoinPool
import kotlin.system.measureTimeMillis

fun main() {
  val testRuns = 5
  val threads = 4
  val graph = createCube(300)

  var seqTimeSum = 0L
  var parTimeSum = 0L

  println("Running sequential bfs:")
  for (i in 0 until testRuns) {
    val seqBfs = SeqBfs(graph)
    val seqTime = measureTimeMillis { seqBfs.search(0) }
    println("For ${i}th run time is: $seqTime ms")
    seqTimeSum += seqTime
  }

  val avgSeqTime = seqTimeSum / testRuns
  println("Average sequential bfs time: $avgSeqTime ms\n")

  println("Running parallel bfs:")
  for (i in 0 until testRuns) {
    val pool = ForkJoinPool(threads)
    val parBfs = ParBfs(graph, pool)
    val parTime = measureTimeMillis { parBfs.search(0) }
    println("For ${i}th run time is: $parTime ms")
    parTimeSum += parTime
    pool.shutdown()
  }

  val avgParTime = parTimeSum / testRuns
  println("Average parallel bfs time: $avgParTime ms\n")

  val timesFaster = String.format("%.2f", avgSeqTime.toDouble() / avgParTime.toDouble())
  println("Parallel bfs on $threads threads is $timesFaster times faster than sequential")
}