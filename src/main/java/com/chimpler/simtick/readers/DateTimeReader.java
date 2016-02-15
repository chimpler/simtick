package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.LongCodec;
import org.joda.time.DateTime;

public class DateTimeReader extends Reader<DateTime> {
    private final LongCodec longCodec;
    private Long oldValue = Long.MAX_VALUE;
    private int divFactor;

    public DateTimeReader(int deltaValues, boolean isMillis) {
        this.divFactor = isMillis ? 1 : 1000;
        this.deltaBits = (int) Math.ceil(Math.log(deltaValues));
        this.rawBits = isMillis ? 32 : 24;
        this.longCodec = new LongCodec(this.rawBits, this.deltaBits, true, true);
    }

    @Override
    public DateTime readRaw(byte[] buffer, int offset) {
        this.oldValue = longCodec.readRawValue(buffer, offset);
        return new DateTime(this.oldValue * this.divFactor);
    }

    @Override
    public DateTime readDelta(byte[] buffer, int offset) {
        long delta = longCodec.readDeltaValue(buffer, offset);
        this.oldValue += delta;
        return new DateTime(this.oldValue);
    }
}
