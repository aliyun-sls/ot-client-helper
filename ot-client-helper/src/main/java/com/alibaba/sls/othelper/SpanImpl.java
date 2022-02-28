package com.alibaba.sls.othelper;

import java.util.concurrent.atomic.AtomicBoolean;

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
