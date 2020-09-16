package com.sw.common.util;//package com.sw.common.util;
//
//import com.sw.cache.util.SpringContextHolder;
//import okhttp3.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * @Description:  http请求
// * @Author:       allenyll
// * @Date:         2019/4/4 10:44 AM
// * @Version:      1.0
// */
//@Component
//public class OkHttpUtil {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpUtil.class);
//
//    public static final String TYPE = "text/html;charset:utf-8";
//
//    public static final String JSON = "text/html;charset:utf-8";
//
//    @Autowired
//    private static OkHttpClient okHttpClient;
//
//    private static DataResponse dataResponse;
//
//    public static DataResponse execute(Request request){
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                OkHttpUtil.dataResponse = DataResponse.fail(e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Map<String, Object> map = new HashMap<>();
//                map.put("response", response);
//                OkHttpUtil.dataResponse = DataResponse.success(map);
//            }
//        });
//        return dataResponse;
//    }
//
//    /**
//     * 调用okhttp的newCall方法
//     * @param request
//     * @return
//     */
//    private static String execNewCall(Request request){
//        Response response = null;
//        try {
//            OkHttpClient okHttpClient = SpringContextHolder.getBean(OkHttpClient.class);
//            response = okHttpClient.newCall(request).execute();
//            int status = response.code();
//            if (response.isSuccessful()) {
//                return response.body().string();
//            }
//        } catch (Exception e) {
//            LOGGER.error("okhttp3 put error >> ex = {}", e.getMessage());
//        } finally {
//            if (response != null) {
//                response.close();
//            }
//        }
//        return "";
//    }
//
//    /**
//     * 根据map获取get请求参数
//     * @param queries
//     * @return
//     */
//    public static StringBuffer getQueryString(String url, Map<String,String> queries){
//        StringBuffer sb = new StringBuffer(url);
//        if (queries != null && queries.keySet().size() > 0) {
//            boolean firstFlag = true;
//            Iterator iterator = queries.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
//                if (firstFlag) {
//                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
//                    firstFlag = false;
//                } else {
//                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
//                }
//            }
//        }
//        return sb;
//    }
//
//    /**
//     * get
//     * @param url     请求的url
//     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
//     * @return
//     */
//    public static String get(String url, Map<String, String> queries) {
//        StringBuffer sb = getQueryString(url,queries);
//        Request request = new Request.Builder()
//                .url(sb.toString())
//                .build();
//        return execNewCall(request);
//    }
//
//    /**
//     * post
//     * @param url    请求的url
//     * @param params post form 提交的参数
//     * @return
//     */
//    public static DataResponse postForm(String url, Map<String, String> params) {
//        FormBody.Builder builder = new FormBody.Builder();
//        //添加参数
//        if (params != null && params.keySet().size() > 0) {
//            for (String key : params.keySet()) {
//                builder.add(key, params.get(key));
//            }
//        }
//        Request request = new Request.Builder()
//                .url(url)
//                .post(builder.build())
//                .build();
//        return execute(request);
//    }
//
//
//    /**
//     * Post请求发送JSON数据....{"name":"yll","pwd":"123456"}
//     * @param url
//     * @param json
//     * @return
//     */
//    public static DataResponse postJson(String url, String json, Headers headers) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse(JSON), json);
//        Request request;
//        if(headers == null){
//            request = new Request.Builder().url(url).post(requestBody).build();
//        }else{
//            request = new Request.Builder().headers(headers).url(url).post(requestBody).build();
//        }
//        return execute(request);
//    }
//
//    /**
//     * Post请求发送xml数据
//     * @param url
//     * @param xml
//     * @return
//     */
//    public static DataResponse postXml(String url, String xml) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//        return execute(request);
//    }
//}
