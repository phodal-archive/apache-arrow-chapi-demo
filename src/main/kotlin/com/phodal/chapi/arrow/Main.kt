@file:ImportDataSchema(
    "CodeDataStruct",
    "src/main/resources/0_codes.json",
)

package com.phodal.chapi.arrow

import chapi.domain.core.CodePosition
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.ImportDataSchema
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.readArrowFeather
import org.jetbrains.kotlinx.dataframe.io.writeArrowFeather
import java.io.File

private const val FILE_NAME = "0_codes.arrow"

fun main(args: Array<String>) {
    listOf(
        chapi.domain.core.CodeDataStruct(
            NodeName = "test",
            Position = CodePosition(1, 2, 3, 4),
        )
    ).toDataFrame()
        .writeArrowFeather(File(FILE_NAME))


    val dataFrame = DataFrame
        .readArrowFeather(FILE_NAME)
        .cast<chapi.domain.core.CodeDataStruct>()

    dataFrame
        .print(10)

    println(dataFrame[0])
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