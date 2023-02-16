package com.phodal.chapi.arrow

import chapi.domain.core.CodeDataStruct
import org.apache.arrow.vector.VectorSchemaRoot
import org.apache.arrow.vector.ipc.ArrowFileWriter
import org.apache.arrow.vector.types.pojo.Field
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.io.arrowWriter
import org.jetbrains.kotlinx.dataframe.io.ignoreMismatchMessage
import org.jetbrains.kotlinx.dataframe.io.read
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun main(args: Array<String>) {
    val dataFrame =
        DataFrame.read("data/one_data.json")
            .cast<CodeDataStruct>()
//
    val fields: List<Field> = dataFrame.columns().map {
        val col = HelpUtil().toArrowField(it, ignoreMismatchMessage)
        col
    }

    val toArrowSchema = org.apache.arrow.vector.types.pojo.Schema(fields)
    File("schema.json").writeText(toArrowSchema.toJson())

//    dataFrame.arrowWriter(toArrowSchema).writeArrowFeather(File("codes.arrow"))
//    dataFrame.writeArrowFeather(File("codes.arrow"))

//    dataFrame.arrowWriter(toArrowSchema).allocateVectorSchemaRoot().use {
//        val writer = ArrowFileWriter(it, null, FileOutputStream("codes.arrow").channel)
//        writer.start()
//        writer.writeBatch()
//        writer.end()
//    }
//
//    val frame = DataFrame.read("codes.arrow")
//    frame.print(1)
}

