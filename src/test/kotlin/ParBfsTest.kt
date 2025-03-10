import CubeGraphCreator.createCube
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.ForkJoinPool

class ParBfsTest {
  @Test
  fun search() {
    val graph = arrayOf(
      arrayOf(1, 3),
      arrayOf(0, 2, 3, 4),
      arrayOf(1, 4),
      arrayOf(0, 1, 5),
      arrayOf(1, 2, 5),
      arrayOf(3, 4, 6),
      arrayOf(5),
      arrayOf(8),
      arrayOf(7)
    )
    val expected = arrayOf(0, 1, 2, 1, 2, 2, 3, -1, -1)
    val bfs = ParBfs(graph, pool = ForkJoinPool(4))

    val actual = bfs.search(0)

    assertThat(actual).containsExactly(expected)
  }

  @Test
  fun search_cube() {
    val graph = createCube(100)
    val seqBfs = SeqBfs(graph)
    val parBfs = ParBfs(graph)
    val expected = seqBfs.search(0)

    val actual = parBfs.search(0)

    assertThat(actual).containsExactly(expected.toTypedArray())
  }
}