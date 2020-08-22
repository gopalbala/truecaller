package com.gb.truecaller.model.common;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.Getter;
import lombok.Setter;
import orestes.bloomfilter.CountingBloomFilter;
import orestes.bloomfilter.FilterBuilder;

import java.nio.charset.Charset;

@Getter
@Setter
public class GlobalSpam {
    private BloomFilter<String> blockedContacts =
            BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),
                    10_000_000);

    private CountingBloomFilter<String> globalSpam =
            new FilterBuilder(10_000_000, .01)
                    .buildCountingBloomFilter(
                    );
}
