package com.google.android.exoplayer2.demo;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import java.io.IOException;

public class CustomTextLoadErrorHandlingPolicy extends DefaultLoadErrorHandlingPolicy {

    @Override
    public long getRetryDelayMsFor(
            int dataType,
            long loadDurationMs,
            IOException exception,
            int errorCount) {
        if (exception instanceof HttpDataSource.InvalidResponseCodeException) {
            HttpDataSource.InvalidResponseCodeException invalidResponseCodeException = (HttpDataSource.InvalidResponseCodeException) exception;
            if (invalidResponseCodeException != null &&
                    invalidResponseCodeException.dataSpec != null &&
                    invalidResponseCodeException.dataSpec.uri != null &&
                    invalidResponseCodeException.dataSpec.uri.getLastPathSegment() != null) {

                String lastPathSegment = invalidResponseCodeException.dataSpec.uri.getLastPathSegment();
                if (lastPathSegment.endsWith(".vtt") || lastPathSegment.endsWith(".srt")) {
                    return Long.MAX_VALUE;
                }
            }
        }
        return C.TIME_UNSET;
    }

    @Override
    public int getMinimumLoadableRetryCount(int dataType) {
        return Integer.MAX_VALUE;
    }

//    @Override
//    public long getBlacklistDurationMsFor(
//            int dataType,
//            long loadDurationMs,
//            IOException exception,
//            int errorCount) {
//        if (exception instanceof HttpDataSource.InvalidResponseCodeException) {
//            HttpDataSource.InvalidResponseCodeException invalidResponseCodeException = (HttpDataSource.InvalidResponseCodeException) exception;
//            if (invalidResponseCodeException != null &&
//                    invalidResponseCodeException.dataSpec != null &&
//                    invalidResponseCodeException.dataSpec.uri != null &&
//                    invalidResponseCodeException.dataSpec.uri.getLastPathSegment() != null) {
//
//                String lastPathSegment = invalidResponseCodeException.dataSpec.uri.getLastPathSegment();
//                if (lastPathSegment.endsWith(".vtt") || lastPathSegment.endsWith(".srt")) {
//                    return Consts.TIME_UNSET;                }
//            }
//        }
//        return super.getBlacklistDurationMsFor(
//                dataType, loadDurationMs, exception, errorCount);
//    }
}
