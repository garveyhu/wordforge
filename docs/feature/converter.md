转换模块功能说明
========

该文档提供了转换模块中核心功能类和方法的详细说明，帮助开发者理解如何在项目中实现 Word 文档与 PDF、WordML 之间的转换。

核心功能概述
------

转换模块封装了以下核心功能：

*   **Word 转 PDF**：使用 `ConverterPDF.wordToPdf` 方法将 Word 文档转换为 PDF 格式。
*   **Word 转 WordML**：使用 `ConverterXML.convertWordToWordML` 方法将 Word 文档转换为 WordML 格式。
*   **WordML 转 Word**：使用 `ConverterXML.convertWordMLToWord` 方法将 WordML 格式的文档转换回 Word 文档格式。

功能使用指南
------

### Word 转 PDF

要将 Word 文档转换为 PDF 格式，您可以使用 `ConverterPDF.wordToPdf` 方法。该方法需要一个 `InputStream` 作为参数，它代表了源 Word 文件的输入流。

```java
InputStream inputStream = // 获取 Word 文件的输入流
OutputStream outputStream = ConverterPDF.wordToPdf(inputStream);
// 使用 outputStream 进行下一步操作（如保存文件）
```

### Word 转 WordML

要将 Word 文档转换为 WordML 格式，您可以使用 `ConverterXML.convertWordToWordML` 方法。该方法同样需要一个 `InputStream` 作为参数。

```java
InputStream inputStream = // 获取 Word 文件的输入流
OutputStream outputStream = ConverterXML.convertWordToWordML(inputStream);
// 使用 outputStream 进行下一步操作（如保存文件）
```

### WordML 转 Word

要将 WordML 格式的文档转换回 Word 文档格式，您可以使用 `ConverterXML.convertWordMLToWord` 方法。该方法需要一个 `InputStream` 作为参数，它代表了源 WordML 文件的输入流。

```java
InputStream inputStream = // 获取 WordML 文件的输入流
OutputStream outputStream = ConverterXML.convertWordMLToWord(inputStream);
// 使用 outputStream 进行下一步操作（如保存文件）
```

注意事项
----

*   在使用这些转换功能时，确保源文件的输入流正确获取，且文件格式符合预期。
*   转换过程可能根据文件的大小和复杂度消耗一定时间，请在实际应用中合理安排转换任务。
*   转换完成后，务必正确处理输出流，例如保存为文件或发送给客户端。