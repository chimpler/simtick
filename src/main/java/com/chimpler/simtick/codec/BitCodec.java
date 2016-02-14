package com.chimpler.simtick.codec;

public class BitCodec {
    byte[] buffer = new byte[9];

    public long read(byte[] array, int pos, int len) {
        long value = 0;
        int offset = pos % 8;
        for (int i = 0; i < len; i++) {
            value = (value << 1) | ((array[(i + pos) / 8] >> (len + (8 - offset) - i - 1) % 8) & 0x01);
        }
        return value;
    }

    public void write(byte[] array, long value, int pos, int len) {
        int offset = pos % 8;
        java.util.Arrays.fill(buffer, (byte)0);
        for (int i = 0; i < len; i++) {
            buffer[(i + offset) / 8] |= (byte)((value >> ((len - i - 1))) & 0x1) << ((len + (8 - offset) - i - 1) % 8);
        }

        for (int i = 0; i < 9; i++) {
            array[i + pos / 8] |= buffer[i];
        }
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
