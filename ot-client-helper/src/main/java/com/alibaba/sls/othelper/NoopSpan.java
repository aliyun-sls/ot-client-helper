package com.alibaba.sls.othelper;

enum NoopSpan implements Span {
    INSTANCE;

    @Override
    public Span withAttribute(String key, String value) {
        return this;
    }


    @Override
    public Span withResource(String key, String value) {
        return this;
    }

    @Override
    public Span withHost(String host) {
        return this;
    }

    @Override
    public Span occurError() {
        return this;
    }

    @Override
    public TraceContext traceContext() {
        return NoopTraceContext.INSTANCE;
    }

    @Override
    public void stop() {
    }

    @Override
    public String traceID() {
        return "";
    }

    @Override
    public String spanID() {
        return "";
    }

    @Override
    public Span spanKind(SpanKind kind) {
        return this;
    }

}
