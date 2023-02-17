package com.phodal.chapi.arrow

import chapi.domain.core.CodeDataStruct
import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.VarCharVector
import org.apache.arrow.vector.VectorSchemaRoot
import org.apache.arrow.vector.ipc.ArrowFileWriter
import org.apache.arrow.vector.types.pojo.ArrowType
import org.apache.arrow.vector.types.pojo.Field
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.io.ignoreMismatchMessage
import org.jetbrains.kotlinx.dataframe.io.read
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun main(args: Array<String>) {
    val dataFrame =
        DataFrame.read("data/one_data.json")
            .cast<CodeDataStruct>()

    val fields: List<Field> = dataFrame.columns().map {
        val col = HelpUtil().toArrowField(it, ignoreMismatchMessage)
        col
    }

    val toArrowSchema = org.apache.arrow.vector.types.pojo.Schema(fields)
    File("schema.json").writeText(toArrowSchema.toJson())

    RootAllocator().use { allocator ->
        VectorSchemaRoot.create(toArrowSchema, allocator).use { vectorSchemaRoot ->
            val file = File("randon_access_to_file.arrow")
            try {
                FileOutputStream(file).use { fileOutputStream ->
                    ArrowFileWriter(vectorSchemaRoot, null, fileOutputStream.channel)
                        .use { writer ->
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

