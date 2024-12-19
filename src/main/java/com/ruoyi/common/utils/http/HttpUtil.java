package com.ruoyi.common.utils.http;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.enums.SpecialCharacter;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.file.FileException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.web.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zhaowang
 * @since 2022/8/19 15:12
 */
@Slf4j
public class HttpUtil {
    /**
     * 基础请求
     *
     * @param method   方式
     * @param url      url
     * @param headers  headers
     * @param params   params
     * @param formData 表单参数
     * @param body     body
     * @return 返回值
     */
    public static HttpResponse request(Method method, String url,
                                       Map<String, List<String>> headers,
                                       Map<String, Object> params,
                                       Map<String, Object> formData,
                                       Object body) {
        UrlBuilder urlBuilder = newUrlBuilder(url);
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach((k, v) -> urlBuilder.addQuery(k, v != null ? v.toString() : StringUtils.EMPTY));
        }
        HttpRequest request = cn.hutool.http.HttpUtil.createRequest(method, urlBuilder.build());
        if (!CollectionUtils.isEmpty(headers)) {
            // request header
            request.header(headers);
        }
        if (!CollectionUtils.isEmpty(formData)) {
            // 表单数据
            request.form(formData);
        }
        if (body != null) {
            if (body instanceof byte[]) {
                request.body((byte[]) body);
            } else {
                String bodyStr;
                if (body instanceof String) {
                    bodyStr = body.toString();
                } else {
                    bodyStr = JSONUtil.toJsonStr(body);
                }
                request.body(bodyStr);
            }
        }
        /*String logStr = "\n=============================================";
        logStr += "\nRequest method: " + request.getMethod().name();
        logStr += "\n" + request;
        logStr += "\n                                     ********";
        log.info(logStr);*/
        HttpResponse response = request.execute();
        /*int status = response.getStatus();
        logStr = "\nResponse status:" + status;
        logStr += "\n" + response;
        logStr += "\n---------------------------------------------";
        log.info(logStr);*/
        return response;
    }

    /**
     * 请求结果转为json对象
     *
     * @param method   方式
     * @param url      url
     * @param headers  headers
     * @param params   params
     * @param formData 表单参数
     * @param body     body
     * @return 返回值
     */
    public static JSON request4Json(Method method, String url,
                                    Map<String, List<String>> headers,
                                    Map<String, Object> params,
                                    Map<String, Object> formData,
                                    Object body) {
        HttpResponse response = request(method, url, headers, params, formData, body);
        if (response.isOk()) {
            return covertResponse2Json(response);
        } else {
            int status = response.getStatus();
            String info = String.format("三方响应有误：{ statusCode: %d, responseText: %s }", status, response.body().trim());
            throw new ServiceException(info);
        }
    }

    /**
     * 请求流接口 返回为数据流
     *
     * @param url   Url
     * @param param 参数
     * @return 文件流
     */
    public static InputStream getInputStream(String url, Map<String, Object> param) {
        try (HttpResponse response = request(Method.GET, url, null, param, null, null)) {
            return response.bodyStream();
        }
    }

    /**
     * 请求流接口 返回为数据流
     *
     * @param url   Url
     * @param param 参数
     * @param body  body
     * @return 文件流
     */
    public static InputStream postInputStream(String url, Map<String, Object> param, Object body) {
        return postInputStream(url, null, param, body);
    }

    /**
     * 请求流接口 返回为数据流
     *
     * @param url     Url
     * @param headers headers
     * @param param   参数
     * @param body    body
     * @return 文件流
     */
    public static InputStream postInputStream(String url, Map<String, String> headers, Map<String, Object> param, Object body) {
        try (HttpResponse response = request(Method.POST, url, covertHeader(headers), param, null, body)) {
            return response.bodyStream();
        }
    }

    /**
     * 返回值转化为json对象
     *
     * @param response 响应对象
     * @return response json
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    private static JSON covertResponse2Json(HttpResponse response) {
        String responseBody = response.body();
        if (StringUtils.isNotBlank(responseBody)) {
            responseBody = responseBody.trim();
            if (responseBody.startsWith("{")) {
                return JSONUtil.parseObj(responseBody);
            } else if (responseBody.startsWith("[")) {
                return JSONUtil.parseArray(responseBody);
            } else {
                String info = String.format("三方响应：{ statusCode: %d, responseText: %s }", response.getStatus(),
                        responseBody);
                throw new ServiceException(info);
            }
        }
        return null;
    }

    /**
     * GET请求
     *
     * @param url 请求路径
     * @return 返回值
     */
    public static JSON get(String url) {
        return request4Json(Method.GET, url, null, null, null, null);
    }

    /**
     * GET请求
     *
     * @param url   请求路径
     * @param param param
     * @return 返回值
     */
    public static JSON get(String url, Map<String, Object> param) {
        return request4Json(Method.GET, url, null, param, null, null);
    }

    /**
     * GET请求
     *
     * @param url     请求路径
     * @param headers headers
     * @param param   param
     * @return 返回值
     */
    public static JSON get(String url, Map<String, String> headers, Map<String, Object> param) {
        Map<String, List<String>> headerMap = covertHeader(headers);
        return request4Json(Method.GET, url, headerMap, param, null, null);
    }

    /**
     * POST请求
     *
     * @param url  请求路径
     * @param body body
     * @return 返回值
     */
    public static JSON post(String url, Object body) {
        return request4Json(Method.POST, url, null, null, null, body);
    }

    /**
     * POST form请求
     *
     * @param url  请求路径
     * @param form form
     * @return 返回值
     */
    public static JSON postForm(String url, Object form) {
        Map<String, List<String>> headerMap = new HashMap<>(0);
        headerMap.put("Content-Type", Collections.singletonList("application/x-www-form-urlencoded"));
        Map<String, Object> formMap = BeanUtil.beanToMap(form);
        return request4Json(Method.POST, url, headerMap, null, formMap, null);
    }

    /**
     * POST请求
     *
     * @param url    请求路径
     * @param params query params
     * @param body   body
     * @return 返回值
     */
    public static JSON post(String url, Map<String, Object> params, Object body) {
        return request4Json(Method.POST, url, null, params, null, body);
    }

    /**
     * POST请求
     *
     * @param url     请求路径
     * @param headers headers
     * @param params  query params
     * @param body    body
     * @return 返回值
     */
    public static JSON post(String url, Map<String, String> headers, Map<String, Object> params, Object body) {
        Map<String, List<String>> headerMap = covertHeader(headers);
        return request4Json(Method.POST, url, headerMap, params, null, body);
    }

    /**
     * POST请求
     *
     * @param url      请求路径
     * @param params   query params
     * @param formData formData
     * @param body     body
     * @return 返回值
     */
    public static JSON post(String url, Map<String, String> headers, Map<String, Object> params, Map<String, Object> formData, Object body) {
        Map<String, List<String>> headerMap = covertHeader(headers);
        return request4Json(Method.POST, url, headerMap, params, formData, body);
    }

    private static Map<String, List<String>> covertHeader(Map<String, String> headers) {
        Map<String, List<String>> headerMap = null;
        if (headers != null) {
            headerMap = headers.entrySet()
                    .stream().collect(Collectors.toMap(Map.Entry::getKey, v -> Collections.singletonList(v.getValue())));
        }
        return headerMap;
    }

    /**
     * PUT请求
     *
     * @param url  请求路径
     * @param body body
     * @return 返回值
     */
    public static JSON put(String url, Object body) {
        return request4Json(Method.PUT, url, null, null, null, body);
    }

    /**
     * PUT请求
     *
     * @param url     请求路径
     * @param headers headers
     * @param param   param
     * @param body    body
     * @return 返回值
     */
    public static JSON put(String url, Map<String, String> headers, Map<String, Object> param, Object body) {
        Map<String, List<String>> headerMap = covertHeader(headers);
        return request4Json(Method.PUT, url, headerMap, param, null, body);
    }

    /**
     * DEL请求
     *
     * @param url   请求路径
     * @param param param
     * @return 返回值
     */
    public static JSON del(String url, Map<String, Object> param) {
        return request4Json(Method.PUT, url, null, param, null, null);
    }

    /**
     * 图片流
     *
     * @param url   url
     * @param param 参数
     * @throws IOException 异常
     */
    public static void downloadImage(String url, Map<String, Object> param) throws IOException {
        HttpServletResponse response = ServletUtils.getResponse();
        assert response != null;
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + DateUtils.dateTimeNow() + ".png");
        InputStream is = getInputStream(url, param);
        IOUtils.copy(is, response.getOutputStream());
    }

    /**
     * 图片下载
     *
     * @param url url
     * @return 临时文件对象
     */
    public static R<Object> downloadImage(String url) {
        return HttpUtil.downloadImage(url, null, null);
    }

    /**
     * 图片下载
     *
     * @param url   url
     * @param param 参数
     * @param body  body
     * @return 临时文件对象
     */
    public static R<Object> downloadImage(String url,
                                          Map<String, Object> param,
                                          Map<String, Object> body) {
        HttpResponse response = request(Method.GET, url, null, param, null, body);
        if (response.isOk()) {
            ContentType contentType = ContentType.covert(response.header("Content-Type"));
            if (contentType == ContentType.JSON) {
                return R.ok(covertResponse2Json(response));
            } else {
                InputStream is = null;
                FileOutputStream fos = null;
                File tempFile;
                try {
                    is = response.bodyStream();
                    tempFile = File.createTempFile(DateUtils.dateTimeNow(), covertSuffix(contentType, url));
                    fos = new FileOutputStream(tempFile);
                    IOUtils.copy(is, fos);
                    R<Object> r = R.ok(tempFile);
                    r.setMsg(contentType.getMimeType());
                    return r;
                } catch (IOException e) {
                    throw new ServiceException(e.getMessage());
                } finally {
                    try {
                        IOUtils.close(is, fos);
                    } catch (IOException e) {
                        throw new ServiceException(e.getMessage());
                    }
                }
            }
        } else {
            R<Object> err = R.fail(response.getStatus(), "requestFail");
            err.setData(response.body());
            return err;
        }
    }

    private static String covertSuffix(ContentType contentType, String url) {
        String suffix = contentType.getSuffix();
        if (StringUtils.isBlank(suffix)) {
            if (url.contains(".") && url.contains("?")) {
                return url.substring(0, url.indexOf("?")).substring(url.substring(0, url.indexOf("?")).lastIndexOf("."));
            }
        }
        return suffix;
    }

    /**
     * 图片转存入file服务
     *
     * @param url   url
     * @param param 参数
     * @param body  body
     * @return 转存位置
     */
    public static R<JSON> downloadImagePath(String url,
                                            Map<String, Object> param,
                                            Map<String, Object> body) {
        R<Object> imageR = downloadImage(url, param, body);
        if (R.isSuccess(imageR)) {
            Object object = imageR.getData();
            if (object instanceof File) {
                File tempFile = (File) object;
                Optional<String> filePath = SpringUtils.getValue("file.uploadUrl");
                if (tempFile.length() > 0 && filePath.isPresent()) {
                    String fileUrl = filePath.get();
                    Map<String, Object> formData = new HashMap<>(0);
                    formData.put("file", tempFile);
                    return (post(fileUrl, null, null, formData, null))
                            .toBean(new TypeReference<R<JSON>>() {
                            });
                }
                return R.fail();
            } else {
                return R.ok((JSONObject) object);
            }
        } else {
            return R.fail(imageR.getMsg());
        }
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 转存位置
     */
    public static JSON uploadFile(String url, Map<String, String> header, File file) {
        if (file == null || !file.exists() || file.length() == 0) {
            throw new FileException(String.valueOf(HttpStatus.ERROR), new Object[]{"文件为空"});
        }
        header.put("Accept", "application/json");
        Map<String, Object> formData = new HashMap<>(0);
        formData.put("file", file);
        return post(url, header, null, formData, null);
    }

    /**
     * 文件上传
     *
     * @param url      上传地址
     * @param filePath 文件路径
     * @return 转存位置
     */
    public static JSON uploadFile(String url, Map<String, String> header, String filePath) {
        File file = new File(filePath);
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            byte[] fileB = new byte[(int) file.length()];
            inputStream.read(fileB);
            inputStream.close();
            HttpResponse response = request(Method.POST, url, covertHeader(header), null, null, fileB);
            return covertResponse2Json(response);
        } catch (IOException e) {
            String errMsg = String.format("uploadFile error: %s", e.getMessage());
            log.error(errMsg);
            throw new ServiceException(errMsg);
        }
    }

    /**
     * 构造url
     *
     * @param url url
     * @return url构造器
     */
    public static UrlBuilder newUrlBuilder(String url) {
        UrlBuilder urlBuilder;
        if (StringUtils.isNotBlank(url)) {
            urlBuilder = UrlBuilder.of(url);
            return urlBuilder;
        }
        throw new ServiceException("url 不能为空");
    }

    /**
     * 获取请求 body 入参 json
     *
     * @param request HttpServletRequest
     * @return json
     * @throws IOException io异常
     */
    public static JSON covertCbParam(HttpServletRequest request) throws IOException {
        String bodyParam = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        bodyParam = preprocessInputString(bodyParam).trim();
//        log.info("callback request param: {}", bodyParam);
        JSON json;
        if (bodyParam.contains(SpecialCharacter.EQUALS.getValue().toString())) {
            json = new JSONObject(Arrays.stream(bodyParam.split(SpecialCharacter.AMPERSAND.getValue().toString()))
                    .map(param -> param.split(SpecialCharacter.EQUALS.getValue().toString()))
                    .collect(Collectors.toMap(m -> m[0], m -> m[1])));
        } else {
            json = JSONUtil.parseObj(bodyParam);
        }
//        log.info("callback param json: {}", json);
        return json;
    }

    /**
     * 预处理url编码
     */
    private static String preprocessInputString(String input) throws UnsupportedEncodingException {
        Pattern pattern = Pattern.compile("%[0-9A-Fa-f]{2}");
        Matcher matcher = pattern.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String replaceStr = URLDecoder.decode(matcher.group(), "UTF-8");
            matcher.appendReplacement(sb, replaceStr);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
