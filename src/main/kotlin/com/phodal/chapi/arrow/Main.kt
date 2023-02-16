//@file:ImportDataSchema(
//    "CodeDataStruct",
//    "src/main/resources/0_codes.json"
//)
package com.phodal.chapi.arrow

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.readArrowFeather
import org.jetbrains.kotlinx.dataframe.io.writeArrowFeather
import java.io.File

private const val FILE_NAME = "0_codes.arrow"

fun main(args: Array<String>) {
    val dataString = File("0_codes.json").readText()
    val codeDataStructs: List<CodeDataStruct> = Json.decodeFromString(dataString)

    // maxDepth = 1
    codeDataStructs.toDataFrame()
        .writeArrowFeather(File(FILE_NAME))
    val dataFrame = DataFrame
        .readArrowFeather(File(FILE_NAME))

    dataFrame
        .print(10)
}
