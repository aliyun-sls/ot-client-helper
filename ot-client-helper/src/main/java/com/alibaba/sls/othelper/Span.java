package com.alibaba.sls.othelper;

public interface Span {

    Span spanKind(SpanKind kind);

    Span withAttribute(String key, String value);

    Span withResource(String key, String value);

    TraceContext traceContext();

    void stop();
}
