package com.alibaba.sls.othelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SpanBuilderImpl implements SpanBuilder {
    private final String name;
    private SpanKind kind = SpanKind.INTERNAL;
    private final Map<String, String> attributes = new ConcurrentHashMap<>();
    private final Map<String, String> resources = new ConcurrentHashMap<>();

    SpanBuilderImpl(String name) {
        this.name = name;
    }

    @Override
    public SpanBuilder withAttribute(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    @Override
    public SpanBuilder withResource(String key, String value) {
        resources.put(key, value);
        return this;
    }

    @Override
    public SpanBuilder withKind(SpanKind kind) {
        this.kind = kind;
        return this;
    }

    @Override
    public Span start() {
        return new SpanImpl(name, kind, attributes, resources);
    }
}
