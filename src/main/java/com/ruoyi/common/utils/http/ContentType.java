package com.ruoyi.common.utils.http;

import com.ruoyi.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhaowang
 * @since 2024/7/3 11:08
 */
@AllArgsConstructor
@Getter
public enum ContentType {
    //  文本文件
    /**
     * 纯文本。表示文档是纯文本格式，不包含任何格式控制字符。
     */
    TXT("text/plain", ".txt"),
    /**
     * HTML文档。这是Web页面最常用的类型，表示文档是HTML格式的
     */
    HTML("text/html", ".html"),
    /**
     * JSON数据。这是一种轻量级的数据交换格式，易于人阅读和编写，同时也易于机器解析和生成。
     */
    JSON("application/json", ".json5"),
    /**
     * 样式文件
     */
    CSS("text/css", ".css"),
    /**
     * XML文档。用于表示文档是XML格式的。虽然text/xml和application/xml在技术上有所不同，但在实际应用中，它们经常被当作可互换的
     */
    XML("text/xml", ".xml"),
    /**
     * XML文档。用于表示文档是XML格式的。虽然text/xml和application/xml在技术上有所不同，但在实际应用中，它们经常被当作可互换的
     */
    A_XML("application/xml", ".xml"),
    /**
     * 脚本文件
     */
    JS("text/javascript", ".js"),
    /**
     * 脚本文件
     */
    A_JS("application/javascript", ".js"),
    //  图像文件
    /**
     * 图片文件。表示JPEG格式的图片
     */
    JPEG("image/jpeg", ".jpeg"),
    /**
     * 图片文件。表示PNG格式的图片
     */
    PNG("image/png", ".png"),
    /**
     * 图片文件。表示GIF格式的图片
     */
    GIF("image/gif", ".gif"),
    /**
     * 图片文件。表示SVG格式的图片
     */
    SVG("image/svg+xml", ".svg"),
    /**
     * 图片文件。表示BMP格式的图片
     */
    BMP("image/bmp", ".bmp"),
    //  视频文件
    MP4("video/mp4", ".mp4"),
    WEBM("video/webm", ".webm"),
    MOV("video/quicktime", ".mov"),
    MKV("video/x-matroska", ".mkv"),
    FLV("video/x-flv", ".flv"),
    //  音频文件
    MP3("audio/mpeg", ".mp3"),
    OGG("audio/ogg", ".ogg"),
    WAV("audio/wav", ".wav"),
    AAC("audio/aac", ".aac"),
    //  应用程序文件
    /**
     * PDF文档。表示文档是PDF格式的，PDF是一种广泛使用的文档格式，可以跨平台展示
     */
    PDF("application/pdf", ".pdf"),
    /**
     * zip模式的压缩文件
     */
    ZIP("application/zip", ".zip"),
    /**
     * GZ模式的压缩文件
     */
    GZ("application/gzip", ".gz"),
    /**
     * java程序归档文件
     */
    JAR("application/java-archive", ".jar"),
    /**
     * Microsoft Word 97-2003文档。虽然在现代Web应用中较少直接使用，但某些场合下仍然需要处理Word文档。
     */
    DOC("application/msword", ".doc"),
    /**
     * Microsoft Word 文档。虽然在现代Web应用中较少直接使用，但某些场合下仍然需要处理Word文档。
     */
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
    /**
     * Microsoft Excel 97-2003工作表。虽然在现代Web应用中较少直接使用，但某些场合下仍然需要处理Word文档。
     */
    XLS("application/vnd.ms-excel", ".xls"),
    /**
     * Microsoft Excel 工作表。虽然在现代Web应用中较少直接使用，但某些场合下仍然需要处理Word文档。
     */
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
    /**
     * Microsoft PowerPoint 97-2003演示文档。虽然在现代Web应用中较少直接使用，但某些场合下仍然需要处理Word文档。
     */
    PPT("application/vnd.ms-powerpoint", ".ppt"),
    /**
     * Microsoft PowerPoint 演示文档。虽然在现代Web应用中较少直接使用，但某些场合下仍然需要处理Word文档。
     */
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx"),
    /**
     * 表单数据编码。这是HTML表单在提交时使用的默认编码类型，将表单数据编码为key/value对
     */
    FORM_URLENCODED("application/x-www-form-urlencoded", StringUtils.EMPTY),
    /**
     * 多媒体容器 通常不直接关联到文件后缀，但用于HTTP表单的编码类型，特别是包含文件上传时。
     */
    MULTIPART("multipart/form-data", StringUtils.EMPTY),
    /**
     * 二进制流数据。通常用于表示无法识别的二进制文件或需要作为二进制流下载的文件
     */
    STREAM("application/octet-stream", StringUtils.EMPTY),
    ;

    /**
     * mime类型
     */
    private final String mimeType;
    /**
     * 对应文件类型后缀
     */
    private final String suffix;

    public static ContentType covert(String contentTypeStr) {
        for (ContentType contentType : values()) {
            if (contentTypeStr.contains(contentType.mimeType)) {
                return contentType;
            }
        }
        return TXT;
    }

}
