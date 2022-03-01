package com.alibaba.sls.othelper;

public enum SpanKind {
    INTERNAL("internal"), CLIENT("client"), SERVER("server"), CONSUMER("consumer"), PRODUCER("producer");

    private String name;

    SpanKind(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
