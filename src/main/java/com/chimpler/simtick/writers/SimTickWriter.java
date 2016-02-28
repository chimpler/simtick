package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.BitCodec;

import java.io.OutputStream;

public class SimTickWriter {
    public static final int DEFAULT_BUFFER_SIZE = 8096;

    private Writer[] writers;
    private boolean deltaEnabled;
    private boolean rowDelta;
    private OutputStream outputStream;
    private byte[] buffer;

    public SimTickWriter(Writer[] writers, OutputStream outputStream, boolean deltaEnabled, boolean rowDelta) {
        this(writers, outputStream, deltaEnabled, rowDelta, DEFAULT_BUFFER_SIZE);
    }

    public SimTickWriter(Writer[] writers, OutputStream outputStream, boolean deltaEnabled, boolean rowDelta, int bufferSize) {
        this.writers = writers;
        this.deltaEnabled = deltaEnabled;
        this.rowDelta = rowDelta;
        this.outputStream = outputStream;
        this.buffer = new byte[bufferSize];
    }

    protected boolean isRowDelta(Object[] values) {
        for (int i = 0; i < writers.length; i++) {
            Writer writer = writers[i];
            Object value = values[i];
            if (!writer.fixed && !writer.isDelta(value)) {
                return false;
            }
        }
        return true;
    }

    public int writeValues(byte[] output, Object[] values, int srcOffset) {
        int offset = srcOffset;
        // check if all values can be delta-ed or not
        boolean isDelta;

        if (deltaEnabled && rowDelta) {
            isDelta = isRowDelta(values);
            BitCodec.write(output, isDelta ? 1 : 0, offset++, 1);
        }

        for (int i = 0; i < writers.length; i++) {
            Writer writer = writers[i];
            Object value = values[i];
            if (deltaEnabled && !rowDelta && !writer.fixed) {
                isDelta = writer.isDelta(value);
                BitCodec.write(output, isDelta ? 1 : 0, offset++, 1);
            } else {
                isDelta = false;
            }

            offset += isDelta ? writer.writeDelta(output, value, offset) : writer.writeRaw(output, value, offset);
        }
        return offset - srcOffset;
    }
}
