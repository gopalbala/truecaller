package com.gb.truecaller.model.common;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import orestes.bloomfilter.CountingBloomFilter;
import orestes.bloomfilter.FilterBuilder;

import java.nio.charset.Charset;

public class GlobalSpam {
    private BloomFilter<String> globalBlocked =
            BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),
                    10_000_000);

    private CountingBloomFilter<String> globalSpam =
            new FilterBuilder(10_000_000, .01)
                    .buildCountingBloomFilter(
                    );

    private GlobalSpam() {
    }

    public static GlobalSpam INSTANCE = new GlobalSpam();

    public void reportSpam(String number) {
        if (globalSpam.getEstimatedCount(number) > 10000) {
            globalBlocked.put(number);
        } else {
            globalSpam.add(number);
        }

    }
}
