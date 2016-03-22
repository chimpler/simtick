package com.chimpler.simtick.writers;

import com.chimpler.simtick.codec.BitCodec;
import com.chimpler.simtick.codec.CodecFactory;
import com.chimpler.simtick.codec.LongCodec;
import org.joda.time.DateTime;

// TODO: Support millis and seconds
public class DateTimeWriter extends Writer<DateTime> {
    public static final byte TYPE_ID = 5;

    private final LongCodec codec;
    private Long oldValue = Long.MAX_VALUE;
    private final int divFactor;

    private final DateTime minDate;
    private DateTime maxDate;
    private int minDeltaValues;
    private int maxDeltaValues;
    private boolean isMillis;

    public DateTimeWriter(DateTime minDate, DateTime maxDate, boolean isMillis) {
        this(minDate, maxDate, true, 0, 0, isMillis);
    }

    public DateTimeWriter(DateTime minDate, DateTime maxDate, int minDeltaValues, int maxDeltaValues, boolean isMillis) {
        this(minDate, maxDate, false, minDeltaValues, maxDeltaValues, isMillis);
    }

    private DateTimeWriter(DateTime minDate, DateTime maxDate, boolean fixed, int minDeltaValues, int maxDeltaValues, boolean isMillis) {
        super(fixed);
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.minDeltaValues = minDeltaValues;
        this.maxDeltaValues = maxDeltaValues;
        this.isMillis = isMillis;

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

    @Override
    public int writerHeader(byte[] buffer, int srcOffset) {
        int offset = srcOffset;
        offset += BitCodec.write(buffer, isMillis ? 1:0, offset, 1);
        offset += BitCodec.write(buffer, minDate.getMillis() / divFactor, offset, 64);
        offset += BitCodec.write(buffer, maxDate.getMillis() / divFactor, offset, 64);
        offset += BitCodec.write(buffer, fixed ? 1:0, offset, 1);
        offset += BitCodec.write(buffer, minDeltaValues, offset, 32);
        offset += BitCodec.write(buffer, maxDeltaValues, offset, 32);
        return offset - srcOffset;
    }

    @Override
    public int getMaxSize() {
        return this.codec.rawBits;
    }
}
