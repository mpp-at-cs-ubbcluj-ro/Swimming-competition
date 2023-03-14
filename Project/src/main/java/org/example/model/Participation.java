package org.example.model;

import java.util.Objects;

public class Participation extends Entity<Long> {
    private Long SwimmerId;
    private Long EventId;

    public Participation(Long swimmerId, Long eventId) {
        SwimmerId = swimmerId;
        EventId = eventId;
    }

    public Long getSwimmerId() {
        return SwimmerId;
    }

    public void setSwimmerId(Long swimmerId) {
        SwimmerId = swimmerId;
    }

    public Long getEventId() {
        return EventId;
    }

    public void setEventId(Long eventId) {
        EventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participation that = (Participation) o;
        return Objects.equals(SwimmerId, that.SwimmerId) && Objects.equals(EventId, that.EventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SwimmerId, EventId);
    }

    @Override
    public String toString() {
        return "Participation{" +
                "SwimmerId=" + SwimmerId +
                ", TaskId=" + EventId +
                '}';
    }
}
