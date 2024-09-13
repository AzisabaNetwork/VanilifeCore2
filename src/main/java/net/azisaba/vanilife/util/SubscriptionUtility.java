package net.azisaba.vanilife.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SubscriptionUtility
{
    public static double getProgress()
    {
        LocalDate today = LocalDate.now();

        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        long daysPassed = ChronoUnit.DAYS.between(firstDayOfMonth, today);
        long totalDaysInMonth = ChronoUnit.DAYS.between(firstDayOfMonth, lastDayOfMonth) + 1;

        return (double) daysPassed / totalDaysInMonth;
    }
}
