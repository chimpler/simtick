package com.chimpler.simtick.readers;

import com.chimpler.simtick.SimtickException;
import com.chimpler.simtick.Version;
import com.chimpler.simtick.codec.BitCodec;
import com.chimpler.simtick.writers.DateTimeWriter;
import com.chimpler.simtick.writers.LongWriter;
import com.chimpler.simtick.writers.Writer;

public class ReaderFactory {

    public ValueAndLength<Reader> buildReader(byte[] header, int srcOffset) {
        int offset = srcOffset;
        int type = (int)BitCodec.read(header, offset, 8);
        offset += 8;
        System.out.println(type);
        ValueAndLength<Reader> valueAndLength;
        switch (type) {
            case LongWriter.TYPE_ID:
                valueAndLength = LongReader.fromByteArray(header, offset);
                break;

            case DateTimeWriter.TYPE_ID:
                valueAndLength = DateTimeReader.fromByteArray(header, offset);
                break;

            default:
                throw new SimtickException("Unknown header (value=" + type + ")");
        }
        return new ValueAndLength<>(valueAndLength.value, valueAndLength.length + 8);
    }

    public ValueAndLength<Reader[]> buildReaders(byte[] header, int srcOffset) {
        int offset = srcOffset;
        long version = BitCodec.read(header, offset, 16);
        System.out.println("Version: " + version);
        offset += 16;
        int numReaders = (int) BitCodec.read(header, offset, 8);
        offset += 8;
        System.out.println("Readers: " + numReaders);
        Reader[] readers = new Reader[numReaders];
        for (int i = 0; i < numReaders; i++) {
            ValueAndLength<Reader> valueAndLength = buildReader(header, offset);
            readers[i] = valueAndLength.value;
            offset += valueAndLength.length;
            System.out.println("readOffset=======> " + offset);
        }
        return new ValueAndLength<Reader[]>(
          readers,
          offset - srcOffset
        );
    }

    public ValueAndLength<Reader[]> buildReaders(byte[] header) {
        return buildReaders(header, 0);
    }

}
