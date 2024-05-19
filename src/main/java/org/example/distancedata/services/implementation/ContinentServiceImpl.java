package org.example.distancedata.services.implementation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.distancedata.cache.LRUCache;
import org.example.distancedata.dto.ContinentDTO;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.entity.Language;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.repository.ContinentRepository;
import org.example.distancedata.repository.LanguageRepository;
import org.example.distancedata.services.DataService;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContinentServiceImpl implements DataService<Continent, ContinentDTO> {
    private final ContinentRepository continentRepository;
    private final LanguageRepository languageRepository;
    private final LRUCache<Long, Continent> cache;
    private final JdbcTemplate jdbcTemplate;
    private static final String DONT_EXIST = " doesn't exist";

    private long findFreeID() {
        var list = read();
        long i = 1;
        for (Continent continent : list) {
            if (continent.getId() != i) {
                return i;
            }
            i++;
        }
        return i + 1;
    }

    @Override
    public Continent create(final Continent continent) {
        cache.put(continent.getId(), continent);
        continentRepository.save(continent);
        return continent;
    }

    @Override
    public List<Continent> read() {
        return continentRepository.findAll(Sort.by("id"));
    }

    @Override
    public Continent getByName(final String name)
            throws ResourceNotFoundException {
        var optionalContinent = continentRepository.getByName(name);
        if (optionalContinent.isPresent()) {
            cache.put(optionalContinent.get().getId(), optionalContinent.get());
        } else {
            throw new ResourceNotFoundException(
                    "Can't find continent because with this name doesn't exist");
        }
        return optionalContinent.get();
    }

    public Continent getByID(Long id) {
        return continentRepository.findById(id).orElse(null);
    }

    @Override
    public void update(final Continent continent) {
        cache.remove(continent.getId());
        continentRepository.save(continent);
    }

    @Transactional
    public void delete(Long id) throws ResourceNotFoundException {
        Optional<Continent> continent = continentRepository.findById(id);
        if (continent.isPresent()) {
            continentRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Can't delete continent with id = " + id + " because it does not exist.");
        }
    }


    @SuppressWarnings("checkstyle:OverloadMethodsDeclarationOrder")
    public void create(final ContinentDTO continentDto) throws BadRequestException {
        try {
            getByName(continentDto.getName());
            cache.remove(continentDto.getId());
            throw new BadRequestException("Continent with this name is existed");
        } catch (ResourceNotFoundException e) {
            var listLanguage = new HashSet<Language>();
            for (String ptrLanguage : continentDto.getLanguages()) {
                var language = languageRepository.getByName(ptrLanguage);
                language.ifPresent(listLanguage::add);
            }
            var newContinent = Continent.builder().name(continentDto.getName())
                    .languages(new HashSet<>()).id(findFreeID()).build();
            for (Language language : listLanguage) {
                newContinent.addLanguage(language);
            }
            create(newContinent);
        }
    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public void update(final ContinentDTO continent)
            throws ResourceNotFoundException {
        if (continentRepository.getContinentById(continent.getId()).isEmpty()) {
            throw new ResourceNotFoundException(
                    "Continent with this id doesn't exist");
        }
        var newLanguages = new HashSet<Language>();
        for (String language : continent.getLanguages()) {
            var languageTemp = languageRepository.getByName(language);
            languageTemp.ifPresent(newLanguages::add);
        }
        var updatedContinent = Continent.builder().name(continent.getName())
                .languages(new HashSet<>()).id(continent.getId()).build();
        for (Language language : newLanguages) {
            updatedContinent.addLanguage(language);
        }
        update(updatedContinent);
    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public void modifyLanguage(final ContinentDTO continentDto,
                               final boolean deleteFlag)
            throws ResourceNotFoundException {
        var continent = getByID(continentDto.getId());
        for (String language : continentDto.getLanguages()) {
            var tempLanguage = languageRepository.getByName(language);
            tempLanguage.ifPresent(!deleteFlag
                    ? continent::addLanguage : continent::removeLanguage);
        }
        cache.remove(continent.getId());
        continentRepository.save(continent);
    }

    public List<Continent> getByLanguage(final Long id)
            throws ResourceNotFoundException {
        var optionalLanguage = languageRepository.getLanguageById(id);
        if (optionalLanguage.isEmpty()) {
            throw new ResourceNotFoundException("No language with this name");
        }
        return continentRepository.findAllContinentWithLanguage(id);
    }

    public Continent updateContinent(Long id, String newName) throws Exception {
        Continent continent = continentRepository.findById(id).orElseThrow(() -> new Exception("Континент не найден"));
        continent.setName(newName);
        return continentRepository.save(continent);
    }

    public List<Continent> findAll() {
        return continentRepository.findAll();
    }

    public Continent findById(Long id) {
        return continentRepository.findById(id).orElse(null);
    }

    public void save(Continent continent) {
        continentRepository.save(continent);
    }

    public void deleteById(Long id) {
        continentRepository.deleteById(id);
    }

    @Override
    public void createBulk(final List<ContinentDTO> list)
            throws BadRequestException {
        List<Continent> continents = list.stream()
                .map(continentDTO -> Continent.builder()
                        .name(continentDTO.getName())
                        .build())
                .toList();
        String sql = "INSERT into continent (name) VALUES (?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement statement,
                                  final int i) throws SQLException {
                statement.setString(1, continents.get(i).getName());
            }

            @Override
            public int getBatchSize() {
                return continents.size();
            }
        });
    }
    public Continent updateContinent(Long id, ContinentDTO continentDTO) throws Exception {
        Continent continent = continentRepository.findById(id)
                .orElseThrow(() -> new Exception("Континент не найден"));
        continent.setName(continentDTO.getName()); // Обновляем имя континента
        return continentRepository.save(continent); // Возвращаем обновленный континент
    }
}