package com.chimpler.simtick;

import com.chimpler.simtick.readers.DateTimeReader;
import com.chimpler.simtick.readers.LongReader;
import com.chimpler.simtick.readers.Reader;
import com.chimpler.simtick.readers.SimTickReader;
import com.chimpler.simtick.writers.DateTimeWriter;
import com.chimpler.simtick.writers.LongWriter;
import com.chimpler.simtick.writers.SimTickWriter;
import com.chimpler.simtick.writers.Writer;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Arrays;

public class SimTickReaderWriterTest {
    @Test
    public void testSimTickReaderWriter() {
        SimTickWriter writer = new SimTickWriter(new Writer[] {
                new LongWriter(16, 3, true, true),
                new LongWriter(12, 3, true, true),
                new DateTimeWriter(10, false)
        });

        SimTickReader reader = new SimTickReader(new Reader[] {
                new LongReader(16, 3, true, true),
                new LongReader(12, 3, true, true),
                new DateTimeReader(10, false)
        });

        DateTime now = new DateTime().withMillisOfSecond(0);
        DateTime later1 = now.plusSeconds(2);
        DateTime later2 = now.plusSeconds(3);

        Object[] row1 = new Object[] {
            15321L,
            1221L,
            now
        };

        Object[] row2 = new Object[] {
                15323L,
                1223L,
                later1
        };

        Object[] row3 = new Object[] {
                16321L,
                1220L,
                later2
        };

        byte[] buffer = new byte[100];
        int offset = 1;
        offset += writer.write(buffer, row1, offset);
        offset += writer.write(buffer, row2, offset);
        writer.write(buffer, row3, offset);

        Object[] resultRow1 = new Object[3];
        Object[] resultRow2 = new Object[3];
        Object[] resultRow3 = new Object[3];

        offset = 1;
        offset += reader.read(buffer, offset, resultRow1);
        offset += reader.read(buffer, offset, resultRow2);
        reader.read(buffer, offset, resultRow3);

        assert Arrays.equals(row1, resultRow1);
        assert Arrays.equals(row2, resultRow2);
        assert Arrays.equals(row3, resultRow3);
    }
}
