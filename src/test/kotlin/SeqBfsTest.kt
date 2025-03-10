import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeqBfsTest {
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
    val expectedDistances = arrayOf(0, 1, 2, 1, 2, 2, 3, -1, -1)
    val bfs = SeqBfs(graph)

    val actual = bfs.search(0)

    assertThat(actual).containsExactly(expectedDistances)
  }
}