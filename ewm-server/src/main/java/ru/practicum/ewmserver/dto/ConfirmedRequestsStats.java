package ru.practicum.ewmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedRequestsStats {
    protected Integer eventId;
    protected Long confirmedRequests;
}
