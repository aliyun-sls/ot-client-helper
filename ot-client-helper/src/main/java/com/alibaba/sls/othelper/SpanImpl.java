package com.alibaba.sls.othelper;

import static com.alibaba.sls.othelper.Constants.FIELD_ATTRIBUTE;
import static com.alibaba.sls.othelper.Constants.FIELD_DURATION;
import static com.alibaba.sls.othelper.Constants.FIELD_END;
import static com.alibaba.sls.othelper.Constants.FIELD_HOST;
import static com.alibaba.sls.othelper.Constants.FIELD_KIND;
import static com.alibaba.sls.othelper.Constants.FIELD_NAME;
import static com.alibaba.sls.othelper.Constants.FIELD_PARENT_SPAN_ID;
import static com.alibaba.sls.othelper.Constants.FIELD_RESOURCE;
import static com.alibaba.sls.othelper.Constants.FIELD_SERVICE;
import static com.alibaba.sls.othelper.Constants.FIELD_SPAN_ID;
import static com.alibaba.sls.othelper.Constants.FIELD_START;
import static com.alibaba.sls.othelper.Constants.FIELD_STATUS_CODE;
import static com.alibaba.sls.othelper.Constants.FIELD_TRACE_ID;
import static com.alibaba.sls.othelper.OpenTelemetryHelper.globalConfiguration;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aliyun.sls.android.producer.Log;

import org.json.JSONObject;

final class SpanImpl implements Span {
    private AtomicBoolean finished = new AtomicBoolean();
    private Map<String, String> commonsValues = new ConcurrentHashMap<>();
    private Map<String, String> attributes = new ConcurrentHashMap<>();
    private Map<String, String> resources = new ConcurrentHashMap<>();
    private long startTime;


    SpanImpl(final String name) {
        this(name, SpanKind.INTERNAL, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    SpanImpl(String name, SpanKind kind, Map<String, String> attributes, Map<String, String> resources) {
        commonsValues.put(FIELD_TRACE_ID, SpanHelper.generateTraceId());
        commonsValues.put(FIELD_SPAN_ID, SpanHelper.generateSpanId());
        commonsValues.put(FIELD_PARENT_SPAN_ID, "");
        commonsValues.put(FIELD_NAME, name);
        commonsValues.put(FIELD_SERVICE, globalConfiguration().serviceName());
        commonsValues.put(FIELD_STATUS_CODE, "OK");
        commonsValues.put(FIELD_KIND, kind.name());
        this.resources.putAll(globalConfiguration().resources());
        this.resources.putAll(resources);
        this.attributes.putAll(attributes);
        startTime = SpanHelper.getCurrentEpochNanos();
    }

    @Override
    public Span withAttribute(String key, String value) {
        if (finished.get()) {
            return this;
        }
        attributes.put(key, value);
        return this;
    }

    @Override
    public Span withResource(String key, String value) {
        if (finished.get()) {
            return this;
        }
        resources.put(key, value);
        return this;
    }

    @Override
    public Span withHost(String host) {
        commonsValues.put(FIELD_HOST, host);
        return this;
    }

    @Override
    public Span occurError() {
        if (finished.get()) {
            commonsValues.put(FIELD_STATUS_CODE, "ERROR");
        }
        return this;
    }

    @Override
    public TraceContext traceContext() {
        return new W3CTraceContext(this);
    }

    @Override
    public void stop() {
        if (finished.get()) {
            return;
        }

        if (finished.compareAndSet(false, true)) {
            long endEpoch = SpanHelper.getCurrentEpochNanos();

            commonsValues.put(FIELD_DURATION, String.valueOf(endEpoch - startTime));
            commonsValues.put(FIELD_END, String.valueOf(endEpoch));
            commonsValues.put(FIELD_START, String.valueOf(startTime));
            commonsValues.put(FIELD_RESOURCE, new JSONObject(resources).toString());
            commonsValues.put(FIELD_ATTRIBUTE, new JSONObject(attributes).toString());

            Log log = new Log();
            log.putContents(commonsValues);
            OpenTelemetryHelper.send(log);
        }
    }

    @Override
    public String traceID() {
        return commonsValues.get(FIELD_TRACE_ID);
    }

    @Override
    public String spanID() {
        return commonsValues.get(FIELD_SPAN_ID);
    }

    @Override
    public Span spanKind(SpanKind kind) {
        if (!finished.get()) {
            return this;
        }
        commonsValues.put(FIELD_KIND, kind.getName());
        return this;
    }

}
