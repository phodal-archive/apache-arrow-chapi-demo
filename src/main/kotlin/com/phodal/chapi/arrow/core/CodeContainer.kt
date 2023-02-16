package com.phodal.chapi.arrow.core

import kotlinx.serialization.Serializable

@Serializable
data class CodeContainer(
    var FullName: String = "",
    var PackageName: String = "",
    var Imports: ArrayList<CodeImport> = arrayListOf(),
    var Members: ArrayList<CodeMember> = arrayListOf(),
    var DataStructures: ArrayList<CodeDataStruct> = arrayListOf(),
    var Fields: ArrayList<CodeField> = arrayListOf(),
    var Containers: ArrayList<CodeContainer> = arrayListOf()
)