package com.phodal.chapi.arrow

import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.IntVector
import org.apache.arrow.vector.VarCharVector
import org.apache.arrow.vector.VectorSchemaRoot
import org.apache.arrow.vector.ipc.ArrowFileWriter
import org.apache.arrow.vector.types.pojo.ArrowType
import org.apache.arrow.vector.types.pojo.Field
import org.apache.arrow.vector.types.pojo.FieldType
import org.apache.arrow.vector.types.pojo.Schema
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Arrays.asList


fun main(args: Array<String>) {

    RootAllocator().use { allocator ->
        val name = Field("name", FieldType.nullable(ArrowType.Utf8()), null)
        val age = Field("age", FieldType.nullable(ArrowType.Int(32, true)), null)

        val schemaPerson = Schema(listOf(name, age))

        VectorSchemaRoot.create(schemaPerson, allocator).use { vectorSchemaRoot ->
            val nameVector = vectorSchemaRoot.getVector("name") as VarCharVector
            nameVector.allocateNew(3)
            nameVector[0] = "David".toByteArray()
            nameVector[1] = "Gladis".toByteArray()
            nameVector[2] = "Juan".toByteArray()
            val ageVector =
                vectorSchemaRoot.getVector("age") as IntVector
            ageVector.allocateNew(3)
            ageVector[0] = 10
            ageVector[1] = 20
            ageVector[2] = 30
            vectorSchemaRoot.rowCount = 3
            val file = File("randon_access_to_file.arrow")
            try {
                FileOutputStream(file).use { fileOutputStream ->
                    ArrowFileWriter(vectorSchemaRoot, null, fileOutputStream.channel).use { writer ->
                        writer.start()
                        writer.writeBatch()
                        writer.end()
                        println("Record batches written: " + writer.recordBlocks.size + ". Number of rows written: " + vectorSchemaRoot.rowCount)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}