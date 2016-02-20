package com.chimpler.simtick.codec;

public class BitCodec {
    public long read(byte[] buffer, int pos, int len) {
        long value = 0;
        for (int i = 0; i < len; i++) {
            // 32 max length + 7 so it doesn't become negative
            value = (value << 1) | ((buffer[(i + pos) / 8] >> ((39 - pos % 8 - i) % 8)) & 0x01);
        }
        return value;
    }

    public void write(byte[] buffer, long value, int pos, int len) {
        for (int i = 0; i < len; i++) {
            // 32 max length + 7 so it doesn't become negative
            buffer[(i + pos) / 8] |= (byte) ((value >> (len - i - 1)) & 0x1) << ((39 - pos % 8 - i) % 8);
        }
    }

    public void writeBytes(byte[] buffer, byte[] value, int pos) {
        int offset = pos % 8;
        for (int i = 0; i < value.length; i++) {
            buffer[pos / 8 + i] |= value[i] >> offset;
            buffer[pos / 8 + i + 1] |= value[i] << (8 - offset);
        }
    }

    public byte[] readBytes(byte[] buffer, int pos, byte[] output, int numBytes) {
        int offset = pos % 8;
        for (int i = 0; i < numBytes; i++) {
            output[i] = (byte)(buffer[pos / 8 + i] << offset & 0xff | ((buffer[pos / 8 + i + 1] & 0xff) >> (8 - offset) & 0xff));
        }
        return output;
    }

    public static void print(byte[] array) {
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(String.format("%02x", array[i] & 0xff));
        }
        System.out.println();
    }
}
