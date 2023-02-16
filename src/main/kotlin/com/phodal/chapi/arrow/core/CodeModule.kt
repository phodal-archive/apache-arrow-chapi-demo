package com.phodal.chapi.arrow.core

import kotlinx.serialization.Serializable

@Serializable
data class CodeModule (
    var Packages : List<CodePackage> = listOf(),
    var packageInfo : CodePackageInfo = CodePackageInfo()
)
