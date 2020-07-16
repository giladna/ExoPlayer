package com.google.android.exoplayer2.demo;

import android.os.Handler;
import android.os.Looper;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import java.io.IOException;

public class CustomTextLoadErrorHandlingPolicy extends DefaultLoadErrorHandlingPolicy {

    public static final int TRACK_TYPE_TEXT = 2;

    DefaultTrackSelector trackSelector;
    public CustomTextLoadErrorHandlingPolicy(DefaultTrackSelector trackSelector) {
        this.trackSelector = trackSelector;
    }

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
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (trackSelector != null) {
                                trackSelector.setParameters(
                                    trackSelector
                                        .buildUponParameters()
                                        .setRendererDisabled(TRACK_TYPE_TEXT, true));
                            }
                        }
                    });

                    return Long.MAX_VALUE; //C.TIME_UNSET;//
                }
            }
        }
        return super.getRetryDelayMsFor(
            dataType, loadDurationMs, exception, errorCount);
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
