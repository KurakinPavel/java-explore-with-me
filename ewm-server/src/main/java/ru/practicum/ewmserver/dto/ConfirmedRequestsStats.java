package ru.practicum.ewmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfirmedRequestsStats {
    protected Integer eventId;
    protected Long confirmedRequests;
}
