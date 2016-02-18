package com.chimpler.simtick;

import com.chimpler.simtick.codec.BitCodec;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BitCodecTest {
    @Test
    public void testBitCodec() {
        BitCodec bitCodec = new BitCodec();
        byte[] buffer = new byte[100];

        bitCodec.write(buffer, 1L, 1, 1);
        assertEquals(bitCodec.read(buffer, 1, 1), 1L);

        bitCodec.write(buffer, 10278L, 15, 16);
        assertEquals(bitCodec.read(buffer, 15, 16), 10278L);

    }
}
