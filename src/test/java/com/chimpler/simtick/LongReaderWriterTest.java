package com.chimpler.simtick;

import com.chimpler.simtick.readers.LongReader;
import com.chimpler.simtick.writers.LongWriter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongReaderWriterTest {
    @Test
    public void testLongReaderWriter() {
        long value = 1455595658L;
        long delta = 2;
        byte[] buffer = new byte[100];
        LongReader reader = new LongReader(32, 2, true, true);
        LongWriter writer = new LongWriter(32, 2, true, true);
        writer.writeRaw(buffer, value, 1);
        writer.writeDelta(buffer, value + delta, 33);

        assertEquals(reader.readRaw(buffer, 1).longValue(), value);
        assertEquals(reader.readDelta(buffer, 33).longValue(), value + delta);
    }
}
