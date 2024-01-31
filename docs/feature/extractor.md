提取模块功能说明
========

该文档旨在详细介绍提取模块中的核心功能，包括如何提取文档内容（按照不同条件）及如何将提取的内容渲染回文档。

功能概述
----

提取模块提供了多种方式来提取文档中的内容，包括：

*   **按照段落提取**
*   **按照标题级别提取**
*   **按照分隔符提取**

此外，模块还提供了一个渲染功能，允许将提取的 JSON 格式内容渲染回 Word 文档。

核心方法说明
------

### 提取文档内容（按照段落）

通过 `ExtractorDoc.extract0` 方法，可以根据指定的选项提取文档内容。该方法接收一个 `MultipartFile` 类型的文件和一个 `ExtractionOptions` 对象作为参数，返回提取结果的 `JSONObject`。

```java
MultipartFile file = // 获取上传的文件
ExtractionOptions options = // 设置提取选项
JSONObject jsonObject = ExtractorDoc.extract0(file, options);
```

### 提取文档内容（按照标题级别）

使用 `ExtractorDoc.extract1` 方法，可以按照标题级别提取文档内容。该方法同样接收一个文件和提取选项，返回提取结果的 `JSONObject`。

```java
MultipartFile file = // 获取上传的文件
ExtractionOptions options = // 设置提取选项
JSONObject jsonObject = ExtractorDoc.extract1(file, options);
```

### 提取文档内容（按照分隔符）

`ExtractorDoc.extract2` 方法允许按照自定义的分隔符提取文档内容。除了文件和提取选项外，还需要指定分隔符。

```java
MultipartFile file = // 获取上传的文件
ExtractionOptions options = // 设置提取选项
String delimiter = "§"; // 设置分隔符
JSONObject jsonObject = ExtractorDoc.extract2(file, options, delimiter);
```

### 渲染文档内容

`ExtractorRender.renderJsonToWord` 方法可以将 JSON 格式的提取内容渲染回 Word 文档。该方法接收一个 `JSONObject` 作为参数，返回一个用于渲染文档的 `OutputStream`。

```java
String json = // 获取要渲染的 JSON 字符串
JSONObject jsonObject = JSONObject.parseObject(json);
OutputStream outputStream = ExtractorRender.renderJsonToWord(jsonObject);
// 使用 outputStream 进行下一步操作（如保存文件）
```

注意事项
----

*   在使用提取功能时，确保上传的文件格式正确，并符合预期的提取条件。
*   提取选项 (`ExtractionOptions`) 应根据具体需求进行配置，以控制提取过程的细节，如是否提取标题、样式等。
*   渲染功能需要接收正确格式的 JSON 字符串，该字符串应按照特定的结构组织提取的内容。
*   在处理提取或渲染结果时，务必注意资源管理，如正确关闭流等。