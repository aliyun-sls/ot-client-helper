package com.alibaba.sls.othelper;

public final class NoopSpanBuilder implements SpanBuilder {
    @Override
    public SpanBuilder withAttribute(String key, String value) {
        return this;
    }

    @Override
    public SpanBuilder withResource(String key, String value) {
        return this;
    }

    @Override
    public Span build() {
        return NoopSpan.INSTANCE;
    }
}
