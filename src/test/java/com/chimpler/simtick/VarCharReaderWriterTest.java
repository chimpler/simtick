package com.chimpler.simtick;

import com.chimpler.simtick.readers.ValueAndLength;
import com.chimpler.simtick.readers.VarCharReader;
import com.chimpler.simtick.writers.VarCharWriter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VarCharReaderWriterTest {
    @Test
    public void testLongReaderWriter() {
        String value1 = "ABCDEFGHI";
        String value2 = "JKL";
        byte[] buffer = new byte[1000];
        VarCharReader reader = new VarCharReader(16);
        VarCharWriter writer = new VarCharWriter(16);
        writer.writeRaw(buffer, value1, 2);
        writer.writeDelta(buffer, value2, 500);

        assertEquals(new ValueAndLength<String>(value1, 76), reader.readRaw(buffer, 2));
        assertEquals(new ValueAndLength<String>(value2, 28), reader.readDelta(buffer, 500));
    }
}
