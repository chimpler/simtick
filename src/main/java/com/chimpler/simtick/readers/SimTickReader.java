package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;

public class SimTickReader {
    private Reader[] readers;
    private boolean deltaEnabled;
    private boolean rowDelta;

    public SimTickReader(Reader[] readers, boolean deltaEnabled, boolean rowDelta) {
        this.readers = readers;
        this.deltaEnabled = deltaEnabled;
        this.rowDelta = rowDelta;
    }

    public int read(byte[] buffer, int srcOffset, Object[] result) {
        int offset = srcOffset;
        boolean isDelta = false;
        if (deltaEnabled && rowDelta) {
            isDelta = BitCodec.read(buffer, offset++, 1) == 1;
        }

        for (int i = 0; i < readers.length; i++) {
            Reader reader = readers[i];
            if (deltaEnabled && !rowDelta && !reader.fixed) {
                isDelta = BitCodec.read(buffer, offset++, 1) == 1;
            }
            ValueAndLength valueAndLength = isDelta ? reader.readDelta(buffer, offset) : reader.readRaw(buffer, offset);
            result[i] = valueAndLength.value;
            offset += valueAndLength.length;
        }
        return offset - srcOffset;
    }
}
