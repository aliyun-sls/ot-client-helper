package com.alibaba.sls.othelper;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;

import com.aliyun.sls.android.producer.Log;
import com.aliyun.sls.android.producer.LogProducerClient;
import com.aliyun.sls.android.producer.LogProducerConfig;
import com.aliyun.sls.android.producer.LogProducerException;

public class OpenTelemetryHelper {

    private static Configuration configuration;
    private static LogProducerClient producerClient;
    private static AtomicBoolean initialized = new AtomicBoolean(false);
    private static Map<String, String> GLOBAL_RESOURCE = new ConcurrentHashMap<>();

    public static void Init(Configuration.Builder builder) {
        if (initialized.compareAndSet(false, true)) {
            configuration = builder.build();
            initLogProducer(configuration);
        }
    }

    @Deprecated
    public static void Init(Context context, String ak, String sk, String endpoint, String project, String instanceId, String serviceName) {
        Init(context, ak, sk, endpoint, project, instanceId, serviceName, "W3C");
    }

    @Deprecated
    public static void Init(Context context, String ak, String sk, String endpoint, String project, String instanceId, String serviceName,
                            String propagationContext) {
        if (initialized.compareAndSet(false, true)) {
            configuration = new Configuration(context, ak, sk, endpoint, project, instanceId, serviceName);
            initLogProducer(configuration);
        }
    }

    private static void initLogProducer(Configuration configuration) {
        final Context context = configuration.getContext().getApplicationContext();
        final File path = new File(context.getFilesDir(), String.format("%s/%s", File.separator, "trace_data.dat"));

        try {
            LogProducerConfig config = new LogProducerConfig(context, configuration.project(),
                    configuration.project(),
                    configuration.traceLogStore(),
                    configuration.accessKey(),
                    configuration.securityKey());
            config.setTopic("trace");
            config.setDropDelayLog(0);
            config.setDropUnauthorizedLog(0);

            config.setPersistent(1);

            config.setPersistent(1);
            config.setPersistentFilePath(path.getAbsolutePath());
            config.setPersistentForceFlush(0);
            config.setPersistentMaxFileCount(10);
            config.setPersistentMaxFileSize(1024 * 1024);
            config.setPersistentMaxLogCount(65536);

            producerClient = new LogProducerClient(config);
        } catch (LogProducerException e) {
            e.printStackTrace();
        }
    }

    public static SpanBuilder createSpan(String name) {
        if (initialized.get() && configuration != null) {
            return new SpanBuilderImpl(name);
        }
        return new NoopSpanBuilder();
    }

    static void send(Log log) {
        producerClient.addLog(log);
    }

    public static Configuration globalConfiguration() {
        return configuration;
    }
}
