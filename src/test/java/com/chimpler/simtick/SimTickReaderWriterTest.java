package com.chimpler.simtick;

import com.chimpler.simtick.readers.LongReader;
import com.chimpler.simtick.readers.Reader;
import com.chimpler.simtick.writers.LongWriter;
import com.chimpler.simtick.writers.Writer;
import org.junit.Test;

public class SimTickReaderWriterTest {
    @Test
    public void testSimTickReaderWriter() {
        SimTickWriter writer = new SimTickWriter(new Writer[] {
                new LongWriter(10, 2, true, true),
                new LongWriter(15, 3, true, true)
        });

        SimTickReader reader = new SimTickReader(new Reader[] {
                new LongReader(10, 2, true, true),
                new LongReader(15, 3, true, true)
        });

        Object[] row1 = new Object[] {
            15321L,
            1221L
        };

        Object[] row2 = new Object[] {
                15323L,
                1219L
        };

        Object[] row3 = new Object[] {
                16321L,
                1221L
        };

        byte[] buffer = new byte[100];
        int offset = 1;
        offset += writer.write(buffer, row1, offset);
        offset += writer.write(buffer, row2, offset);
        writer.write(buffer, row3, offset);

        Object[] resultRow1 = new Object[2];
        Object[] resultRow2 = new Object[2];
        Object[] resultRow3 = new Object[2];

        offset = 1;
        offset += reader.read(buffer, offset, resultRow1);
        offset += reader.read(buffer, offset, resultRow2);
        reader.read(buffer, offset, resultRow3);

        assert resultRow1 == row1;
        assert resultRow2 == row2;
        assert resultRow3 == row3;
    }
}
