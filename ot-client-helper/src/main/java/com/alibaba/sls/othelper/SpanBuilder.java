package com.alibaba.sls.othelper;

public interface SpanBuilder {
    SpanBuilder withAttribute(String key, String value);

    SpanBuilder withResource(String key, String value);

    SpanBuilder withKind(SpanKind kind);

    Span start();
}
