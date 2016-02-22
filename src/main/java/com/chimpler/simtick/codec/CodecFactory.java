package com.chimpler.simtick.codec;

public class CodecFactory {

    public LongCodec buildFixedLongCodec(long minRaw, long maxRaw) {
        // determine number of bit to encode raw
        long rawRange = maxRaw - minRaw;
        int rawBits = (int) Math.ceil(Math.log(rawRange) / Math.log(2));
        long middleRawOffset = minRaw + rawRange / 2;
        long actualRawRange = (long) Math.pow(2, rawBits);
        long actualMinRaw = middleRawOffset - actualRawRange / 2;
        return new LongCodec(rawBits, actualMinRaw);
    }

    /**
     * @param minRaw
     * @param maxRaw
     * @param minDelta
     * @param maxDelta
     * @param keepMin  recompute min based on additional range provided by bits
     * @return
     */
    public LongCodec buildDeltaLongCodec(long minRaw, long maxRaw, long minDelta, long maxDelta, boolean keepMin) {
        // determine number of bit to encode raw and deltq
        long rawRange = maxRaw - minRaw;
        long deltaRange = maxDelta - minDelta;

        int rawBits = (int) Math.ceil(Math.log(rawRange) / Math.log(2));
        int deltaBits = (int) Math.ceil(Math.log(deltaRange) / Math.log(2));

        long middleRawOffset = minRaw + rawRange / 2;
        long middleDeltaOffset = minDelta + deltaRange / 2;

        long actualMinRaw = minRaw;
        long actualMinDelta = minDelta;
        if (!keepMin) {
            long actualRawRange = (long) Math.pow(2, rawBits);
            long actualDeltaRange = (long) Math.pow(2, deltaBits);

            actualMinRaw = middleRawOffset - actualRawRange / 2;
            actualMinDelta = middleDeltaOffset - actualDeltaRange / 2;
        }

        return new LongCodec(rawBits, deltaBits, actualMinRaw, actualMinDelta);
    }
}
