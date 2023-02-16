package com.phodal.chapi.arrow.core

import kotlinx.serialization.Serializable

@Serializable
data class CodeDependency(
    var GroupId: String = "",
    var ArtifactId: String = "",
    var Scope: String = "",
    var Type: String = "",
    var Version: String = "",
    var Optional: Boolean = false
)
