/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author XINEN
 */
public class DateTimeUtils {

    public static boolean isThisHour(Date date) {
        return isToday(date)
                && (new DateTime(date)).getHourOfDay() == DateTime.now().getHourOfDay();
    }

    public static int minutes(Date date) {
        return DateTime.now().getMinuteOfHour() - (new DateTime(date)).getMinuteOfHour();
    }

    public static boolean isToday(Date date) {
        return (new DateTime(date)).withTimeAtStartOfDay().equals(
                DateTime.now().withTimeAtStartOfDay());
    }

    public static int days(Date date) {
        return DateTime.now().getDayOfYear() - (new DateTime(date)).getDayOfYear();
    }

    public static int hours(Date date) {
        return DateTime.now().getHourOfDay() - (new DateTime(date)).getHourOfDay();
    }

    public static Date withTimeAtStartOfDay(Date date) {
        return new DateTime(date).withTimeAtStartOfDay().toDate();

    }

    public static Date withTimeAtEndOfDay(Date date) {
        return new DateTime(date).withTimeAtStartOfDay()
                .hourOfDay().addToCopy(24)
//                .millisOfDay().addToCopy(-1)
                // .secondOfDay().addToCopy(-1)
                .toDate();

    }

    public static Date withTimeAtEndOfDay(DateTime date) {
        return date.withTimeAtStartOfDay()
                .hourOfDay().addToCopy(24)
//                .millisOfDay().addToCopy(-1)
                // .secondOfDay().addToCopy(-1)
                .toDate();

    }

    public static Date[] today() {
        DateTime today = DateTime.now().withTimeAtStartOfDay();
        return new Date[]{
                today.toDate(),
                today.hourOfDay().addToCopy(24).toDate()};
    }

    public static Date[] yesterday() {
        DateTime yestoday = DateTime.now().withTimeAtStartOfDay().hourOfDay().addToCopy(-24);
        return new Date[]{
                yestoday.toDate(),
                yestoday.hourOfDay().addToCopy(24).toDate()};
    }

    public static Date[] thisWeek() {
        DateTime.Property todayOfWeek = DateTime.now().withTimeAtStartOfDay().dayOfWeek();
        return new Date[]{
                todayOfWeek.withMinimumValue().toDate(),
                todayOfWeek.withMaximumValue().hourOfDay().addToCopy(24).toDate()};
    }

    public static Date[] thisMonth() {
        DateTime.Property todayOfMonth = DateTime.now().withTimeAtStartOfDay().dayOfMonth();
        return new Date[]{
                todayOfMonth.withMinimumValue().toDate(),
                todayOfMonth.withMaximumValue().hourOfDay().addToCopy(24).toDate()};
    }

    public static Date[] thisYear() {
        DateTime.Property todayOfYear = DateTime.now().withTimeAtStartOfDay().dayOfYear();
        return new Date[]{
                todayOfYear.withMinimumValue().toDate(),
                todayOfYear.withMaximumValue().hourOfDay().addToCopy(24).toDate()};
    }

    @Deprecated
    public static Date before(int value) {
        return beforeDays(value);
    }

    public static Date beforeMinutes(int value) {
        return DateTime.now().withTimeAtStartOfDay().minuteOfHour().addToCopy(-value).toDate();

    }

    public static Date beforeHours(int value) {
        return DateTime.now().withTimeAtStartOfDay().hourOfDay().addToCopy(-value).toDate();

    }

    public static Date beforeDays(int value) {
        return DateTime.now().withTimeAtStartOfDay().dayOfYear().addToCopy(-value).toDate();

    }

    public static Date beforeWeeks(int value) {
        return DateTime.now().withTimeAtStartOfDay().weekOfWeekyear().addToCopy(-value).toDate();

    }

    public static Date beforeMonths(int value) {
        return DateTime.now().withTimeAtStartOfDay().monthOfYear().addToCopy(-value).toDate();

    }

    public static Date beforeYears(int value) {
        return DateTime.now().withTimeAtStartOfDay().yearOfEra().addToCopy(-value).toDate();

    }
}
