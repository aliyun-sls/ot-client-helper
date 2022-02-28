package com.alibaba.sls.othelper;

class SpanBuilderImpl implements SpanBuilder {
    private final String name;

    SpanBuilderImpl(String name) {
        this.name = name;
    }

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
        return new SpanImpl();
    }
}
