# ArchGuard 中大数据探索：Kotlin Dataframe 与 Apache Arrow 的尝试

（PS：尝试结合 ChatGPT 生成）

Dataframe Arrow 是基于 Apache Arrow 的 Kotlin Dataframe 实现，目标是提供高性能的数据处理框架，同时保持与 Apache Arrow 的兼容性。
本文将探讨 Dataframe Arrow 的应用以及 Kotlin Dataframe 与 Apache Arrow 的关系。

## 为什么 Dataframe Arrow 是可接受的

Kotlin Dataframe Arrow 的优点：

1. 高性能：由于使用了 Apache Arrow 的数据结构，可以实现高效的数据处理和传输。
2. 数据格式标准化：Apache Arrow 提供了一种标准的跨语言内存布局，Kotlin Dataframe Arrow 的实现与 Apache Arrow 的兼容性非常高，从而可以实现数据格式的标准化，使得数据的处理更加方便。
3. 易于使用：Kotlin Dataframe Arrow 为数据处理和传输提供了一种简单、易于使用的框架，可用于数据挖掘、机器学习、数据分析等领域。在 Kotlin 中使用 Dataframe 也非常方便，只需要定义 Dataframe Schema 和读取 Dataframe 即可。

除此，对于 ArchGuard 的 CLI 来说，体积也是很重要的，如下是积的差异：

- Apache Arrow with Chapi: 14M
- Jetbrains Dataframe with Chapi: 35M
- Jetbrains Dataframe Arrow with Chapi: 19M

Jetbrains Dataframe Arrow 依赖于 Apache Arrow，4M 的差异还是可以接受的。

## Apache Arrow Schema 与 FlatBuffer Schema

Apache Arrow Schema 与 FlatBuffer Schema 是两种不同的 Schema 定义方式，其区别在于：

- Apache Arrow Schema 是使用 JSON 格式。
- FlatBuffer Schema 是使用自定义的 FlatBuffer Schema 格式。

我们来看一个示例。假设我们有一个待序列化的 Kotlin 类：

```kotlin
@Serializable
data class CodePosition(
    var StartLine: Int = 0,
    var StartLinePosition: Int = 0,
    var StopLine: Int = 0,
    var StopLinePosition: Int = 0
)
```

那么，这个类的 FlatBuffer Schema 定义为：

```flatbuffer
table Position {
  StartLine: int;
  StartLinePosition: int;
  StopLine: int;
  StopLinePosition: int;
}
```

而这个类的 Apache Arrow Schema 则可以用以下的 JSON 表示：

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

对于 Apache Arrow Schema 的 Kotlin 实现，可以使用以下代码：

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

在 Kotlin 中使用 Dataframe，需要先定义 Dataframe 的 schema，然后可以使用 Dataframe API 进行数据的读取、过滤、聚合等操作。

下面介绍三种定义 Dataframe schema 的方式：

### 1. 定义 Dataframe Schema

在 Kotlin 中，可以使用类定义 Dataframe 的 schema。需要在类上添加 @DataSchema 注解，然后在类中定义属性作为 Dataframe 的列。属性需要使用 val 或 var 关键字定义，并指定属性的类型和名称。如下所示：

```kotlin
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema

@DataSchema
interface Person {
    val name: String
    val age: Int
}
```

### 2. 使用注解

可以使用 @ImportDataSchema 注解来导入已有的 Dataframe schema，如下所示：

```kotlin
@file:ImportDataSchema(
    "Repository",
    "https://raw.githubusercontent.com/Kotlin/dataframe/master/data/jetbrains_repositories.csv",
)

import org.jetbrains.kotlinx.dataframe.annotations.ImportDataSchema
```

### 3. 使用 Gradle Task

定义好 schema 后，可以使用 Dataframe API 进行数据读取和处理，如下所示：

```kotlin
dataframes {
    schema {
        data = "https://raw.githubusercontent.com/Kotlin/dataframe/master/data/jetbrains_repositories.csv"
        name = "org.example.Repository"
    }
}
```

此外，Dataframe 还支持数据过滤、聚合等操作，具体使用可以参考 Kotlin Dataframe 的官方文档。

## Kotlin Dataframe 与 Apache Arrow 关系

Kotlin Dataframe是基于Apache Arrow的数据框架，它提供了对Arrow内存格式和Arrow表格格式的支持。因此，Kotlin Dataframe可以看作是Arrow在Kotlin中的实现。

Apache Arrow是一种跨语言的内存数据结构，可以有效地在不同的计算引擎和编程语言之间传递数据。Arrow的一个关键优势是它使用了内存映射文件（Memory Mapped Files）来管理数据，这使得跨语言和跨平台数据传输变得更加高效和可扩展。

Kotlin Dataframe 的依赖如下所示：

```gradle
implementation(libs.arrow.vector)
implementation(libs.arrow.format)
implementation(libs.arrow.memory)
implementation(libs.commonsCompress)
implementation(libs.kotlin.reflect)
implementation(libs.kotlin.datetimeJvm)
```

Kotlin Dataframe 依赖于 Arrow 的核心库和格式库，这些库提供了 Arrow 表格格式和内存格式的支持。除此之外，Kotlin Dataframe 还使用了 Kotlin 反射库和 Kotlin 日期时间库等其他第三方库。

## 注意事项

当使用 Kotlin Dataframe 时，还需要注意以下几点：

- 需要将数据类声明为 data class，以便 Dataframe 可以识别数据类的属性。
- 集合类型应该是 List、Map 或 ArrayList，而不是 Array，因为 Dataframe 对于集合的处理是基于 Java Collections Framework 的，而不是基于原生数组。

此外，还需要注意以下几点：

- 数据类的属性类型应该是 Kotlin 原生类型或者是具有无参构造函数的类，这是因为 Dataframe 可以直接将这些类型映射到 Arrow 数据类型，而无需进行其他转换。
- 如果数据类的属性类型是一个复合类型，比如一个列表或一个嵌套的数据类，需要使用 Arrow 的数据类型来定义这个属性的类型。
- 在定义 Dataframe schema 时，需要显式指定每个列的数据类型，否则 Dataframe 会根据数据自动推断数据类型，可能会导致类型不一致的错误。
- 在读取 CSV 或 Parquet 文件时，需要指定文件的 schema，否则 Dataframe 也会尝试根据数据自动推断数据类型，可能会导致类型不一致的错误。

## Kotlin Dataframe 问题

nested type in dataframe is not supported yet: https://github.com/Kotlin/dataframe/issues/271, we need to modify implementation of following classes:

- arrowTypesMatching.kt
- ArrowWriterImpl.kt

工作量比较大，需要修改 dataframe 的源码，所以暂时不考虑这个方案。
