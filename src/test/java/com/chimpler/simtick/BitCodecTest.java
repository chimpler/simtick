package com.chimpler.simtick;

import com.chimpler.simtick.codec.BitCodec;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BitCodecTest {
    @Test
    public void testBitCodec() {
        byte[] buffer = new byte[200];

        BitCodec.write(buffer, 1L, 1, 1);
        assertEquals(1L, BitCodec.read(buffer, 1, 1));

        BitCodec.write(buffer, 10278L, 15, 16);
        assertEquals(10278L, BitCodec.read(buffer, 15, 16));

        BitCodec.write(buffer, 1000, 32, 64);
        assertEquals(1000, BitCodec.read(buffer, 32, 64));

        BitCodec.write(buffer, 10000000000L, 96, 64);
        assertEquals(10000000000L, BitCodec.read(buffer, 96, 64));

    }
}
