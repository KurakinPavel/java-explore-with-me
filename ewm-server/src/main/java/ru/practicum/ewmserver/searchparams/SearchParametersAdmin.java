package ru.practicum.ewmserver.searchparams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmserver.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchParametersAdmin {
    protected List<Integer> users;
    protected List<EventState> states;
    protected List<Integer> categories;
    protected LocalDateTime rangeStart;
    protected LocalDateTime rangeEnd;
}
