package com.phodal.chapi.arrow.core

import kotlinx.serialization.Serializable

@Serializable
data class CodeProject(
    var Modules: List<CodeModule> = listOf()
)