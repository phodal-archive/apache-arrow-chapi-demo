package com.phodal.chapi.arrow.core

import kotlinx.serialization.Serializable

@Serializable
data class CodeModule (
    var Packages : ArrayList<CodePackage> = arrayListOf(),
    var packageInfo : CodePackageInfo = CodePackageInfo()
)
