package com.epam.brest.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Filter {

    private LocalDateTime startDate;

    private LocalDateTime finishDate;

    public Filter() {
    }

    public Filter(LocalDateTime startDate, LocalDateTime finishDate) {
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
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
