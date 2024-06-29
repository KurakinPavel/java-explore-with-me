package ru.practicum.ewmserver.searchparams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchParameters {
    protected String text;
    protected List<Integer> categories;
    protected Boolean paid;
    protected LocalDateTime rangeStart;
    protected LocalDateTime rangeEnd;
    protected Boolean onlyAvailable;
}
