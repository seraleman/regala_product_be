package com.seraleman.regala_product_be.helpers.localDataTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

@Service
public class LocalDateTimeImpl implements ILocalDateTime {

    @Override
    public LocalDateTime getLocalDateTime() {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        return zdt.toLocalDateTime();
    }
}
