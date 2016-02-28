package com.chimpler.simtick;

import com.chimpler.simtick.codec.BitCodec;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BitCodecTest {
    @Test
    public void testBitCodec() {
        byte[] buffer = new byte[100];

        BitCodec.write(buffer, 1L, 1, 1);
        assertEquals(1L, BitCodec.read(buffer, 1, 1));

        BitCodec.write(buffer, 10278L, 15, 16);
        assertEquals(10278L, BitCodec.read(buffer, 15, 16));

    }
}
