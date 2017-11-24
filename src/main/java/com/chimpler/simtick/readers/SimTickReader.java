package com.chimpler.simtick.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class SimTickReader {
    private Reader[] readers;
    private InputStream inputStream;
    // TODO: determine it from header
    private byte[] buffer = new byte[10000];
    private int bufferOffset;

    public SimTickReader(Reader[] readers) {
        this.readers = readers;
    }

    public SimTickReader(InputStream inputStream, ReaderFactory readerFactory) throws IOException {
        this.inputStream = inputStream;
        fillBuffer();
        readerFactory.buildReader(buffer, 0);
    }

    private void fillBuffer() throws IOException {
        int remainingLen = buffer.length - bufferOffset;
        // TODO optimize it to keep pointer and then read in a cyclic way instead of copying
        System.arraycopy(buffer, bufferOffset, buffer, 0, remainingLen);
        this.inputStream.read(buffer, remainingLen, buffer.length);
        bufferOffset = 0;
    }

    public int readValues(byte[] buffer, int srcOffset, Object[] result) {
        int offset = srcOffset;
        boolean isDelta = false;

        for (int i = 0; i < readers.length; i++) {
            Reader reader = readers[i];
            ValueAndLength valueAndLength = isDelta ? reader.readDelta(buffer, offset) : reader.readRaw(buffer, offset);
            result[i] = valueAndLength.value;
            offset += valueAndLength.length;
        }
        return offset - srcOffset;
    }

    public static ValueAndLength<SimTickReader> fromByteArray(byte[] buffer) {
        ValueAndLength<Reader[]> valueAndLength = new ReaderFactory().buildReaders(buffer);
        return new ValueAndLength<SimTickReader>(
                new SimTickReader(valueAndLength.value),
                valueAndLength.length
        );
    }

    public Reader[] getReaders() {
        return readers;
    }

    @Override
    public String toString() {
        return "SimTickReader{" +
                "readers=" + Arrays.toString(readers) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimTickReader that = (SimTickReader) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(readers, that.readers)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(readers);
        return result;
    }
}
