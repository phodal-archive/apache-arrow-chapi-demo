package com.phodal.chapi.arrow

import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.VectorSchemaRoot
import org.apache.arrow.vector.ipc.ArrowFileReader
import org.apache.arrow.vector.types.pojo.ArrowType
import org.apache.arrow.vector.types.pojo.Field
import org.apache.arrow.vector.types.pojo.FieldType
import org.apache.arrow.vector.types.pojo.Schema
import java.io.File
import java.io.FileInputStream
import java.io.IOException


fun main(args: Array<String>) {
    val file = File("jupyter/output.arrow")
    try {
        RootAllocator().use { rootAllocator ->
            FileInputStream(file).use { fileInputStream ->
                ArrowFileReader(fileInputStream.channel, rootAllocator).use { reader ->
                    println("Record batches in file: " + reader.recordBlocks.size)
                    for (arrowBlock in reader.recordBlocks) {
                        reader.loadRecordBatch(arrowBlock)
                        val vectorSchemaRootRecover = reader.vectorSchemaRoot
                        print(vectorSchemaRootRecover.contentToTSVString())
                    }
                }
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

private fun byField() {
    val name = Field("name", FieldType(true, ArrowType.Utf8(), null), emptyList())
    val age = Field("age", FieldType(true, ArrowType.Int(32, true), null), emptyList())
    val schema = Schema(listOf(name, age))
    println(schema.toJson())

    //  Json: { "list": [1, 2, 3] } to Arrow
    val childField = Field("", FieldType(true, ArrowType.Int(32, true), null), null)
    val example2 = Field("list", FieldType(true, ArrowType.List(), null), listOf(childField))
    val schema2 = Schema(listOf(example2))
    println(schema2.toJson())

    try {
        RootAllocator().use { allocator ->
            VectorSchemaRoot.create(schema2, allocator).use { root ->

            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

