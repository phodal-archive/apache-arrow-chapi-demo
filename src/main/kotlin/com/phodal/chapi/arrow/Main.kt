package com.phodal.chapi.arrow

import chapi.domain.core.CodeDataStruct
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.io.readArrowFeather

private const val FILE_NAME = "0_codes.arrow"

fun main(args: Array<String>) {
    val dataFrame = DataFrame
        .readArrowFeather(FILE_NAME)
    dataFrame
        .print(10)

    println(dataFrame.schema())
}

data class SampleData(val name: String, val age: Int)

private fun createArrowFile() {
//    RootAllocator().use { allocator ->
//        val name = Field("name", FieldType.nullable(ArrowType.Utf8()), null)
//        val age = Field("age", FieldType.nullable(ArrowType.Int(32, true)), null)
//
//        val schemaPerson = Schema(listOf(name, age))
//
//        VectorSchemaRoot.create(schemaPerson, allocator).use { vectorSchemaRoot ->
//            val nameVector = vectorSchemaRoot.getVector("name") as VarCharVector
//            nameVector.allocateNew(3)
//            nameVector[0] = "David".toByteArray()
//            nameVector[1] = "Gladis".toByteArray()
//            nameVector[2] = "Juan".toByteArray()
//            val ageVector =
//                vectorSchemaRoot.getVector("age") as IntVector
//            ageVector.allocateNew(3)
//            ageVector[0] = 10
//            ageVector[1] = 20
//            ageVector[2] = 30
//            vectorSchemaRoot.rowCount = 3
//            val file = File(FILE_NAME)
//            try {
//                FileOutputStream(file).use { fileOutputStream ->
//                    ArrowFileWriter(vectorSchemaRoot, null, fileOutputStream.channel).use { writer ->
//                        writer.start()
//                        writer.writeBatch()
//                        writer.end()
//                        println("Record batches written: " + writer.recordBlocks.size + ". Number of rows written: " + vectorSchemaRoot.rowCount)
//                    }
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//    }
}