package ru.practicum.ewmserver.searchparams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmserver.enums.SortType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PresentationParameters {
    protected SortType sort;
    protected Integer from;
    protected Integer size;
}
