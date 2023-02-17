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

