package com.polykhel.ssq.utils;

import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.*;
import java.util.Date;

/**
 * <p>JSR310DateConverters class.</p>
 */
@NoArgsConstructor
public final class JSR310DateConverters {

    @NoArgsConstructor
    public static class LocalDateToDateConverter implements Converter<LocalDate, Date> {

        public static final LocalDateToDateConverter INSTANCE = new LocalDateToDateConverter();

        @Override
        public Date convert(@Nullable LocalDate source) {
            return source == null ? null : Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    @NoArgsConstructor
    public static class DateToLocalDateConverter implements Converter<Date, LocalDate> {

        public static final DateToLocalDateConverter INSTANCE = new DateToLocalDateConverter();

        @Override
        public LocalDate convert(@Nullable Date source) {
            return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault())
                    .toLocalDate();
        }
    }

    @NoArgsConstructor
    public static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

        public static final ZonedDateTimeToDateConverter INSTANCE = new ZonedDateTimeToDateConverter();

        @Override
        public Date convert(@Nullable ZonedDateTime source) {
            return source == null ? null : Date.from(source.toInstant());
        }
    }

    @NoArgsConstructor
    public static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

        public static final DateToZonedDateTimeConverter INSTANCE = new DateToZonedDateTimeConverter();

        @Override
        public ZonedDateTime convert(@Nullable Date source) {
            return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    @NoArgsConstructor
    public static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

        public static final LocalDateTimeToDateConverter INSTANCE = new LocalDateTimeToDateConverter();

        @Override
        public Date convert(@Nullable LocalDateTime source) {
            return source == null ? null : Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    @NoArgsConstructor
    public static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

        public static final DateToLocalDateTimeConverter INSTANCE = new DateToLocalDateTimeConverter();

        @Override
        public LocalDateTime convert(@Nullable Date source) {
            return source == null ? null : LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    @NoArgsConstructor
    public static class DurationToLongConverter implements Converter<Duration, Long> {

        public static final DurationToLongConverter INSTANCE = new DurationToLongConverter();

        @Override
        public Long convert(@Nullable Duration source) {
            return source == null ? null : source.toNanos();
        }
    }

    @NoArgsConstructor
    public static class LongToDurationConverter implements Converter<Long, Duration> {

        public static final LongToDurationConverter INSTANCE = new LongToDurationConverter();

        @Override
        public Duration convert(@Nullable Long source) {
            return source == null ? null : Duration.ofNanos(source);
        }
    }
}
