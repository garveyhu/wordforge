package com.sunyard.wordforge.util;

/**
 * 综合工具
 *
 * @author Archer
 */
public class ComplexUtil {

    /**
     * 将word文件后缀替换为pdf
     *
     * @param src 源文件路径
     * @return 替换后的文件路径
     */
    public static String replaceWordExtensionWithPdf(String src) {
        if (src.endsWith(".docx")) {
            return src.replace(".docx", ".pdf");
        } else if (src.endsWith(".doc")) {
            return src.replace(".doc", ".pdf");
        } else {
            return src;
        }
    }

    /**
     * 获取文件路径字符串中的文件名（不包括扩展名）
     *
     * @param filePath 文件路径字符串
     * @return 文件名
     */
    public static String getFileNameWithoutExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        int slashIndex = filePath.lastIndexOf('\\');
        if (dotIndex > slashIndex) {
            return filePath.substring(slashIndex + 1, dotIndex);
        }
        return String.valueOf(System.currentTimeMillis());
    }
}
