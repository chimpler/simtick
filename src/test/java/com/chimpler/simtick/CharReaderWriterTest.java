package com.chimpler.simtick;

import com.chimpler.simtick.readers.CharReader;
import com.chimpler.simtick.readers.ValueAndLength;
import com.chimpler.simtick.writers.CharWriter;
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

        assertEquals(new ValueAndLength<String>(value1, 16), reader.readRaw(buffer, 2));
        assertEquals(new ValueAndLength<String>(value2, 16), reader.readDelta(buffer, 18));
    }
}
