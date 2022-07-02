package nstv.canvasExtensions

fun Int.nextIndexLoop(currentIndex: Int): Int =
    if (currentIndex + 1 >= this) 0 else currentIndex + 1

fun <E> Collection<E>.nextIndexLoop(currentIndex: Int): Int = this.size.nextIndexLoop(currentIndex)

fun <E> List<E>.nextItemLoop(currentIndex: Int): E = this[nextIndexLoop(currentIndex)]

fun <E> Map<String, E>.nextItemLoop(currentIndex: Int): Pair<String, E> {
    val nextIndex: Int = this.keys.nextIndexLoop(currentIndex)
    return this.keys.elementAt(nextIndex) to this.values.elementAt(nextIndex)
}

fun <E> Array<E>.nextItemLoop(currentIndex: Int): E = this[this.size.nextIndexLoop(currentIndex)]

fun <E> Array<E>.nextItemLoop(currentItem: E): E =
    this[this.size.nextIndexLoop(this.indexOf(currentItem))]
