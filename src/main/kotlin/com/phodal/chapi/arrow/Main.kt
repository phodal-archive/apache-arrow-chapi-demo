package com.phodal.chapi.arrow

import chapi.domain.core.CodeField
import chapi.domain.core.CodePosition
import org.jetbrains.kotlinx.dataframe.DataFrame
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
            Fields = arrayOf(CodeField("name", "string", "test")),
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
