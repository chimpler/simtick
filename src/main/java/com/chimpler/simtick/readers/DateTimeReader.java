package com.chimpler.simtick.readers;

import com.chimpler.simtick.codec.LongCodec;
import org.joda.time.DateTime;

public class DateTimeReader extends Reader<DateTime> {
    private final LongCodec longCodec;
    private Long oldValue = Long.MAX_VALUE;
    private int divFactor;

    public DateTimeReader(int deltaValues, boolean isMillis) {
        super(32, (int) Math.ceil(Math.log(deltaValues) / Math.log(2)));
        this.divFactor = isMillis ? 1 : 1000;
        this.longCodec = new LongCodec(this.rawBits, this.deltaBits, true, true);
    }

    @Override
    public ValueAndLength<DateTime> readRaw(byte[] buffer, int offset) {
        this.oldValue = longCodec.readRawValue(buffer, offset);
        return valueAndLength.withValueAndLength(
                new DateTime(this.oldValue * this.divFactor),
                rawBits
        );
    }

    @Override
    public ValueAndLength<DateTime> readDelta(byte[] buffer, int offset) {
        long delta = longCodec.readDeltaValue(buffer, offset);
        this.oldValue += delta;
        return valueAndLength.withValueAndLength(
                new DateTime(this.oldValue * this.divFactor),
                deltaBits
        );
    }
}
