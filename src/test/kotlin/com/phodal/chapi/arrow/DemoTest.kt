package com.phodal.chapi.arrow

import com.google.flatbuffers.FlatBufferBuilder
import kotlinx.serialization.descriptors.PrimitiveKind.*
import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.util.Collections2
import org.apache.arrow.vector.complex.FixedSizeListVector
import org.apache.arrow.vector.complex.ListVector
import org.apache.arrow.vector.types.FloatingPointPrecision.DOUBLE
import org.apache.arrow.vector.types.FloatingPointPrecision.SINGLE
import org.apache.arrow.vector.types.TimeUnit
import org.apache.arrow.vector.types.Types.MinorType
import org.apache.arrow.vector.types.UnionMode
import org.apache.arrow.vector.types.pojo.ArrowType
import org.apache.arrow.vector.types.pojo.ArrowType.FloatingPoint
import org.apache.arrow.vector.types.pojo.ArrowType.Struct
import org.apache.arrow.vector.types.pojo.ArrowType.Timestamp
import org.apache.arrow.vector.types.pojo.ArrowType.Union
import org.apache.arrow.vector.types.pojo.ArrowType.Utf8
import org.apache.arrow.vector.types.pojo.Field
import org.apache.arrow.vector.types.pojo.FieldType
import org.apache.arrow.vector.types.pojo.Schema
import org.junit.jupiter.api.Test

class DemoTest {
    @Test
    fun `json schema`() {
        val children: MutableList<Field> = ArrayList<Field>()
        RootAllocator(Long.MAX_VALUE).use { allocator ->
            ListVector.empty("list", allocator).use { writeVector ->
                FixedSizeListVector.empty("fixedlist", 5, allocator)
                    .use { writeFixedVector ->
                        val listVectorField: Field = writeVector.field
                        children.add(listVectorField)
                        val listFixedVectorField: Field = writeFixedVector.field
                        children.add(listFixedVectorField)
                    }
            }
        }

        val initialField = Field("a", FieldType.nullable(ArrowType.Struct.INSTANCE), children)
        val parent: MutableList<Field> = ArrayList<Field>()
        parent.add(initialField)
        val builder = FlatBufferBuilder()
        builder.finish(initialField.getField(builder))
        val flatBufField = org.apache.arrow.flatbuf.Field.getRootAsField(builder.dataBuffer())

        val initialSchema = Schema(parent)
        val jsonSchema: String = initialSchema.toJson()

        println(jsonSchema)
    }

    @Test
    fun `json schema 2`() {
        val children: MutableList<Field> = ArrayList()
        children.add(Field("child1", FieldType.nullable(Utf8.INSTANCE), null))
        children.add(Field("child2", FieldType.nullable(FloatingPoint(SINGLE)), emptyList<Field>()))
        children.add(
            Field(
                "child3", FieldType.nullable(Struct()), Collections2.asImmutableList<Field>(
                    Field("child3.1", FieldType.nullable(Utf8.INSTANCE), null),
                    Field("child3.2", FieldType.nullable(FloatingPoint(DOUBLE)), emptyList<Field>())
                )
            )
        )
        children.add(
            Field(
                "child4", FieldType.nullable(ArrowType.List()), Collections2.asImmutableList<Field>(
                    Field("child4.1", FieldType.nullable(Utf8.INSTANCE), null)
                )
            )
        )
        children.add(
            Field(
                "child5", FieldType.nullable(
                    Union(UnionMode.Sparse, intArrayOf(MinorType.TIMESTAMPMILLI.ordinal, MinorType.FLOAT8.ordinal))
                ),
                Collections2.asImmutableList<Field>(
                    Field("child5.1", FieldType.nullable(Timestamp(TimeUnit.MILLISECOND, null)), null),
                    Field("child5.2", FieldType.nullable(FloatingPoint(DOUBLE)), emptyList<Field>()),
                    Field("child5.3", FieldType.nullable(Timestamp(TimeUnit.MILLISECOND, "UTC")), null)
                )
            )
        )

        run(Schema(children))
    }

    private fun run(initialSchema: Schema) {
        val builder = FlatBufferBuilder()
        builder.finish(initialSchema.getSchema(builder))
        val flatBufSchema = org.apache.arrow.flatbuf.Schema.getRootAsSchema(builder.dataBuffer())
        val finalSchema = Schema.convertSchema(flatBufSchema)
        val jsonSchema: String = finalSchema.toJson()
        println(jsonSchema)
    }
}