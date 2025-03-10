import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.min

class ParallelUtils {
  private companion object {
    const val DEFAULT_THRESHOLD = 1000
  }

  fun parallelFor(
    function: (Int) -> Unit,
    left: Int,
    right: Int,
    step: Int = 1,
    pool: ForkJoinPool = ForkJoinPool(),
    threshold: Int = DEFAULT_THRESHOLD
  ) {
    pool(ParallelForTask(function, left, right, step, threshold))
  }

  private inner class ParallelForTask(
    private val function: (Int) -> Unit,
    private val left: Int,
    private val right: Int,
    private val step: Int,
    private val threshold: Int,
  ) : RecursiveTask<Unit>() {
    override fun compute() {
      if (left >= right) return

      val count = (right - left + step - 1) / step

      if (count < threshold) {
        for (i in left until right step step) function(i)
      } else {
        val mid = left + (count / 2) * step
        val left = ParallelForTask(function, left, mid, step, threshold)
        val right = ParallelForTask(function, mid, right, step, threshold)
        invokeAll(left, right)
      }
    }
  }
}