拆分模块功能说明
========

拆分模块提供了文档按分隔符和页码（单页及奇偶页）进行拆分的能力，使用户能够根据特定需求对文档进行拆分。

功能概述
----

拆分模块包括以下核心功能：

*   **分隔符切分**：根据指定的分隔符对文档进行拆分。
*   **页码切分（单页）**：将文档拆分成单独的页面。
*   **页码切分（奇偶页）**：将文档拆分为奇数页和偶数页两组。

核心方法说明
------

### 分隔符切分

使用 `SplitterLabel.splitDocumentBySeparator` 方法，可以根据给定的分隔符对文档进行拆分。该方法接收文档的 `InputStream` 和分隔符字符串作为参数，返回拆分后的 `OutputStream` 列表。

```java
InputStream inputStream = // 获取文档的输入流
String separator = "§"; // 指定分隔符
List<OutputStream> outputStreams = SplitterLabel.splitDocumentBySeparator(inputStream, separator);
// 处理 outputStreams（例如，保存为文件或发送给客户端）
```

### 页码切分（单页）

通过 `SplitterPage.splitDocumentByPage` 方法，可以将文档拆分成单独的页面。该方法仅需要文档的 `InputStream` 作为参数，返回拆分后的 `OutputStream` 列表。

```java
InputStream inputStream = // 获取文档的输入流
List<OutputStream> outputStreams = SplitterPage.splitDocumentByPage(inputStream);
// 处理 outputStreams
```

### 页码切分（奇偶页）

`SplitterPage.splitDocumentByOddEvenPages` 方法允许将文档拆分为奇数页和偶数页两组。该方法接收文档的 `InputStream` 作为参数，返回拆分后的 `OutputStream` 列表。

```java
InputStream inputStream = // 获取文档的输入流
List<OutputStream> outputStreams = SplitterPage.splitDocumentByOddEvenPages(inputStream);
// 处理 outputStreams`
```

注意事项
----

*   确保在使用拆分功能时，文档的输入流正确获取，并且文档格式符合预期。
*   根据拆分后的 `OutputStream` 列表，可以将每个流保存为单独的文件，或者根据需求进行其他处理。
*   拆分过程可能根据文档的大小和复杂度消耗一定时间，请在实际应用中考虑性能影响。
*   操作完成后，务必正确管理资源，例如关闭流等。