package com.alibaba.sls.othelper;

import java.io.File;
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

    public static void Init(Context context, String ak, String sk, String project, String instanceId) {
        Init(context, ak, sk, project, instanceId, "W3C");
    }

    public static void Init(Context context, String ak, String sk, String project, String instanceId,
        String propagationContext) {
        configuration = new Configuration(context, ak, sk, project, instanceId, propagationContext);
        initialized.compareAndSet(false, true);
    }

    private static void initLogProducer(Configuration configuration) {
        final Context context = configuration.getContext().getApplicationContext();
        final File path = new File(context.getFilesDir(), String.format("%s/%s", File.separator, "trace_data.dat"));

        try {
            LogProducerConfig config = new LogProducerConfig(context, "endpoint",
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
}
