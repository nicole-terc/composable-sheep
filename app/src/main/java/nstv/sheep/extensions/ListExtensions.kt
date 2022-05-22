package nstv.sheep.extensions

fun Collection<Any>.nextIndexLoop(currentIndex: Int): Int =
    if (currentIndex + 1 >= this.size) 0 else currentIndex + 1
