package com.phodal.chapi.arrow

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.json.Json
import org.jetbrains.kotlinx.dataframe.api.print
import java.io.File
import kotlinx.serialization.decodeFromString
import org.apache.arrow.vector.ipc.ArrowWriter
import org.apache.arrow.vector.types.pojo.Schema
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.arrowWriter
import org.jetbrains.kotlinx.dataframe.io.toArrowSchema
import org.jetbrains.kotlinx.dataframe.io.writeMismatchMessage

private const val FILE_NAME = "0_codes.arrow"

fun main(args: Array<String>) {
    // for remote will be: https://raw.githubusercontent.com/phodal-archive/apache-arrow-chapi-demo/master/data/0_codes.json
//    val dataFrame = DataFrame.read("data/0_codes.json")

    val ds = File("data/0_codes.json").readText()
    val dataStructs = Json.decodeFromString<List<CodeDataStruct>>(ds)
    val dataFrame = dataStructs.toDataFrame(maxDepth = 1)

    val toArrowSchema = dataFrame.columns().toArrowSchema()
    File("schema.json").writeText(toArrowSchema.toJson())

//    dataFrame.print(10)
//    dataFrame.arrowWriter(
//        dataFrame.columns().toArrowSchema(),
//        mode = org.jetbrains.kotlinx.dataframe.io.ArrowWriter.Mode(
//            restrictWidening = true,
//            restrictNarrowing = true,
//            strictType = true,
//            strictNullable = false,
//        ),
//
//        // Specify mismatch subscriber
//        mismatchSubscriber = writeMismatchMessage,
//    ).use { writer ->
//        writer.writeArrowFeather(File(FILE_NAME))
//    }

//    val byteArray = dataFrame.writeArrowFeather()
//    File(FILE_NAME).writeBytes(byteArray)
}
