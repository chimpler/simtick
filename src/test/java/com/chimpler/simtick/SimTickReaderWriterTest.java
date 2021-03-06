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

import static org.junit.Assert.assertArrayEquals;

public class SimTickReaderWriterTest {
    @Test
    public void testSimTickReaderWriter() {
        DateTime now = new DateTime().withMillisOfSecond(0);
        DateTime later1 = now.plusSeconds(2);
        DateTime later2 = now.plusSeconds(3);
        DateTime minDate = new DateTime(2000, 1, 1, 0, 0);
        DateTime maxDate = new DateTime(2020, 1, 1, 0, 0);

        SimTickWriter writer = new SimTickWriter(
                new Writer[]{
                        new LongWriter(0, 1000000, 0, 100),
                        new LongWriter(0, 1000000, 0, 100),
                        new DateTimeWriter(minDate, maxDate, -20, 20, false)
                },
                null,
                true,
                true);

        SimTickReader reader = new SimTickReader(
                new Reader[]{
                        new LongReader(0, 1000000, 0, 100),
                        new LongReader(0, 1000000, 0, 100),
                        new DateTimeReader(minDate, maxDate, -20, 20, false)
                },
                true,
                true);


        Object[] row1 = new Object[]{
                15321L,
                1221L,
                now
        };

        Object[] row2 = new Object[]{
                15323L,
                1223L,
                later1
        };

        Object[] row3 = new Object[]{
                16321L,
                1220L,
                later2
        };

        byte[] buffer = new byte[100];
        int offset = 1;
        offset += writer.writeValues(buffer, row1, offset);
        offset += writer.writeValues(buffer, row2, offset);
        writer.writeValues(buffer, row3, offset);

        Object[] resultRow1 = new Object[3];
        Object[] resultRow2 = new Object[3];
        Object[] resultRow3 = new Object[3];

        offset = 1;
        offset += reader.read(buffer, offset, resultRow1);
        offset += reader.read(buffer, offset, resultRow2);
        reader.read(buffer, offset, resultRow3);

        assertArrayEquals(row1, resultRow1);
        assertArrayEquals(row2, resultRow2);
        assertArrayEquals(row3, resultRow3);
    }
}
