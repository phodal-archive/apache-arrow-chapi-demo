# ArchGuard 中大数据探索：Kotlin Dataframe 与 Apache Arrow 的尝试  

## 为什么 Dataframe Arrow 是可接受的

Dataframe Arrow 是一个基于 Apache Arrow 的 Kotlin Dataframe 实现，它的目标是提供一个高性能的数据处理框架，同时保持与 Apache Arrow 的兼容性。

Spike for package size:

- Apache Arrow with Chapi: 14M
- Jetbrains Dataframe with Chapi: 35M
- Jetbrains Dataframe Arrow with Chapi: 19M

Jetbrains Dataframe Arrow depends on Apache Arrow, so it is not surprising that the size is the same as Jetbrains Dataframe.

## Apache Arrow Schema 与 FlatBuffer Schema

Apache Arrow Schema 与 FlatBuffer Schema 是两种不同的 Schema 定义方式，它们的区别在于：

- Apache Arrow Schema 是？？ 
- FlatBuffer Schema 是？？

Arrow 使用的是 JSON，而 FlatBuffer 使用的是 FlatBuffer Schema，自定义的格式 。

先看个示例，我们有一个待序列化的 Kotlin 类：

```kotlin
@Serializable
data class CodePosition(
    var StartLine: Int = 0,
    var StartLinePosition: Int = 0,
    var StopLine: Int = 0,
    var StopLinePosition: Int = 0
)
```

对比一下差异：

### FlatBuffer Schema 示例

转换成 FlatBuffer Schema 之后：

```flatbuffer
table Position {
  StartLine: int;
  StartLinePosition: int;
  StopLine: int;
  StopLinePosition: int;
}
```

### Apache Arrow Schema 示例：

JSON Schema 示例：

```json
 {
  "name": "Position",
  "type": {
    "name": "Position",
    "type": "struct",
    "fields": [
      {
        "name": "StartLine",
        "type": "int"
      },
      {
        "name": "StartLinePosition",
        "type": "int"
      },
      {
        "name": "StopLine",
        "type": "int"
      },
      {
        "name": "StopLinePosition",
        "type": "int"
      }
    ]
  }
}
```

代码示例：

```kotlin
fun main() {
    val rootAllocator = RootAllocator(Long.MAX_VALUE)
    val byteBuddyPool = ByteBuddyPool()

    val schema = Schema(List<Field>().k().apply {
        add(Field.nullable("StartLine", ArrowType.Int(32, true)))
        add(Field.nullable("StartLinePosition", ArrowType.Int(32, true)))
        add(Field.nullable("StopLine", ArrowType.Int(32, true)))
        add(Field.nullable("StopLinePosition", ArrowType.Int(32, true)))
    }, null)

    // Use the schema as needed
    println(schema)
}
```

## 在 Kotlin 中使用 Dataframe 

### 1. 定义 Dataframe Schema

Method 1: by Class Schema 

```kotlin
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema

@DataSchema
interface Person {
    val name: String
    val age: Int
}
```

Method 2: Can be used in Annotation:

```kotlin
@file:ImportDataSchema(
    "Repository",
    "https://raw.githubusercontent.com/Kotlin/dataframe/master/data/jetbrains_repositories.csv",
)

import org.jetbrains.kotlinx.dataframe.annotations.ImportDataSchema
```

Method 3: Can be used in Gradle Task:

```kotlin
dataframes {
    schema {
        data = "https://raw.githubusercontent.com/Kotlin/dataframe/master/data/jetbrains_repositories.csv"
        name = "org.example.Repository"
    }
}
```

### 2. 读取 Dataframe

```kotlin
// todo: add some code demos
```

## Kotlin Dataframe 与 Apache Arrow 关系

some details in Kotlin dataframe

dataframeArrow core dependencies

```gradle
implementation(libs.arrow.vector)
implementation(libs.arrow.format)
implementation(libs.arrow.memory)
implementation(libs.commonsCompress)
implementation(libs.kotlin.reflect)
implementation(libs.kotlin.datetimeJvm)
```

### Notes for dataframe

1. the class should be `data class`,
2. the collections should be `List` or `Map` or `ArrayList`, can not be `Array`

## Kotlin Dataframe 问题

nested type in dataframe is not supported yet: https://github.com/Kotlin/dataframe/issues/271, we need to modify implementation of following classes:

- arrowTypesMatching.kt
- ArrowWriterImpl.kt

工作量比较大，需要修改 dataframe 的源码，所以暂时不考虑这个方案。
