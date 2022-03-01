package com.alibaba.sls.othelper;

import android.content.Context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Configuration {
    private final Context context;
    private final String accessKey;
    private final String securityKey;
    private final String project;
    private final String instanceId;
    private final String traceLogstore;
    private final String endpoint;
    private final String propagationContext;
    private final String serviceName;
    private Map<String, String> resources;

    Configuration(Context context, String accessKey, String securityKey, String endpoint, String project, String instanceId, String serviceName) {
        this(context, accessKey, securityKey, endpoint, project, instanceId, serviceName, new ConcurrentHashMap<>(), "W3C");
    }

    Configuration(Context context, String accessKey, String securityKey, String endpoint, String project, String instanceId, String serviceName, Map<String, String> resources) {
        this(context, accessKey, securityKey, endpoint, project, instanceId, serviceName, resources, "W3C");
    }

    Configuration(Context context, String accessKey, String securityKey, String endpoint,
                  String project, String instanceId, String serviceName, Map<String, String> resources, String propagationContext) {
        this.context = context;
        this.accessKey = accessKey;
        this.securityKey = securityKey;
        this.project = project;
        this.instanceId = instanceId;
        this.traceLogstore = instanceId + "-traces";
        this.endpoint = endpoint;
        this.serviceName = serviceName;
        this.propagationContext = propagationContext;
        this.resources = new ConcurrentHashMap<>();
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

    String endpoint() {
        return this.endpoint;
    }

    public String serviceName() {
        return serviceName;
    }

    public Map<String, String> resources() {
        return resources;
    }

    public static class Builder {
        private final Context context;
        private final String ak;
        private final String sk;
        private final String endpoint;
        private final String project;
        private final String instanceId;
        private String serviceName;
        private Map<String, String> resources = new ConcurrentHashMap<>();

        public static Builder newBuilder(Context context, String ak, String sk, String endpoint, String project, String instanceId, String serviceName) {
            return new Builder(context, ak, sk, endpoint, project, instanceId, serviceName);
        }

        public static Builder newBuilder(Context context, String ak, String sk, String endpoint, String project, String instanceId) {
            return new Builder(context, ak, sk, endpoint, project, instanceId);
        }

        private Builder(Context context, String ak, String sk, String endpoint, String project, String instanceId, String serviceName) {
            this.context = context;
            this.ak = ak;
            this.sk = sk;
            this.endpoint = endpoint;
            this.project = project;
            this.instanceId = instanceId;
            this.serviceName = serviceName;
        }

        private Builder(Context context, String ak, String sk, String endpoint, String project, String instanceId) {
            this(context, ak, sk, endpoint, project, instanceId, "Android");
        }

        public Builder withServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder withResources(Map<String, String> resources) {
            this.resources.putAll(resources);
            return this;
        }

        public Configuration build() {
            return new Configuration(context, ak, sk, endpoint, project, instanceId, serviceName, this.resources);
        }
    }
}
