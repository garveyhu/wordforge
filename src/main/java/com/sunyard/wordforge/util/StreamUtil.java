package com.sunyard.wordforge.util;

import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.constant.MimeTypeConstant;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 流工具
 *
 * @author Archer
 */
public class StreamUtil {

    /**
     * 将文件绝对路径转换为输入流
     *
     * @param filePath 文件的绝对路径
     * @return 输入流
     */
    public static InputStream filePathToInputStream(String filePath) {
        try {
            return Files.newInputStream(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将多个文件绝对路径转换为输入流集合
     *
     * @param filePaths 文件的绝对路径集合
     * @return 输入流集合
     */
    public static List<InputStream> filePathsToInputStreams(List<String> filePaths) {
        List<InputStream> inputStreams = new ArrayList<>();
        for (String filePath : filePaths) {
            try {
                InputStream inputStream = Files.newInputStream(Paths.get(filePath));
                inputStreams.add(inputStream);
            } catch (IOException e) {
                throw new RuntimeException("Error opening file: " + filePath, e);
            }
        }
        return inputStreams;
    }

    /**
     * 将单个输出流保存为本地文件
     *
     * @param outputStream         输出流
     * @param destinationDirectory 目标目录路径
     * @param fileName             可选的文件名（如果为空，则使用时间戳）
     */
    public static void outputStreamToFile(OutputStream outputStream, String destinationDirectory, String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = generateUniqueFileName() + ".docx";
        }

        File directory = new File(destinationDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File destinationFile = new File(destinationDirectory, fileName);

        if (outputStream instanceof ByteArrayOutputStream) {
            try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
                fos.write(((ByteArrayOutputStream) outputStream).toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Output stream is not an instance of ByteArrayOutputStream");
        }
    }

    /**
     * 将多个输出流保存为多个本地文件
     *
     * @param outputStreams        输出流列表
     * @param destinationDirectory 输出目录
     * @param fileNames            可选的文件名列表（可以为空或部分为空）
     */
    public static void outputStreamsToFiles(
        List<OutputStream> outputStreams,
        String destinationDirectory,
        List<String> fileNames
    ) {
        if (fileNames != null && outputStreams.size() != fileNames.size()) {
            throw new IllegalArgumentException(
                "The number of output streams and file names must be equal, or file names can be null"
            );
        }

        for (int i = 0; i < outputStreams.size(); i++) {
            String fileName = (fileNames == null || fileNames.get(i) == null || fileNames.get(i).trim().isEmpty())
                ? i + generateUniqueFileName() + ".docx"
                : fileNames.get(i);
            outputStreamToFile(outputStreams.get(i), destinationDirectory, fileName);
        }
    }

    /**
     * 将单个文件输出流转为HttpServletResponse响应
     *
     * @param outputStream 输出流
     * @param response     HttpServletResponse
     * @param fileName     文件名
     * @param contentType  内容类型
     */
    public static void outputStreamToResponse(
        OutputStream outputStream,
        HttpServletResponse response,
        String fileName,
        String contentType
    )
        throws IOException {
        setResponseHeaders(response, fileName, contentType);
        if (outputStream instanceof ByteArrayOutputStream) {
            byte[] data = ((ByteArrayOutputStream) outputStream).toByteArray();
            response.addHeader("Content-Length", String.valueOf(data.length));
            response.getOutputStream().write(data);
        }
    }

    /**
     * 将多个文件输出流转为HttpServletResponse响应（ZIP文件）
     *
     * @param outputStreams 输出流列表
     * @param response      HttpServletResponse
     * @param zipFileName   ZIP文件名
     */
    public static void outputStreamsToResponseAsZip(
        List<OutputStream> outputStreams,
        HttpServletResponse response,
        String zipFileName
    )
        throws IOException {
        setResponseHeaders(response, zipFileName, MimeTypeConstant.APPLICATION_ZIP);

        try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
            for (OutputStream outputStream : outputStreams) {
                ByteArrayOutputStream baos = (ByteArrayOutputStream) outputStream;
                ZipEntry entry = new ZipEntry(generateUniqueFileName());
                zippedOut.putNextEntry(entry);
                baos.writeTo(zippedOut);
                zippedOut.closeEntry();
            }
        }
    }

    /**
     * 设置HttpServletResponse的响应头
     *
     * @param response    HttpServletResponse
     * @param fileName    文件名
     * @param contentType 内容类型
     */
    private static void setResponseHeaders(HttpServletResponse response, String fileName, String contentType)
        throws UnsupportedEncodingException {
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\""
        );
    }

    /**
     * 生成唯一的文件名
     *
     * @return 唯一的文件名
     */
    private static String generateUniqueFileName() {
        String source = Instant.now().getEpochSecond() + "_" + UUID.randomUUID();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] result = digest.digest(source.getBytes());
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < result.length && i < 5; ++i) { // Use first 5 bytes (40 bits, 10 hex chars)
            String hex = Integer.toHexString(0xff & result[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
