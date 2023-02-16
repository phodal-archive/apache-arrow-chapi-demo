package com.phodal.chapi.arrow.core

import chapi.domain.core.CodeCall
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

enum class FunctionType {
    Function,
    // for Golang block
    Block,
}

@Serializable
data class CodeFunction(
    var Name: String = "",
    var FilePath: String = "",
    var Package: String = "",
    var ReturnType: String = "",
    var MultipleReturns: ArrayList<CodeProperty> = arrayListOf(),
    var Parameters: ArrayList<CodeProperty> = arrayListOf(),
    var FunctionCalls: ArrayList<CodeCall> = arrayListOf(),
    var Annotations: ArrayList<CodeAnnotation> = arrayListOf(),
    var Override: Boolean = false,
    var Modifiers: ArrayList<String> = arrayListOf(),
    // for example, Java can have Inner Class
    var InnerStructures: ArrayList<CodeDataStruct> = arrayListOf(),
    // for lambda or anonymous function inside function.
    var InnerFunctions: ArrayList<CodeFunction> = arrayListOf(),
    var Position: CodePosition = CodePosition(),
    var Extension: JsonElement = JsonObject(HashMap()),
    var LocalVariables: ArrayList<CodeProperty> = arrayListOf(),
    var IsConstructor: Boolean = false, // todo: move to extension
    var IsReturnHtml: Boolean = false,
    var BodyHash: Int = 0,
    var Type: FunctionType = FunctionType.Function,
    // a experimental api for code analysis, please carefully use it.
//    @property:ExperimentalStdlibApi val expression: Expression? = null,
) {
    private var extensionMap = HashMap<String, JsonElement>()

    @Deprecated(
        "is for Java/Kotlin Only",
        ReplaceWith("this.ReturnType == \"String\" || this.ReturnType == \"int\" || this.ReturnType == \"float\" || this.ReturnType == \"void\" || this.ReturnType == \"char\" || this.ReturnType == \"double\"")
    )
    fun isJavaLangReturnType(): Boolean {
        return this.ReturnType == "String" || this.ReturnType == "int" || this.ReturnType == "float" || this.ReturnType == "void" || this.ReturnType == "char" || this.ReturnType == "double"
    }

    @Deprecated("is for Java/Kotlin Only", ReplaceWith("this.Modifiers.contains(\"static\")"))
    fun isStatic(): Boolean {
        return this.Modifiers.contains("static")
    }

    @Deprecated(
        "is for Java/Kotlin Only",
        ReplaceWith("this.Name.startsWith(\"set\") || this.Name.startsWith(\"get\")")
    )
    fun isGetterSetter(): Boolean {
        return this.Name.startsWith("set") || this.Name.startsWith("get")
    }

    fun buildFullMethodName(node: CodeDataStruct): String {
        return "${node.Package}.${node.NodeName}.${this.Name}"
    }

    fun getAllCallString(): List<String> {
        return FunctionCalls
            .filter { it.NodeName != "" }
            .map { it.buildClassFullName() }
    }

    fun isJUnitTest(): Boolean {
        return Annotations.any { it.isIgnoreOrTest() }
    }

    fun isOverride(): Boolean {
        return this.Annotations.any { it.isOverride() }
    }

    fun addExtension(key: String, value: String) {
        this.extensionMap[key] = JsonPrimitive(value)
        this.Extension = JsonObject(this.extensionMap)
    }

    @Deprecated(
        "is for Java/Kotlin Only", ReplaceWith(
            "this.Extension.jsonObject[\"IsReturnNull\"] == JsonPrimitive(\"true\")",
            "kotlinx.serialization.json.jsonObject",
            "kotlinx.serialization.json.JsonPrimitive"
        )
    )
    fun isReturnNull(): Boolean {
        return this.Extension.jsonObject["IsReturnNull"] == JsonPrimitive("true")
    }

    fun addVarsFromMap(localVars: MutableMap<String, String>) {
        this.LocalVariables = localVars.map { entry ->
            CodeProperty(TypeValue = entry.key, TypeType = entry.value)
        }.toCollection(ArrayList())
    }

    fun fileExt(): String {
        return this.FilePath.substringAfterLast('.', "")
    }

    fun fileName(): String {
        return this.FilePath.substringBeforeLast('.', "")
    }
}
