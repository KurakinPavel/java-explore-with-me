package ru.practicum.ewmserver.services.entityservices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmserver.dto.compilation.CompilationDto;
import ru.practicum.ewmserver.mappers.CompilationMapper;
import ru.practicum.ewmserver.model.Compilation;
import ru.practicum.ewmserver.repositories.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;

    @Transactional
    public Compilation saveCompilation(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    @Transactional(readOnly = true)
    public Compilation getCompilation(int compId) {
        return compilationRepository.getReferenceById(compId);
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Compilation> compilations;
        compilations = compilationRepository.findByPinnedForPublic(pinned, pageable);
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCompilation(int compId) {
        Compilation compilation = getCompilation(compId);
        compilation.getId();
        compilationRepository.delete(compilation);
    }
}
