package com.alibaba.sls.othelper;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aliyun.sls.android.producer.Log;

final class SpanImpl implements Span {
    private AtomicBoolean finished = new AtomicBoolean();

    SpanImpl() {
    }

    @Override
    public Span withAttribute(String key, String value) {
        if (finished.get()) {
            return this;
        }
        return this;
    }

    @Override
    public Span withResource(String key, String value) {
        if (finished.get()) {
            return this;
        }

        return this;
    }

    @Override
    public TraceContext traceContext() {
        return null;
    }

    @Override
    public void stop() {
        if (!finished.get()) {
            return;
        }

        if (finished.compareAndSet(false, true)) {
            // send data
            Log log = new Log();
            OpenTelemetryHelper.send(log);
        }
    }

    @Override
    public Span spanKind(SpanKind kind) {
        if (!finished.get()) {
            return this;
        }
        return this;
    }

}
