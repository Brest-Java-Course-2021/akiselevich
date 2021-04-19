package com.epam.brest.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

public class Filter {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate finishDate;

    public Filter() {
    }

    public Filter(LocalDate startDate, LocalDate finishDate) {
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return Objects.equals(startDate, filter.startDate) && Objects.equals(finishDate, filter.finishDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, finishDate);
    }

    @Override
    public String toString() {
        return "filter{" +
                "startDate=" + startDate +
                ", finishDate=" + finishDate +
                '}';
    }
}
