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
        int deltaBits = (int) Math.ceil(Math.log(deltaValues));
        int rawBits = isMillis ? 32 : 24;
        this.longCodec = new LongCodec(rawBits, deltaBits, true, true);
    }

    @Override
    public boolean isDelta(DateTime value) {
        return longCodec.isInDeltaRange(oldValue, value.getMillis() / divFactor);
    }

    @Override
    public int writeRaw(byte[] buffer, DateTime value, int offset) {
        long newTimestamp = value.getMillis() / divFactor;
        return longCodec.writeRawValue(buffer, newTimestamp, offset);
    }

    @Override
    public int writeDelta(byte[] buffer, DateTime value, int offset) {
        long oldTimestamp = oldValue / divFactor;
        long newTimestamp = value.getMillis() / divFactor;
        return longCodec.writeDeltaValue(buffer, newTimestamp - oldTimestamp, offset);
    }
}
