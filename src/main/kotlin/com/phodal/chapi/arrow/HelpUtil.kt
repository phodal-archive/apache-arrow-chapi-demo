package com.phodal.chapi.arrow

import org.apache.arrow.flatbuf.Union
import org.apache.arrow.vector.types.DateUnit
import org.apache.arrow.vector.types.FloatingPointPrecision
import org.apache.arrow.vector.types.TimeUnit
import org.apache.arrow.vector.types.pojo.ArrowType
import org.apache.arrow.vector.types.pojo.Field
import org.apache.arrow.vector.types.pojo.FieldType
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow
import org.jetbrains.kotlinx.dataframe.columns.BaseColumn
import org.jetbrains.kotlinx.dataframe.columns.ColumnGroup
import org.jetbrains.kotlinx.dataframe.columns.FrameColumn
import org.jetbrains.kotlinx.dataframe.io.ConvertingMismatch
import org.jetbrains.kotlinx.dataframe.io.ignoreMismatchMessage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

class HelpUtil {
    fun toArrowField(
        col: BaseColumn<*>,
        mismatchSubscriber: (ConvertingMismatch) -> Unit = ignoreMismatchMessage
    ): Field {
        val column = col
        val columnType = column.type()
        val nullable = columnType.isMarkedNullable
        return when {
            columnType.isSubtypeOf(typeOf<String?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.Utf8(), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<Boolean?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.Bool(), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<Byte?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.Int(8, true), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<Short?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.Int(16, true), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<Int?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.Int(32, true), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<Long?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.Int(64, true), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<Float?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.FloatingPoint(FloatingPointPrecision.SINGLE), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<Double?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.FloatingPoint(FloatingPointPrecision.DOUBLE), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<LocalDate?>()) || columnType.isSubtypeOf(typeOf<kotlinx.datetime.LocalDate?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.Date(DateUnit.DAY), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<LocalDateTime?>()) || columnType.isSubtypeOf(typeOf<kotlinx.datetime.LocalDateTime?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.Date(DateUnit.MILLISECOND), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<LocalTime?>()) -> Field(
                column.name(),
                FieldType(nullable, ArrowType.Time(TimeUnit.NANOSECOND, 64), null),
                emptyList()
            )

            columnType.isSubtypeOf(typeOf<List<String>>()) -> {
                //           {
                //            "name": "Modifiers",
                //            "type": {
                //              "name": "Modifiers",
                //              "type": "list",
                //              "value_type": "string"
                //            }
                //          }
                val field = Field(null, FieldType(nullable, ArrowType.Utf8(), null), emptyList())
                Field(column.name(), FieldType(true, ArrowType.List(), null), listOf(field))
            }

            columnType.isSubtypeOf(typeOf<DataFrame<*>>()) -> {
                val fields: List<Field> = (column as FrameColumn<*>).values().flatMap {
                    it.columns().map { col -> toArrowField(col, mismatchSubscriber) }
                }

                Field(column.name(), FieldType(true, ArrowType.List(), null), fields)
            }

            columnType.isSubtypeOf(typeOf<DataRow<*>>()) -> {
                val fields = (column as ColumnGroup<*>).columns().map { col -> toArrowField(col, mismatchSubscriber) }
                Field(column.name(), FieldType(true, ArrowType.Struct(), null), fields)
            }

            else -> {
                println("Unsupported type: ${columnType}")
                mismatchSubscriber(ConvertingMismatch.SavedAsString(column.name(), column.javaClass))
                Field(column.name(), FieldType(true, ArrowType.Utf8(), null), emptyList())
            }
        }
    }
}