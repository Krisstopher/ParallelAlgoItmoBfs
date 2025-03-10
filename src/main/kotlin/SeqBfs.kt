import java.util.LinkedList

class SeqBfs(private val graph: Array<Array<Int>>) : Bfs {
  private val distances = IntArray(graph.size) { -1 }

  override fun search(start: Int): IntArray {
    val frontier = LinkedList<Int>().apply { add(start) }

    distances[start] = 0

    while (frontier.isNotEmpty()) {
      val current = frontier.pop()
      val currentDistance = distances[current]

      for (node in graph[current]) {
        if (distances[node] == -1) {
          frontier.add(node)
          distances[node] = currentDistance + 1
        }
      }
    }

    return distances
  }
}