package com.chimpler.simtick;

import com.chimpler.simtick.readers.LongReader;
import com.chimpler.simtick.readers.ValueAndLength;
import com.chimpler.simtick.writers.LongWriter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongReaderWriterTest {
    @Test
    public void testLongReaderWriter() {
        long value = 1455595658L;
        long delta = 2;
        int rawBits = 31;
        int deltaBits = 6;

        byte[] buffer = new byte[100];
        LongReader reader = new LongReader(1000, 2000000000L, -20, 20);
        LongWriter writer = new LongWriter(1000, 2000000000L, -20, 20);
        writer.writeRaw(buffer, value, 1);
        writer.writeDelta(buffer, value + delta, 1 + rawBits);

        assertEquals(new ValueAndLength<Long>(value, rawBits), reader.readRaw(buffer, 1));
        assertEquals(new ValueAndLength<Long>(value + delta, deltaBits), reader.readDelta(buffer, 1 + rawBits));
    }
}
