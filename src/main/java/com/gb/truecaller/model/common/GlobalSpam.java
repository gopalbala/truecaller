package com.gb.truecaller.model.common;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import orestes.bloomfilter.CountingBloomFilter;
import orestes.bloomfilter.FilterBuilder;

import java.nio.charset.Charset;

import static com.gb.truecaller.model.common.Constant.MAX_COUNT_TO_MARK_SPAM;
import static com.gb.truecaller.model.common.Constant.MAX_GLOBAL_SPAM_COUNT;

public class GlobalSpam {
    private BloomFilter<String> globalBlocked =
            BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),
                    MAX_GLOBAL_SPAM_COUNT);

    private CountingBloomFilter<String> globalSpam =
            new FilterBuilder(MAX_GLOBAL_SPAM_COUNT, .01)
                    .buildCountingBloomFilter(
                    );

    private GlobalSpam() {
    }

    public static GlobalSpam INSTANCE = new GlobalSpam();

    public void reportSpam(String number) {
        if (globalSpam.getEstimatedCount(number) > MAX_COUNT_TO_MARK_SPAM) {
            globalBlocked.put(number);
        } else {
            globalSpam.add(number);
        }
    }

    public boolean isSpam(String number) {
        return globalBlocked.mightContain(number);
    }
}
