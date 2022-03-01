package com.alibaba.sls.othelper;

final class W3CTraceContext implements TraceContext {

    private final Span span;

    public W3CTraceContext(Span span) {
        this.span = span;
    }

    @Override
    public String contextKey() {
        return "traceparent";
    }

    @Override
    public String contextValue() {
        return String.format("00-%s-%s-01", span.traceID(), span.spanID());
    }
}
