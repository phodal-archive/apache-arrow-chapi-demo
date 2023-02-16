@file:ImportDataSchema(
    "CodeDataStruct",
    "src/main/resources/0_codes.json"
)

package com.phodal.chapi.arrow

import chapi.domain.core.CodeField
import chapi.domain.core.CodePosition
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.ImportDataSchema
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.readArrowFeather
import org.jetbrains.kotlinx.dataframe.io.writeArrowFeather
import java.io.File

private const val FILE_NAME = "0_codes.arrow"

fun main(args: Array<String>) {
    data class Name(val firstName: String, val lastName: String)
    data class Score(val subject: String, val value: Int)
    data class Student(val name: Name, val age: Int, val scores: Array<Score>)

    val students = listOf(
        Student(Name("Alice", "Cooper"), 15, arrayOf(Score("math", 4), Score("biology", 3))),
        Student(Name("Bob", "Marley"), 20, arrayOf(Score("music", 5)))
    )

    val df = students.toDataFrame(maxDepth = 1)
    df.writeArrowFeather(File(FILE_NAME))

    val dataFrame = DataFrame
        .readArrowFeather(FILE_NAME)
        .cast<Student>()

    dataFrame.print(10)


    listOf(
        chapi.domain.core.CodeDataStruct(
            NodeName = "test",
            Fields = arrayOf(CodeField("name", "string", "test")),
            Position = CodePosition(1, 2, 3, 4),
        )
    ).toDataFrame(maxDepth = 1)
        .writeArrowFeather(File(FILE_NAME))

    val chapiFrame = DataFrame
        .readArrowFeather(FILE_NAME)
        .cast<CodeDataStruct>()

    println(chapiFrame[0].Fields[0].TypeType)

    println(chapiFrame[0])
}
