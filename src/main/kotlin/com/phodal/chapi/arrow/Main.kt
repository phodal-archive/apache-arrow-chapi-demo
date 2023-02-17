package com.phodal.chapi.arrow

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import chapi.domain.core.DataStructType
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.VectorSchemaRoot
import org.apache.arrow.vector.ipc.ArrowFileReader
import org.apache.arrow.vector.ipc.ArrowFileWriter
import org.apache.arrow.vector.types.pojo.ArrowType
import org.apache.arrow.vector.types.pojo.Field
import org.apache.arrow.vector.types.pojo.FieldType
import org.apache.arrow.vector.types.pojo.Schema
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.arrowWriter
import org.jetbrains.kotlinx.dataframe.io.ignoreMismatchMessage
import org.jetbrains.kotlinx.dataframe.io.read
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


@DataSchema
data class SinpleDS(
    // class and DataStruct Name
    // for TypeScript/JavaScript, if is a variable, function, it will be naming to `default`
    var NodeName: String = "",
    var Module: String = "",
    var Type: DataStructType = DataStructType.EMPTY,
    var Package: String = "",
    var FilePath: String = "",
    // todo: thinking in change to property
    var Fields: String = "",
    var MultipleExtend: List<String> = listOf(),
    var Implements: List<String> = listOf(),
    var Extend: String = "",
    var Functions: String = "",
    var InnerStructures: String = "",
    var Annotations: String = "",
    var FunctionCalls: String = "",
    var Parameters: String = "",
    var Imports: String = "",
    var Exports: String = "",
    var Extension: JsonElement = JsonObject(HashMap()),
    var Position: String = ""
)

fun main(args: Array<String>) {
    val ds = listOf(
        CodeDataStruct(
            NodeName = "Main",
            Functions = listOf(CodeFunction(Name = "main")),
        )
    )

    val dataframe = ds.toDataFrame()

    val schema = Schema.fromJSON(File("schema.json").readText())

    val root = dataframe.arrowWriter(schema).allocateVectorSchemaRoot()

    val file = File("randon_access_to_file.arrow")
    try {
        FileOutputStream(file).use { fileOutputStream ->
            ArrowFileWriter(root, null, fileOutputStream.channel)
                .use { writer ->
                    writer.start()
                    writer.writeBatch()
                    writer.end()
                    println("Record batches written: " + writer.recordBlocks.size + ". Number of rows written: " + root.rowCount)
                }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

private fun parserPythonOutput() {
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

                    val schema = reader.vectorSchemaRoot.schema.toJson()
                    File("schema.json").writeText(schema)
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

private fun convertType() {
    val dataFrame =
        DataFrame.read("data/one_data.json")
            .cast<CodeDataStruct>()

    dataFrame.schema().print()

    val fields: List<Field> = dataFrame.columns().map {
        val col = HelpUtil().toArrowField(it, ignoreMismatchMessage)
        col
    }

    val toArrowSchema = Schema(fields)
    File("schema.json").writeText(toArrowSchema.toJson())
}