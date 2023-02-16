package com.phodal.chapi.arrow.core

import kotlinx.serialization.Serializable

@Serializable
data class CodeImport(
    var Source: String = "",
    // todo: define for new usage
    var AsName: String = "",
    // import UsageName from 'usage'
    // import AsSource as UsageName from 'source'
    var UsageName: ArrayList<String> = arrayListOf(),
    var Scope: String = ""
)