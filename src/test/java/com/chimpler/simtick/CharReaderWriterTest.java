package com.chimpler.simtick;

import com.chimpler.simtick.readers.CharReader;
import com.chimpler.simtick.readers.LongReader;
import com.chimpler.simtick.writers.CharWriter;
import com.chimpler.simtick.writers.LongWriter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CharReaderWriterTest {
    @Test
    public void testLongReaderWriter() {
        String value1 = "AC";
        String value2 = "CD";
        byte[] buffer = new byte[100];
        CharReader reader = new CharReader(2);
        CharWriter writer = new CharWriter(2);
        writer.writeRaw(buffer, value1, 2);
        writer.writeDelta(buffer, value2, 18);

        assertEquals(value1, reader.readRaw(buffer, 2));
        assertEquals(value2, reader.readDelta(buffer, 18));
    }
}
