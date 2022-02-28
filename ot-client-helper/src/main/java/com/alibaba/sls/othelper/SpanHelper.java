package com.alibaba.sls.othelper;

import java.util.Random;
import java.util.concurrent.TimeUnit;

final class SpanHelper {

    static final int BYTE_BASE16 = 2;
    private static final String ALPHABET = "0123456789abcdef";
    private static final char[] ENCODING = buildEncodingArray();
    private static final long INVALID_ID = 0;

    static long getCurrentEpochNanos() {
        long startEpochNanos = System.currentTimeMillis();
        long s1Nanos = System.nanoTime();
        long s2Nanos = System.nanoTime();
        return TimeUnit.MILLISECONDS.toNanos(startEpochNanos) + (s2Nanos - s1Nanos);
    }

    static final String generateTraceId() {
        Random random = new Random();
        long idHi = random.nextLong();
        long idLo;
        do {
            idLo = random.nextLong();
        } while (idLo == INVALID_ID);
        return generateTraceId(idHi, idLo);
    }

    static final String generateSpanId() {
        long id;
        Random random = new Random();
        do {
            id = random.nextLong();
        } while (id == INVALID_ID);
        return generateSpanId(id);
    }

    private static String generateSpanId(long id) {
        if (id == 0) {
            return "0000000000000000";
        }

        char[] result = new char[16];
        toHex(id, result, 0);
        return new String(result, 0, 8);
    }

    private static String generateTraceId(long traceIdLongHighPart, long traceIdLongLowPart) {
        if (traceIdLongHighPart == 0 && traceIdLongLowPart == 0) {
            return "00000000000000000000000000000000";
        }
        char[] chars = new char[32];
        toHex(traceIdLongHighPart, chars, 0);
        toHex(traceIdLongLowPart, chars, 16);
        return new String(chars, 0, 16);
    }

    private static void toHex(long value, char[] dest, int destOffset) {
        byteToBase16((byte) (value >> 56 & 0xFFL), dest, destOffset);
        byteToBase16((byte) (value >> 48 & 0xFFL), dest, destOffset + BYTE_BASE16);
        byteToBase16((byte) (value >> 40 & 0xFFL), dest, destOffset + 2 * BYTE_BASE16);
        byteToBase16((byte) (value >> 32 & 0xFFL), dest, destOffset + 3 * BYTE_BASE16);
        byteToBase16((byte) (value >> 24 & 0xFFL), dest, destOffset + 4 * BYTE_BASE16);
        byteToBase16((byte) (value >> 16 & 0xFFL), dest, destOffset + 5 * BYTE_BASE16);
        byteToBase16((byte) (value >> 8 & 0xFFL), dest, destOffset + 6 * BYTE_BASE16);
        byteToBase16((byte) (value & 0xFFL), dest, destOffset + 7 * BYTE_BASE16);
    }

    private static char[] buildEncodingArray() {
        char[] encoding = new char[512];
        for (int i = 0; i < 256; ++i) {
            encoding[i] = ALPHABET.charAt(i >>> 4);
            encoding[i | 0x100] = ALPHABET.charAt(i & 0xF);
        }
        return encoding;
    }

    private static void byteToBase16(byte value, char[] dest, int destOffset) {
        int b = value & 0xFF;
        dest[destOffset] = ENCODING[b];
        dest[destOffset + 1] = ENCODING[b | 0x100];
    }

}
