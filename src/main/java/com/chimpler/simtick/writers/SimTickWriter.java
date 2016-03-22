package com.chimpler.simtick.writers;

import com.chimpler.simtick.Version;
import com.chimpler.simtick.codec.BitCodec;

import java.io.IOException;
import java.io.OutputStream;

public class SimTickWriter {
    public static final int DEFAULT_BUFFER_SIZE = 8096;

    private Writer[] writers;
    private boolean deltaEnabled;
    private boolean rowDelta;
    private OutputStream outputStream;
    private byte[] buffer;
    private int bufferOffset = 0;
    private final int maxSize;

    public SimTickWriter(Writer[] writers, OutputStream outputStream, boolean deltaEnabled, boolean rowDelta) {
        this(writers, outputStream, deltaEnabled, rowDelta, DEFAULT_BUFFER_SIZE);
    }

    public SimTickWriter(Writer[] writers, OutputStream outputStream, boolean deltaEnabled, boolean rowDelta, int bufferSize) {
        this.writers = writers;
        this.deltaEnabled = deltaEnabled;
        this.rowDelta = rowDelta;
        this.outputStream = outputStream;
        this.buffer = new byte[bufferSize];
        this.maxSize = computeMaxSize();
        bufferOffset = writeHeader(buffer, 0);
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

    public int writeHeader(byte[] output, int srcOffset) {
        int offset = srcOffset;
        offset += BitCodec.write(output, Version.DataVersion, srcOffset, 16);
        offset += BitCodec.write(output, writers.length, srcOffset, 8);
        for (Writer writer: writers) {
            offset += writer.writerHeader(output, srcOffset);
        }
        return offset - srcOffset;
    }

    public int write(Object[] values) throws IOException {
        int len = writeValues(buffer, values, bufferOffset);
        bufferOffset += len;
        if (bufferOffset > buffer.length - maxSize) {
            outputStream.write(buffer, 0, bufferOffset);
            bufferOffset = 0;
        }

        return len;
    }

    private int computeMaxSize() {
        int len = 0;
        // check if all values can be delta-ed or not

        if (deltaEnabled && rowDelta) {
            len++;
        }

        for (Writer writer: writers) {
            if (deltaEnabled && !rowDelta && !writer.fixed) {
                len++;
            }

            len += writer.getMaxSize();
        }
        return len;
    }
}
