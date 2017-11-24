package com.chimpler.simtick.codec;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

public class BitCodec {

    private BitCodec() {}

    public static int readInt(byte[] buffer, int pos, int len) {
        return (int)read(buffer, pos, len);
    }

    public static int writeBoolean(byte[] buffer, boolean value, int pos) {
        return write(buffer, value ? 1 : 0, pos, 1);
    }

    public static boolean readBoolean(byte[] buffer, int pos) {
        return read(buffer, pos, 1) != 0;
    }

    public static int writeDateTime(byte[] buffer, DateTime dateTime, int millisUnit, int pos) {
        return write(buffer, dateTime.getMillis() / millisUnit, pos, 64);
    }

    public static DateTime readDateTime(byte[] buffer, int millisUnit, int pos) {
        return new DateTime(read(buffer, pos, 64) * millisUnit);
    }

    public static int write(byte[] buffer, long value, int pos, int len) {
        for (int i = 0; i < len; i++) {
            // 64 max length + 7 so it doesn't become negative
            buffer[(i + pos) / 8] |= (byte) ((value >> (len - i - 1)) & 0x1) << ((71 - pos % 8 - i) % 8);
        }
        return len;
    }

    public static long read(byte[] buffer, int pos, int len) {
        long value = 0;
        for (int i = 0; i < len; i++) {
            // 64 max length + 7 so it doesn't become negative
            value = (value << 1) | ((buffer[(i + pos) / 8] >> ((71 - pos % 8 - i) % 8)) & 0x01);
        }
        return value;
    }

    public static int writeBytes(byte[] buffer, byte[] value, int pos) {
        int offset = pos % 8;
        for (int i = 0; i < value.length; i++) {
            buffer[pos / 8 + i] |= value[i] >> offset;
            buffer[pos / 8 + i + 1] |= value[i] << (8 - offset);
        }
        return value.length;
    }

    public static byte[] readBytes(byte[] buffer, int pos, byte[] output, int numBytes) {
        int offset = pos % 8;
        for (int i = 0; i < numBytes; i++) {
            output[i] = (byte)(buffer[pos / 8 + i] << offset & 0xff | ((buffer[pos / 8 + i + 1] & 0xff) >> (8 - offset) & 0xff));
        }
        return output;
    }

    public static void print(byte[] array, int offset, int len) {
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(String.format("%02x", array[offset + i] & 0xff));
        }
        System.out.println();
    }

    public static void print(byte[] array) {
        print(array, 0, array.length);
    }

}
