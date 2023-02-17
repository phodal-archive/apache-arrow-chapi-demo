## ChatGPT

```json
{
  "NodeName": "CSharpApiAnalyser",
  "Module": "root:feat_apicalls",
  "Type": "CLASS",
  "Package": "org.archguard.scanner.analyser.backend",
  "FilePath": "feat_apicalls/src/main/kotlin/org/archguard/scanner/analyser/backend/CSharpApiAnalyser.kt",
  "Fields": [
    {
      "TypeType": "kotlin.List<ContainerSupply>",
      "TypeValue": "listOf()",
      "TypeKey": "resources",
      "Modifiers": [
        "override",
        "var"
      ]
    }
  ],
  "Implements": [
    "org.archguard.scanner.analyser.base.ApiAnalyser"
  ],
  "Functions": [
    {
      "Name": "analysisByNode",
      "Package": "org.archguard.scanner.analyser.backend",
      "ReturnType": "kotlin.Unit",
      "Parameters": [
        {
          "TypeValue": "node",
          "TypeType": "chapi.domain.core.CodeDataStruct"
        }
      ],
      "FunctionCalls": [
        {
          "NodeName": "node",
          "FunctionName": "filterAnnotations",
          "Parameters": [
            {
              "TypeValue": "RoutePrefix",
              "TypeType": "kotlin.String"
            },
            {
              "TypeValue": "Route",
              "TypeType": "kotlin.String"
            }
          ],
          "Position": {
            "StartLine": 13,
            "StartLinePosition": 30,
            "StopLine": 13,
            "StopLinePosition": 75
          }
        }
      ],
      "Position": {
        "StartLine": 23,
        "StartLinePosition": 4,
        "StopLine": 52,
        "StopLinePosition": 4
      },
      "LocalVariables": [
        {
          "TypeValue": "httpMethod",
          "TypeType": ""
        },
        {
          "TypeValue": "route",
          "TypeType": ""
        }
      ]
    }
  ],
  "Imports": [
    {
      "Source": "chapi.domain.core.CodeDataStruct",
      "AsName": "CodeDataStruct"
    }
  ],
  "Position": {
    "StartLine": 9,
    "StopLine": 57
  }
}
```



```
{
  "name": "Modifiers",
  "type": {
    "name": "Modifiers",
    "type": "list",
    "value_type": "string"
  }
}
```

这个 JSON 对象可以转换为以下的 Apache Arrow 的 FieldType：

```kotlin
import org.apache.arrow.vector.types.pojo.ArrowType
import org.apache.arrow.vector.types.pojo.Field
import org.apache.arrow.vector.types.pojo.ListType

val modifiersField = Field(
        "Modifiers",
        ListType(true, ArrowType.Utf8()),
        null
        )
```

其中，"name" 字段的类型是 Utf8，"type" 字段的类型是 List，"value_type" 字段的类型是 Utf8。因为 "type" 字段是一个列表类型，
所以需要使用 ArrowType.List() 来表示。最后，将三个字段的信息组成一个 Struct 类型，就得到了整个 Modifiers 类型的 ArrowType。

## Tools

sample: https://github.com/domoritz/arrow-tools/tree/main

```
json2arrow  one_data.json one.schema -p >> by_tools.schema
```