package com.chimpler.simtick;

import com.chimpler.simtick.readers.DecimalReader;
import com.chimpler.simtick.writers.DecimalWriter;
import org.junit.Test;

public class DecimalReaderWriterTest {
    @Test
    public void testLongReaderWriter() {
        double value = 1455418.551;
        double delta = 0.2;
        byte[] buffer = new byte[100];
        DecimalReader reader = new DecimalReader(32, 8, true, true, 3);
        DecimalWriter writer = new DecimalWriter(32, 8, true, true, 3);
        writer.writeRaw(buffer, value, 1);
        writer.writeDelta(buffer, value + delta, 50);
        assert reader.readRaw(buffer, 1) == value;
        assert reader.readDelta(buffer, 50) == value + delta;
    }
}