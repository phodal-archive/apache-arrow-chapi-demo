# Package Size

- Apache Arrow with Chapi: 14M
- Jetbrains Dataframe with Chapi: 35M
- Jetbrains Dataframe Arrow with Chapi: 19M

Jetbrains Dataframe Arrow depends on Apache Arrow, so it is not surprising that the size is the same as Jetbrains Dataframe.

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
