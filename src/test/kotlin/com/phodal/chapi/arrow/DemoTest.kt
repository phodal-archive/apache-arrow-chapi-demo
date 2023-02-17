package com.phodal.chapi.arrow

import com.google.flatbuffers.FlatBufferBuilder
import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.complex.FixedSizeListVector
import org.apache.arrow.vector.complex.ListVector
import org.apache.arrow.vector.types.pojo.ArrowType
import org.apache.arrow.vector.types.pojo.FieldType
import org.apache.arrow.vector.types.pojo.Field;
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
}