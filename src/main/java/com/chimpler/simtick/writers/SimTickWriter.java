package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.BitCodec;

public class SimTickWriter {
    private Writer[] writers;
    private BitCodec bitCodec;
    private boolean deltaEnabled;
    private boolean rowDelta;

    public SimTickWriter(Writer[] writers, boolean deltaEnabled, boolean rowDelta) {
        this.writers = writers;
        this.bitCodec = new BitCodec();
        this.deltaEnabled = deltaEnabled;
        this.rowDelta = rowDelta;
    }

    private boolean isRowDelta(Object[] values) {
        for (int i = 0; i < writers.length; i++) {
            Writer writer = writers[i];
            Object value = values[i];
            if (!writer.fixed && !writer.isDelta(value)) {
                return false;
            }
        }
        return true;
    }

    public int write(byte[] buffer, Object[] values, int srcOffset) {

        int offset = srcOffset;
        // check if all values can be delta-ed or not
        boolean isDelta;

        if (deltaEnabled && rowDelta) {
            isDelta = isRowDelta(values);
            bitCodec.write(buffer, isDelta ? 1 : 0, offset++, 1);
        }

        for (int i = 0; i < writers.length; i++) {
            Writer writer = writers[i];
            Object value = values[i];
            if (deltaEnabled && !rowDelta && !writer.fixed) {
                isDelta = writer.isDelta(value);
                bitCodec.write(buffer, isDelta ? 1 : 0, offset++, 1);
            } else {
                isDelta = false;
            }

            offset += isDelta ? writer.writeDelta(buffer, value, offset) : writer.writeRaw(buffer, value, offset);
        }
        return offset - srcOffset;
    }
}
