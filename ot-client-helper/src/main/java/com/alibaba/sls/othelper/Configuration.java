package com.alibaba.sls.othelper;

class Configuration {
    private final String accessKey;
    private final String securityKey;
    private final String project;
    private final String instanceId;
    private final String traceLogstore;

    Configuration(String accessKey, String securityKey, String project, String instanceId, String propagationContext) {
        this.accessKey = accessKey;
        this.securityKey = securityKey;
        this.project = project;
        this.instanceId = instanceId;
        this.traceLogstore = instanceId + "-traces";
    }

    String accessKey() {
        return this.accessKey;
    }

    String securityKey() {
        return this.securityKey;
    }

    String project() {
        return this.project;
    }

    String traceLogStore() {
        return this.traceLogstore;
    }

    String instanceId() {
        return this.instanceId;
    }
}
