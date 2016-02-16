package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.LongCodec;
import org.joda.time.DateTime;

// TODO: Support millis and seconds
public class DateTimeWriter extends Writer<DateTime> {
    private final LongCodec longCodec;
    private Long oldValue = Long.MAX_VALUE;
    private int divFactor;

    public DateTimeWriter(int deltaValues, boolean isMillis) {
        this.divFactor = isMillis ? 1 : 1000;
        int deltaBits = (int) Math.ceil(Math.log(deltaValues) / Math.log(2));
        int rawBits = 32;
        this.longCodec = new LongCodec(rawBits, deltaBits, true, true);
    }

    @Override
    public boolean isDelta(DateTime value) {
        return longCodec.isInDeltaRange(oldValue, value.getMillis() / divFactor);
    }

    @Override
    public int writeRaw(byte[] buffer, DateTime value, int offset) {
        this.oldValue =  value.getMillis() / divFactor;
        return longCodec.writeRawValue(buffer, oldValue, offset);
    }

    @Override
    public int writeDelta(byte[] buffer, DateTime value, int offset) {
        long delta = value.getMillis() / divFactor - this.oldValue;
        this.oldValue += delta;
        return longCodec.writeDeltaValue(buffer, delta, offset);
    }
}
