package com.phodal.chapi.arrow

import org.apache.arrow.vector.types.pojo.Field
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.io.ignoreMismatchMessage
import org.jetbrains.kotlinx.dataframe.io.read
import java.io.File

private const val FILE_NAME = "0_codes.arrow"

fun main(args: Array<String>) {
    val dataFrame =
        DataFrame.read("data/0_codes.json")
//
    val fields: List<Field> = dataFrame.columns().map {
        val col = HelpUtil().toArrowField(it, ignoreMismatchMessage)
        col
    }

    val toArrowSchema = org.apache.arrow.vector.types.pojo.Schema(fields)
    File("schema.json").writeText(toArrowSchema.toJson())

//    dataFrame.writeArrowFeather(File("codes.arrow"))
}

