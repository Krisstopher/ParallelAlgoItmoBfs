import java.lang.Integer.max
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinTask.invokeAll
import java.util.concurrent.RecursiveAction
import java.util.concurrent.atomic.AtomicIntegerArray

class ParBfs(
  private val graph: Array<Array<Int>>,
  private val pool: ForkJoinPool = ForkJoinPool(),
) : Bfs {
  private val distances = AtomicIntegerArray(IntArray(graph.size) { -1 })

  override fun search(start: Int): IntArray {
    var frontier = intArrayOf(start)
    var level = 0
    distances[start] = 0

    while (frontier.isNotEmpty()) {
      val localFrontiers = Array(pool.parallelism * 2) { mutableListOf<Int>() }

      pool.invoke(object : RecursiveAction() {
        override fun compute() {
          parallelProcess(frontier, 0, frontier.size, localFrontiers, level)
        }
      })

      frontier = localFrontiers.flatMap { it }.toIntArray()
      level++
    }

    return distances.toIntArray()
  }

  private fun parallelProcess(
    frontier: IntArray,
    start: Int,
    end: Int,
    localFrontiers: Array<MutableList<Int>>,
    level: Int
  ) {
    if (end - start <= max(2, frontier.size / (pool.parallelism * 4))) {
      val localNext = localFrontiers[(Thread.currentThread().id % localFrontiers.size).toInt()]
      for (i in start until end) {
        val node = frontier[i]
        for (neighbor in graph[node]) {
          if (distances.compareAndSet(neighbor, -1, level + 1)) {
            localNext.add(neighbor)
          }
        }
      }
    } else {
      val mid = (start + end) ushr 1
      invokeAll(
        object : RecursiveAction() {
          override fun compute() = parallelProcess(frontier, start, mid, localFrontiers, level)
        },
        object : RecursiveAction() {
          override fun compute() = parallelProcess(frontier, mid, end, localFrontiers, level)
        }
      )
    }
  }

  private fun AtomicIntegerArray.toIntArray() = IntArray(length()) { get(it) }
}