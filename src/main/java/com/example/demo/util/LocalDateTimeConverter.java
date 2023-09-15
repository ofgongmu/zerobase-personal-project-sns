package com.example.demo.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

public class LocalDateTimeConverter implements PropertyValueConverter {

  @Override
  public Object write(Object value) {
    ZoneId zoneId = ZoneId.systemDefault();
    return ((LocalDateTime)value).atZone(zoneId).toEpochSecond();
  }

  @Override
  public Object read(Object value) {
    Instant instant = Instant.ofEpochSecond((Integer) value);
    ZoneId zoneId = ZoneId.systemDefault();
    return instant.atZone(zoneId).toLocalDateTime();
  }

  public static LocalDateTime toLocalDateTime(Integer epochSecond) {
    Instant instant = Instant.ofEpochSecond(epochSecond);
    ZoneId zoneId = ZoneId.systemDefault();
    return instant.atZone(zoneId).toLocalDateTime();
  }
}
