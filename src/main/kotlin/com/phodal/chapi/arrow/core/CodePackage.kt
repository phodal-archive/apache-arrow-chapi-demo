package com.phodal.chapi.arrow.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class CodePackage(
    var Name: String = "",
    var ID: String = "",
    var codeContainers: ArrayList<CodeContainer> = arrayListOf(),
    var Packages : ArrayList<CodePackage> = arrayListOf(),

    var Extension: JsonElement = JsonObject(HashMap())
)