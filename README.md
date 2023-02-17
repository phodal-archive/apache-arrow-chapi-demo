# Package Size

- Apache Arrow with Chapi: 14M
- Jetbrains Dataframe with Chapi: 35M
- Jetbrains Dataframe Arrow with Chapi: 19M

Jetbrains Dataframe Arrow depends on Apache Arrow, so it is not surprising that the size is the same as Jetbrains Dataframe.

## Use dataframe-arrow

## Schema inference

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

## Dataframe details

dataframeArrow core dependencies

```gradle
implementation(libs.arrow.vector)
implementation(libs.arrow.format)
implementation(libs.arrow.memory)
implementation(libs.commonsCompress)
implementation(libs.kotlin.reflect)
implementation(libs.kotlin.datetimeJvm)
```

## Notes for dataframe

1. the class should be `data class`,
2. the collections should be `List` or `Map` or `ArrayList`, can not be `Array`

