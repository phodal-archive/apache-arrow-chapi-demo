package com.phodal.chapi.arrow

import org.apache.arrow.vector.types.pojo.Field
import org.apache.arrow.vector.types.pojo.FieldType
import org.jetbrains.kotlinx.dataframe.AnyCol
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.io.ConvertingMismatch
import org.jetbrains.kotlinx.dataframe.io.ignoreMismatchMessage
import org.jetbrains.kotlinx.dataframe.io.read
import org.jetbrains.kotlinx.dataframe.io.toArrowSchema
import org.jetbrains.kotlinx.dataframe.typeClass
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.xml.validation.Schema
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

private const val FILE_NAME = "0_codes.arrow"

fun main(args: Array<String>) {

//    val ds = File("data/0_codes.json").readText()
//    val dataStructs = Json.decodeFromString<List<CodeDataStruct>>(ds)
//    val dataFrame = dataStructs.toDataFrame()

//     for remote will be: https://raw.githubusercontent.com/phodal-archive/apache-arrow-chapi-demo/master/data/0_codes.json
    val dataFrame = DataFrame.read("https://raw.githubusercontent.com/phodal-archive/apache-arrow-chapi-demo/master/data/0_codes.json")
//    dataFrame.schema().print()

    val fields: List<Field> = dataFrame.columns().map {
        val col = it.toArrowField(ignoreMismatchMessage)
        println(col)
        col
    }

    val toArrowSchema = org.apache.arrow.vector.types.pojo.Schema(fields)
    File(FILE_NAME).writeText(toArrowSchema.toJson())
//
//    dataFrame.writeArrowFeather(File("codes.arrow"))
}

public fun AnyCol.toArrowField(mismatchSubscriber: (ConvertingMismatch) -> Unit = ignoreMismatchMessage): Field {
    val column = this
    val columnType = column.type()
    val nullable = columnType.isMarkedNullable
    return when {
        columnType.isSubtypeOf(typeOf<String?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.Utf8(), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<Boolean?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.Bool(), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<Byte?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.Int(8, true), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<Short?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.Int(16, true), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<Int?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.Int(32, true), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<Long?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.Int(64, true), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<Float?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.FloatingPoint(org.apache.arrow.vector.types.FloatingPointPrecision.SINGLE), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<Double?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.FloatingPoint(org.apache.arrow.vector.types.FloatingPointPrecision.DOUBLE), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<LocalDate?>()) || columnType.isSubtypeOf(typeOf<kotlinx.datetime.LocalDate?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.Date(org.apache.arrow.vector.types.DateUnit.DAY), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<LocalDateTime?>()) || columnType.isSubtypeOf(typeOf<kotlinx.datetime.LocalDateTime?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.Date(org.apache.arrow.vector.types.DateUnit.MILLISECOND), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<LocalTime?>()) -> Field(
            column.name(),
            FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.Time(org.apache.arrow.vector.types.TimeUnit.NANOSECOND, 64), null),
            emptyList()
        )

        columnType.isSubtypeOf(typeOf<DataFrame<*>?>()) -> {
            println("Unsupported type: ${columnType}")
            Field(
                column.name(),
                FieldType(nullable, org.apache.arrow.vector.types.pojo.ArrowType.Utf8(), null),
                emptyList()
            )
        }

        else -> {
            println("Unsupported type: ${columnType}")
            mismatchSubscriber(ConvertingMismatch.SavedAsString(column.name(), column.typeClass.java))
            Field(column.name(), FieldType(true, org.apache.arrow.vector.types.pojo.ArrowType.Utf8(), null), emptyList())
        }
    }
}
