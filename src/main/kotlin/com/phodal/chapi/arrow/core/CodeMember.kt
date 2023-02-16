package com.phodal.chapi.arrow.core

import kotlinx.serialization.Serializable

@Serializable
data class CodeMember(
    var ID: String = "",
    var FileID: String = "",
    var DataStructID: String = "",
    var AliasPackage: String = "",
    var Name: String = "",
    var Type: String = "",
    var StructureNodes: ArrayList<CodeDataStruct> = arrayListOf(),
    var FunctionNodes: ArrayList<CodeFunction> = arrayListOf(),
    var Namespace: ArrayList<String> = arrayListOf(),
    var Position: CodePosition = CodePosition()
) {
    fun buildMemberId() {
        val isDefaultFunction = this.DataStructID == "default"
        when {
            isDefaultFunction -> {
                for (node in this.FunctionNodes) {
                    this.ID = this.AliasPackage + ":" + node.Name
                }
            }
            else -> {
                var packageName = this.FileID
                if (this.FileID != this.AliasPackage) {
                    packageName = this.FileID + "|" + this.AliasPackage
                }
                if (this.FileID == "" && this.AliasPackage != "") {
                    packageName = this.AliasPackage
                }

                this.ID = packageName + "::" + this.DataStructID
            }
        }
    }
}
