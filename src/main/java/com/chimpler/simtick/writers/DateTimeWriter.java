package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;
import org.joda.time.DateTime;

// TODO: Support millis and seconds
public class DateTimeWriter extends Writer<DateTime> {
    private final LongCodec codec;
    private Long oldValue = Long.MAX_VALUE;
    private int divFactor;

    public DateTimeWriter(DateTime minDate, DateTime maxDate, boolean isMillis) {
        this(minDate, maxDate, true, 0, 0, isMillis);
    }

    public DateTimeWriter(DateTime minDate, DateTime maxDate, int minDeltaValues, int maxDeltaValues, boolean isMillis) {
        this(minDate, maxDate, false, minDeltaValues, maxDeltaValues, isMillis);
    }

    private DateTimeWriter(DateTime minDate, DateTime maxDate, boolean fixed, int minDeltaValues, int maxDeltaValues, boolean isMillis) {
        super(fixed);
        this.divFactor = isMillis ? 1 : 1000;
        this.codec = new CodecFactory().buildDeltaLongCodec(
                minDate.getMillis() / divFactor,
                maxDate.getMillis() / divFactor,
                minDeltaValues,
                maxDeltaValues,
                true
        );
    }

    @Override
    public boolean isDelta(DateTime value) {
        return codec.isInDeltaRange(oldValue, value.getMillis() / divFactor);
    }

    @Override
    public int writeRaw(byte[] buffer, DateTime value, int offset) {
        this.oldValue = value.getMillis() / divFactor;
        return codec.writeRawValue(buffer, oldValue, offset);
    }

    @Override
    public int writeDelta(byte[] buffer, DateTime value, int offset) {
        long delta = value.getMillis() / divFactor - this.oldValue;
        this.oldValue += delta;
        return codec.writeDeltaValue(buffer, delta, offset);
    }
}
