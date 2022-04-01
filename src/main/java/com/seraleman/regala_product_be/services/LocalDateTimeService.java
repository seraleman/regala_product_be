package com.seraleman.regala_product_be.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

@Service
public class LocalDateTimeService implements ILocalDateTimeService {

    @Override
    public LocalDateTime getLocalDateTime() {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        return zdt.toLocalDateTime();
    }
}
