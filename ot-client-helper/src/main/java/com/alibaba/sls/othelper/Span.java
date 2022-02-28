package com.alibaba.sls.othelper;

public interface Span {

    String traceID();

    String spanID();

    Span spanKind(SpanKind kind);

    Span withAttribute(String key, String value);

    Span withResource(String key, String value);

    Span withHost(String host);

    Span occurError();

    TraceContext traceContext();

    void stop();
}
