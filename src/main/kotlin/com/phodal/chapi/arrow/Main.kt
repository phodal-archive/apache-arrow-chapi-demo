package com.phodal.chapi.arrow

import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.IntVector

fun main(args: Array<String>) {
    RootAllocator().use { allocator ->
        IntVector("fixed-size-primitive-layout", allocator).use { intVector ->
            intVector.allocateNew(3)
            intVector[0] = 1
            intVector.setNull(1)
            intVector[2] = 2
            intVector.valueCount = 3
            println("Vector created in memory: $intVector")
        }
    }
}