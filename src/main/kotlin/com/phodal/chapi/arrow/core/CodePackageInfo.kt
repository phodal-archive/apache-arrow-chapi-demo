package com.phodal.chapi.arrow.core

import kotlinx.serialization.Serializable

@Serializable
data class CodePackageInfo(
    var ProjectName: String = "",
    var Dependencies: ArrayList<CodeDependency> = arrayListOf()
)