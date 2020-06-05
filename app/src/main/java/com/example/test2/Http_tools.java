package com.example.test2;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http_tools  {
    public String get(String cookie) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"type\":\"org\",\"identity\":\"\",\"para\":{\"organization_id\":\"1649\",\"organization_path_str\":\"1649,1650,1651,1652,1653,1654,1655\"},\"date\":\"2020-06-05\",\"activityid\":\"3195\",\"flag\":0,\"domain\":\"cqcfe\",\"stucode\":\"032018030186\"}");
        Request request = new Request.Builder()
                .url("https://lightapp.weishao.com.cn/api/reportstatistics/reportstatistics/getStatistical")
                .method("POST", body)
                .addHeader("Content-Length", "214")
                .addHeader("Content-Type", "application/json")
                .addHeader("Referer", "https://lightapp.weishao.com.cn/reportstatistics/reportMember")
                .addHeader("Cookie", cookie)
                .addHeader("Host", "lightapp.weishao.com.cn")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
