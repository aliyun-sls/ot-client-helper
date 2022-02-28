package com.alibaba.sls.othelper;

import java.util.concurrent.atomic.AtomicBoolean;

public class OpenTelemetryHelper {

    private static Configuration configuration;
    private static AtomicBoolean initialized = new AtomicBoolean(false);

    public static void Init(String ak, String sk, String project, String instanceId) {
        Init(ak, sk, project, instanceId, "W3C");
    }

    public static void Init(String ak, String sk, String project, String instanceId, String propagationContext) {
        configuration = new Configuration(ak, sk, project, instanceId, propagationContext);
        initialized.compareAndSet(false, true);
    }

    public static SpanBuilder createSpan(String name) {
        if (initialized.get() && configuration != null) {
            return new SpanBuilderImpl(name);
        }
        return new NoopSpanBuilder();
    }
}
