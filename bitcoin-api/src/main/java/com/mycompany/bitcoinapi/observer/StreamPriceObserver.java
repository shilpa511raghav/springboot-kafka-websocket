package com.mycompany.bitcoinapi.observer;

import com.google.gson.Gson;
import com.mycompany.bitcoinapi.dto.PriceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(Source.class)
public class StreamPriceObserver implements PriceObserver {

    private final Source source;
    private final Gson gson;

    public StreamPriceObserver(Source source, Gson gson) {
        this.source = source;
        this.gson = gson;
    }

    @Override
    public void update(PriceDto priceDto) {
        Message<String> message = MessageBuilder.withPayload(gson.toJson(priceDto))
                .setHeader("partitionKey", priceDto.getTimestamp().getTime())
                .build();
        source.output().send(message);

        log.info("{} sent to bus.", message);
    }
}