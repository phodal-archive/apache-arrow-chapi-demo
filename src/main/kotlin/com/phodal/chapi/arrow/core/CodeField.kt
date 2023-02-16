package com.phodal.chapi.arrow.core

import chapi.domain.core.CodeCall
import kotlinx.serialization.Serializable

@Serializable
data class CodeField(
    var TypeType: String = "",
    var TypeValue: String = "",
    var TypeKey: String = "",
    var Annotations: ArrayList<CodeAnnotation> = arrayListOf(),
    var Modifiers: ArrayList<String> = arrayListOf(),
    // for TypeScript and JavaScript only, examples: `export default sample = createHello() `
    var Calls: ArrayList<CodeCall> = arrayListOf()
)
