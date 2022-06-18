package nstv.canvasExtensions

fun <E> Collection<E>.nextIndexLoop(currentIndex: Int): Int =
    if (currentIndex + 1 >= this.size) 0 else currentIndex + 1

fun <E> List<E>.nextItemLoop(currentIndex: Int): E = this[nextIndexLoop(currentIndex)]

fun <E> Map<String, E>.nextItemLoop(currentIndex: Int): Pair<String, E> {
    val nextIndex: Int = this.keys.nextIndexLoop(currentIndex)
    return this.keys.elementAt(nextIndex) to this.values.elementAt(nextIndex)
}
