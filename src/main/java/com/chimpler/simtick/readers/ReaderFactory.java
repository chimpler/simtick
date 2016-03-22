package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.BitCodec;
import com.chimpler.simtick.writers.DateTimeWriter;
import com.chimpler.simtick.writers.LongWriter;

public class ReaderFactory {

    public Reader buildReader(byte[] header, int srcOffset) {
        int offset = srcOffset;
        int type = header[offset++];
        switch (type) {
            case LongWriter.TYPE_ID:
                return new LongReader(header, offset);

        }
        return null;
    }

    public Reader[] buildReaders(byte[] header, int srcOffset) {
        int offset = 0;
        int numReaders = (int) BitCodec.read(header, offset++, 8);
        Reader[] readers = new Reader[numReaders];
        for (int i = 0; i < numReaders; i++) {
            readers[i] = buildReader(header, offset);
        }
        return readers;
    }
}
