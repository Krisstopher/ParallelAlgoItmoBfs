object CubeGraphCreator {
  fun createCube(edgeSize: Int): Array<Array<Int>> {
    val squaredEdgeSize = edgeSize * edgeSize
    val nodesNumber = squaredEdgeSize * edgeSize

    return Array(nodesNumber) {
      buildList {
        if (it % edgeSize != 0) add(it - 1)
        if ((it + 1) % edgeSize != 0) add(it + 1)
        if (it >= squaredEdgeSize) add(it - squaredEdgeSize)
        if (it + squaredEdgeSize < nodesNumber) add(it + squaredEdgeSize)
        if ((it % squaredEdgeSize) - edgeSize >= 0) add(it - edgeSize)
        if ((it % squaredEdgeSize) + edgeSize < squaredEdgeSize) add(it + edgeSize)
      }.sorted().toTypedArray()
    }
  }
}