package com.alibaba.sls.othelper;

enum NoopTraceContext implements TraceContext {
    INSTANCE;

    @Override
    public String contextKey() {
        return "noop-tracing-context";
    }

    @Override
    public String contextValue() {
        return "";
    }
}
