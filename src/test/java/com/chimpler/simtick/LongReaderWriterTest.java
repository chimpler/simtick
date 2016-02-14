package com.chimpler.simtick;

import com.chimpler.simtick.readers.LongReader;
import com.chimpler.simtick.writers.LongWriter;
import org.junit.Test;

public class LongReaderWriterTest {
    @Test
    public void testLongReaderWriter() {
        long value = 1455418551L;
        long delta = 2;
        byte[] buffer = new byte[100];
        LongReader reader = new LongReader(32, 2, true, true);
        LongWriter writer = new LongWriter(32, 2, true, true);
        writer.writeRaw(buffer, value, 1);
        writer.writeDelta(buffer, value + delta, 50);
        assert reader.readRaw(buffer, 1) == value;
        assert reader.readDelta(buffer, 50) == value + delta;
    }
}
