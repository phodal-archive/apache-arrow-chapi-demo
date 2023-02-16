package com.phodal.chapi.arrow.core

import kotlinx.serialization.Serializable

@Serializable
data class CodeProperty(
    var Modifiers: ArrayList<String> = arrayListOf(),
    var DefaultValue: String = "",
    var TypeValue: String,
    var TypeType: String,
    var Annotations: ArrayList<CodeAnnotation> = arrayListOf(),
    // for TypeScript and Parameter
    var ObjectValue: ArrayList<CodeProperty> = arrayListOf(),
    var ReturnTypes: ArrayList<CodeProperty> = arrayListOf(),
    var Parameters: ArrayList<CodeProperty> = arrayListOf(),
)