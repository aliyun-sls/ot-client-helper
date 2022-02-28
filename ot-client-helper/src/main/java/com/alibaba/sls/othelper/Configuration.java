package com.alibaba.sls.othelper;

import android.content.Context;

class Configuration {
    private final Context context;
    private final String accessKey;
    private final String securityKey;
    private final String project;
    private final String instanceId;
    private final String traceLogstore;

    Configuration(Context context, String accessKey, String securityKey, String project, String instanceId, String propagationContext) {
        this.context = context;
        this.accessKey = accessKey;
        this.securityKey = securityKey;
        this.project = project;
        this.instanceId = instanceId;
        this.traceLogstore = instanceId + "-traces";
    }

    Context getContext() {
        return this.context;
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
