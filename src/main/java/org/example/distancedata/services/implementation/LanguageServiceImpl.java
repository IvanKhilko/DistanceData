package org.example.distancedata.services.implementation;

import jakarta.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import org.example.distancedata.cache.LRUCache;
import org.example.distancedata.dto.LanguageDTO;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.entity.Language;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.repository.LanguageRepository;
import org.example.distancedata.services.DataService;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LanguageServiceImpl implements DataService<Language, LanguageDTO> {
    private final LanguageRepository repository;
    private final LRUCache<Long, Language> cache;
    private final JdbcTemplate jdbcTemplate;
    private static final String DONT_EXIST = " doesn't exist";

    @Override
    public Language create(final Language language) {
        repository.save(language);
        cache.put(language.getId(), language);
        return language;
    }

    @Override
    public List<Language> read() {
        return repository.findAll(Sort.by("id"));
    }

    @Override
    public Language getByName(final String name)
            throws ResourceNotFoundException {
        var optionalLanguage = repository.getByName(name);
        if (optionalLanguage.isPresent()) {
            cache.put(optionalLanguage.get().getId(), optionalLanguage.get());
        } else {
            throw new ResourceNotFoundException(
                    "Can't find language because with this name");
        }
        return optionalLanguage.get();
    }

    @Override
    public Language getByID(final Long id) throws ResourceNotFoundException {
        var optionalLanguage = cache.get(id);
        if (optionalLanguage.isEmpty()) {
            optionalLanguage = repository.getLanguageById(id);
            if (optionalLanguage.isPresent()) {
                cache.put(id, optionalLanguage.get());
            } else {
                throw new ResourceNotFoundException(
                        "Can't find language with this id = "
                                + id + " doesnt exist");
            }
        }
        return optionalLanguage.get();
    }

    @Override
    public void update(final Language language) throws ResourceNotFoundException {
        try {
            getByID(language.getId());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("No info with this id");
        }
        cache.remove(language.getId());
        repository.save(language);
        cache.put(language.getId(), language);
    }

    @Override
    public void delete(final Long id) throws ResourceNotFoundException {
        Language language = getByID(id);
        if (language != null) {
            List<Continent> existingContinents = language.getContinents();
            for (Continent continent : existingContinents) {
                continent.removeLanguage(language);
            }
            cache.remove(id);
            repository.deleteById(language.getId());
        } else {
            throw new ResourceNotFoundException(
                    "Can't delete language with this id = "
                            + id + DONT_EXIST);
        }
    }

    @Transactional
    @Override
    public void createBulk(final List<LanguageDTO> list)
            throws BadRequestException {
        List<Language> languages = list.stream()
                .map(languageDTO -> Language.builder()
                        .name(languageDTO.getName()).build()).toList();
        String sql = "INSERT into language (name, id) VALUES (?, ?)";

        var indexes = new HashSet<Long>();
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement statement,
                                  final int i)
                    throws SQLException {
                long index = findFreeId(indexes);
                indexes.add(index);
                statement.setString(1, languages.get(i).getName());
                statement.setLong(2, index);
            }

            @Override
            public int getBatchSize() {
                return languages.size();
            }
        });
    }

    @SuppressWarnings("checkstyle:OverloadMethodsDeclarationOrder")
    public void update(final LanguageDTO language)
            throws ResourceNotFoundException {
        update(Language.builder().name(language.getName())
                .continents(new ArrayList<>()).id(language.getId()).build());
    }

    @SuppressWarnings("checkstyle:OverloadMethodsDeclarationOrder")
    public void create(final LanguageDTO language) throws BadRequestException {
        try {
            getByName(language.getName());
            throw new BadRequestException("Language with this id is existed");
        } catch (ResourceNotFoundException e) {
            var newLanguage = Language.builder().name(language.getName())
                    .id(findFreeId()).continents(new ArrayList<>()).build();
            create(newLanguage);
        }
    }

    private long findFreeId() {
        var list = read();
        long i = 1;
        for (Language language : list) {
            if (language.getId() != i) {
                return i;
            }
            i++;
        }
        return i + 1;
    }

    public long findFreeId(final HashSet<Long> usedIndexes) {
        // Собираем все существующие идентификаторы из базы данных
        var existingIds = new HashSet<Long>();
        var languages = read(); // Предполагается, что read() возвращает все языки
        for (Language language : languages) {
            existingIds.add(language.getId());
        }

        // Добавляем уже использованные идентификаторы
        existingIds.addAll(usedIndexes);

        // Находим следующий свободный идентификатор, начиная с 1
        long id = 1;
        while (existingIds.contains(id)) {
            id++;
        }

        return id; // Возвращаем первый свободный идентификатор
    }
}