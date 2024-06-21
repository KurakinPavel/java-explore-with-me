package ru.practicum.ewmserver.dto;

import java.util.List;

public class EventRequestStatusUpdateRequest {
    protected List<Integer> requestIds;
    protected String status;
}
