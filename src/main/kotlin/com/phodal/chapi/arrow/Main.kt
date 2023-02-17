package com.phodal.chapi.arrow

import chapi.domain.core.CodeDataStruct
import org.apache.arrow.vector.types.pojo.Field
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.io.ignoreMismatchMessage
import org.jetbrains.kotlinx.dataframe.io.read
import java.io.File


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

//    val schema = Schema(List<Field>().k().apply {
//        add(Field.nullable("Source", ArrowType.Utf8()))
//        add(Field.nullable("AsName", ArrowType.Utf8()))
//        add(Field.nullable("UsageName", ArrowType.List(ArrowType.Utf8())))
//        add(Field.nullable("Scope", ArrowType.Utf8()))
//    }, null)

}

