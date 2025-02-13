package com.yunyou.common.http;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

public class MyCallback implements FutureCallback<HttpResponse> {

    @Override
    public void completed(HttpResponse result) {

    }

    @Override
    public void failed(Exception ex) {

    }

    @Override
    public void cancelled() {

    }
}
