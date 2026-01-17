package com.github.klee0kai.stone.test_feature.finance.model;

import java.util.UUID;

public class WorkCalendar {

    public final String uuid = UUID.randomUUID().toString();

    public final String name;
    public final int workDaysInYear;
    public final int workHoursInDay;

    public WorkCalendar(String name, int workDaysInYear, int workHoursInDay) {
        this.name = name;
        this.workDaysInYear = workDaysInYear;
        this.workHoursInDay = workHoursInDay;
    }

}
