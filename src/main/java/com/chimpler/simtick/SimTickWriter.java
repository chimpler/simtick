package com.chimpler.simtick;

import com.chimpler.simtick.codec.BitCodec;
import com.chimpler.simtick.writers.Writer;

public class SimTickWriter {
    private Writer[] writers;
    private BitCodec bitCodec;

    public SimTickWriter(Writer[] writers) {
        this.writers = writers;
        this.bitCodec = new BitCodec();
    }

    private int writeValues(byte[] buffer, Object[] values, int srcOffset, boolean isDelta) {
        int rowLen = 0;
        int offset = srcOffset;
        for (int i = 0; i < writers.length; i++) {
            Writer writer = writers[i];
            Object value = values[i];
            int len = isDelta ? writer.writeDelta(buffer, value, offset) : writer.writeRaw(buffer, value, offset);
            rowLen += len;
            offset += len;
        }
        return rowLen;
    }

    private boolean isDelta(Object[] values) {
        for (int i = 0; i < writers.length; i++) {
            Writer writer = writers[i];
            Object value = values[i];
            if (!writer.isDelta(value)) {
                return false;
            }
        }
        return true;
    }

    public int write(byte[] buffer, Object[] values, int targetOffset) {
        // check if all values can be delta-ed or not
        boolean isDelta = isDelta(values);
        int offset = targetOffset;

        long deltaBit = isDelta ? 1:0;
        bitCodec.write(buffer, deltaBit, offset++, 1);
        return 1 + writeValues(buffer, values, offset, isDelta);
    }
}
