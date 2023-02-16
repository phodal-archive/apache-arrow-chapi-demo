//@file:ImportDataSchema(
//    "CodeDataStruct",
//    "src/main/resources/0_codes.json"
//)
package com.phodal.chapi.arrow

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeExport
import chapi.domain.core.CodeField
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.readArrowFeather
import org.jetbrains.kotlinx.dataframe.io.writeArrowFeather
import java.io.File

private const val FILE_NAME = "0_codes.arrow"

fun main(args: Array<String>) {
    val codeDataStructs = listOf(
        CodeDataStruct(
            NodeName = "MainCli",
            Package = "com.phodal.chapi",
            Fields = arrayListOf(CodeField("name", "string", "test")),
            MultipleExtend = arrayListOf("Sample"),
            Extend = "Client",
            Exports = arrayListOf(CodeExport("test", "test"))
        )
    )

    // maxDepth = 1
    codeDataStructs.toDataFrame(maxDepth = 2).writeArrowFeather(File(FILE_NAME))
    val dataFrame = DataFrame
        .readArrowFeather(File(FILE_NAME))

    dataFrame
        .print(10)
}

private fun sampleWithArray() {
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
}
