package com.common.http;

import com.facebook.imagepipeline.producers.BaseNetworkFetcher;

/**
 * @module OkHttp的图片网络加载
 */
public class CustomOkHttpNetworkFetcher
        /*extends BaseNetworkFetcher<OKHttFe.OkHttpNetworkFetchState>*/ {
//    private static final String TAG = "CustomOkHttpNetworkFetcher";
//    private static final String QUEUE_TIME = "queue_time";
//    private static final String FETCH_TIME = "fetch_time";
//    private static final String TOTAL_TIME = "total_time";
//    private static final String IMAGE_SIZE = "image_size";
//
//    CacheControl mCacheControl;
//
//    private final OkHttpClient mOkHttpClient;
//    private Executor mCancellationExecutor;
//    private Url2IpManager mUrl2IpManager;
//
//    public CustomOkHttpNetworkFetcher(OkHttpClient okHttpClient) {
//        this.mOkHttpClient = okHttpClient;
//        this.mCancellationExecutor = okHttpClient.dispatcher().executorService();
//        mCacheControl = new CacheControl.Builder().noStore().build();
//        mUrl2IpManager = new Url2IpManager();
//    }
//
//    @Override
//    public OkHttpNetworkFetcher.OkHttpNetworkFetchState createFetchState(Consumer<EncodedImage> consumer, ProducerContext context) {
//        MyLog.d(TAG, "createFetchState" + " consumer=" + consumer + " context=" + context);
//        return new OkHttpNetworkFetcher.OkHttpNetworkFetchState(consumer, context);
//    }
//
//    @Override
//    public void fetch(final OkHttpNetworkFetcher.OkHttpNetworkFetchState fetchState, final Callback callback) {
//        MyLog.d(TAG, "fetch" + " fetchState=" + fetchState + " callback=" + callback);
//        fetchState.submitTime = SystemClock.elapsedRealtime();
//        Uri uri = fetchState.getUri();
//        String url = uri.toString();
//        work(fetchState, callback, url, 0, 0);
//    }
//
//    private void work(final OkHttpNetworkFetcher.OkHttpNetworkFetchState fetchState, final Callback callback, String url, int index, int callNum) {
//        MyLog.d(TAG, "当前线程名1:" + Thread.currentThread().getName() + "url:" + url);
//        if (callNum >= 10) {
//            //做个保护，避免死递归
//            return;
//        }
//        String host = Uri.parse(url).getHost();
//        String urlWithIp = mUrl2IpManager.getNextAvailableUrl(url, index);
//        if (TextUtils.isEmpty(urlWithIp)) {
//            urlWithIp = url;
//        }
//        MyLog.d(TAG, "ThreadId=" + Thread.currentThread().getId() + ",ThreadNam:" + Thread.currentThread().getName()
//                + ",urlWithIp:" + urlWithIp);
//
//        final Request.Builder requestBuilder = new Request.Builder()
//                .header("host", host)
//                .header("User-Agent", U.getHttpUtils().buildUserAgent())
//                .url(urlWithIp)
//                .get();
//
//        if (mCacheControl != null) {
//            requestBuilder.cacheControl(mCacheControl);
//        }
//
//        final BytesRange bytesRange = fetchState.getContext().getImageRequest().getBytesRange();
//        if (bytesRange != null) {
//            requestBuilder.addHeader("Range", bytesRange.toHttpRangeHeaderValue());
//        }
//
//
//        final Call call = this.mOkHttpClient.newCall(requestBuilder.build());
//        fetchState
//                .getContext()
//                .addCallbacks(new BaseProducerContextCallbacks() {
//                    @Override
//                    public void onCancellationRequested() {
//                        if (Looper.myLooper() != Looper.getMainLooper()) {
//                            call.cancel();
//                        } else {
//                            CustomOkHttpNetworkFetcher.this.mCancellationExecutor.execute(new Runnable() {
//                                @Override
//                                public void run() {
//                                    call.cancel();
//                                }
//                            });
//                        }
//                    }
//                });
//        call.enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // 如果有保底ip，再尝试
//                String ip = mUrl2IpManager.getNextAvailableUrl(url, index + 1);
//                MyLog.w(TAG, "onFailure" + " ip=" + ip + ",url=" + url);
//                if (!TextUtils.isEmpty(ip)) {
//                    work(fetchState, callback, url, index + 1, callNum + 1);
//                } else {
//                    CustomOkHttpNetworkFetcher.this.handleException(call, e, callback);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                fetchState.responseTime = SystemClock.elapsedRealtime();
//                ResponseBody body = response.body();
//                try {
//                    MyLog.d(TAG, "onResponse 当前线程名:" + Thread.currentThread().getName() + ", ResponseBody=" + body.toString());
//                    long e = body.contentLength();
//                    if (e < 0L) {
//                        e = 0L;
//                    }
//                    String nextUriString = response.header("Location");//处理302跳转
//                    String nextScheme = nextUriString == null ? null : Uri.parse(nextUriString).getScheme();
//                    if (nextUriString == null || nextScheme == null) {
//                        callback.onResponse(body.byteStream(), (int) e);
//                    } else {
//                        MyLog.w(TAG, "download with 302 nextUriString=" + nextUriString);
//                        work(fetchState, callback, nextUriString, 0, callNum + 1);
//                    }
//                } catch (Exception var13) {
//                    // 如果有保底ip，再尝试
//                    String ip = mUrl2IpManager.getNextAvailableUrl(url, index + 1);
//                    if (!TextUtils.isEmpty(ip)) {
//                        work(fetchState, callback, url, index + 1, callNum + 1);
//                    } else {
//                        CustomOkHttpNetworkFetcher.this.handleException(call, var13, callback);
//                    }
//                } finally {
//                    try {
//                        body.close();
//                    } catch (Exception var12) {
//                        MyLog.w("OkHttpNetworkFetchProducer", "Exception when closing response body", var12);
//                    }
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onFetchCompletion(OkHttpNetworkFetcher.OkHttpNetworkFetchState fetchState, int byteSize) {
//        fetchState.fetchCompleteTime = SystemClock.elapsedRealtime();
//    }
//
//    @Override
//    public Map<String, String> getExtraMap(OkHttpNetworkFetcher.OkHttpNetworkFetchState fetchState, int byteSize) {
//        HashMap extraMap = new HashMap(4);
//        extraMap.put("queue_time", Long.toString(fetchState.responseTime - fetchState.submitTime));
//        extraMap.put("fetch_time", Long.toString(fetchState.fetchCompleteTime - fetchState.responseTime));
//        extraMap.put("total_time", Long.toString(fetchState.fetchCompleteTime - fetchState.submitTime));
//        extraMap.put("image_size", Integer.toString(byteSize));
//        return extraMap;
//    }
//
//    private void handleException(Call call, Exception e, Callback callback) {
//        MyLog.d(TAG, "handleException 当前线程名:" + Thread.currentThread().getName());
//        if (call.isCanceled()) {
//            callback.onCancellation();
//        } else {
//            callback.onFailure(e);
//        }
//
//    }

}
