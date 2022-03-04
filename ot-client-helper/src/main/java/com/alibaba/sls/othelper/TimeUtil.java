package com.alibaba.sls.othelper;

import android.os.SystemClock;

public class TimeUtil {
    private static long diffTime = 0;
    private static boolean isServerTime = false;

    public synchronized static long getServerTime() {
        if (!isServerTime) {
            return System.currentTimeMillis() / 1000;
        }
        return (diffTime + SystemClock.elapsedRealtime()) / 1000;
    }

    private synchronized static long calibrationTime(long lastServerTime) {
        diffTime = lastServerTime - SystemClock.elapsedRealtime();
        isServerTime = true;
        return lastServerTime;
    }
    
//    OKHttp Interceptor示例代码
//    public static class TimeSyncInterceptor implements Interceptor {
//        long minResponseTime = Long.MAX_VALUE;
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            long startTime = System.nanoTime();
//            Response proceed = chain.proceed(request);
//            long lastTime = System.nanoTime() - startTime;
//
//            Headers headers = proceed.headers();
//            calibration(lastTime, headers);
//            return proceed;
//        }
//
//        private void calibration(long responseTime, Headers headers) {
//            if (headers == null) {
//                return;
//            }
//            if (responseTime >= minResponseTime) {
//                return;
//            }
//
//            String standardTime = headers.get("Date");
//            if (!TextUtils.isEmpty(standardTime)) {
//
//                Date parse = HttpDate.parse(standardTime);
//
//                if (parse != null) {
//                    TimeUtil.calibrationTime(parse.getTime());
//                    minResponseTime = responseTime;
//                }
//            }
//        }
//    }
}
