package org.example.distancedata.services.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.distancedata.cache.LRUCache;
import org.example.distancedata.dto.CountryDTO;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.entity.Country;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.repository.ContinentRepository;
import org.example.distancedata.repository.CountryRepository;
import org.example.distancedata.services.DataService;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements DataService<Country, CountryDTO> {
    private final CountryRepository repository;
    private final ContinentRepository continentRepository;
    private final LRUCache<Long, Country> cache;
    private final JdbcTemplate jdbcTemplate;
    private static final String DONT_EXIST = " doesn't exist";

    public long findFreeId() {
        var list = read();
        long i = 1;
        for (Country countryInfo : list) {
            if (countryInfo.getId() != i) {
                return i;
            }
            i++;
        }
        return i + 1;
    }

    public long findFreeId(final HashSet<Long> usedIndexes) {
        // Собираем все существующие идентификаторы из базы данных
        var existingIds = new HashSet<Long>();
        var countries = read(); // Предполагается, что read() возвращает все языки
        for (Country country : countries) {
            existingIds.add(country.getId());
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

    public Country createWithContinent(final CountryDTO country,
                                  final Continent continent)
            throws BadRequestException {
        var newCountry = Country.builder().build();
        try {
            getByName(country.getName());
            throw new BadRequestException("Can't create because already exist");
        } catch (ResourceNotFoundException e) {
            newCountry = Country.builder().name(country.getName())
                    .latitude(country.getLatitude()).longitude(country.getLongitude())
                    .continent(continent).id(findFreeId()).build();
            create(newCountry);
        }
        return newCountry;
    }

    @Override
    public Country create(final Country country) throws BadRequestException {
        repository.save(country);
        cache.put(country.getId(), country);
        return country;
    }

    public void updateWithContinent(final CountryDTO country,
                                  final Continent continent)
            throws ResourceNotFoundException {
        try {
            var newCountry = getByID(country.getId());
            if (!country.getLongitude().isNaN()) {
                newCountry.setLongitude(country.getLongitude());
            }
            if (!country.getLatitude().isNaN()) {
                newCountry.setLatitude(country.getLatitude());
            }
            if (!country.getName().isEmpty()) {
                newCountry.setName(country.getName());
            }
            newCountry.setContinent(continent);
            update(newCountry);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Can't update country with this id"
                    + country.getId() + DONT_EXIST);
        }
    }

    @Override
    public List<Country> read() {
        return repository.findAll(Sort.by("id"));
    }

    @Override
    public Country getByName(final String name) throws ResourceNotFoundException {
        var optionalCountry = repository.getCountryByName(name);
        if (optionalCountry.isPresent()) {
            cache.put(optionalCountry.get().getId(), optionalCountry.get());
        } else {
            throw new ResourceNotFoundException(
                    "Can't find country because with this name doesn't exist");
        }
        return optionalCountry.get();
    }

    @Override
    public Country getByID(final Long id) throws ResourceNotFoundException {
        Optional<Country> optionalCountry = cache.get(id);
        if (optionalCountry.isEmpty()) {
            optionalCountry = repository.getCountryById(id);
            if (optionalCountry.isPresent()) {
                cache.put(id, optionalCountry.get());
            } else {
                throw new ResourceNotFoundException(
                        "Cannot find country with id = " + id + DONT_EXIST);
            }
        }
        return optionalCountry.get();
    }

    @Override
    public void update(final Country country) throws ResourceNotFoundException {
        cache.remove(country.getId());
        repository.save(country);
        cache.put(country.getId(), country);
    }

    public void update(final CountryDTO country) throws ResourceNotFoundException {
        var oldCountry = cache.get(country.getId());
        if (oldCountry.isEmpty()) {
            oldCountry = repository.getCountryById(country.getId());
            if (oldCountry.isEmpty()) {
                throw new ResourceNotFoundException("Can't find country with id = "
                        + country.getId() + DONT_EXIST);
            }
        }
        oldCountry.get().setName(country.getName());
        oldCountry.get().setLatitude(country.getLatitude());
        oldCountry.get().setLongitude(country.getLongitude());
        update(oldCountry.get());
    }

    @Override
    public void delete(final Long id)
            throws ResourceNotFoundException {
        if (repository.getCountryById(id).isPresent()) {
            repository.deleteById(id);
            cache.remove(id);
        } else {
            throw new ResourceNotFoundException("Can't delete country with id = "
                    + id + DONT_EXIST);
        }
    }

    @Transactional
    @Override
    public void createBulk(final List<CountryDTO> list)
            throws BadRequestException {
        List<Country> countries = list.stream()
                .map(countryDTO -> {
                    var continent = continentRepository.getContinentById(countryDTO.getContinent());
                    if (continent.isPresent()) {
                        return Country.builder().name(countryDTO.getName()).continent(continent.get())
                                .latitude(countryDTO.getLatitude())
                                .longitude(countryDTO.getLongitude()).build();
                    }
                    return Country.builder().build();
                }).filter(country -> country.getContinent() != null).toList();
        String sql = "INSERT into country (name, id, latitude, longitude, continent)"
                + "VALUES (?, ?, ?, ?, ?)";
        var indexes = new HashSet<Long>();
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement statement,
                                  final int i)
                    throws SQLException {
                statement.setString(1, countries.get(i).getName());
                long index = findFreeId(indexes);
                indexes.add(index);
                statement.setLong(2, index);
                statement.setDouble(3, countries.get(i).getLatitude());
                statement.setDouble(4, countries.get(i).getLongitude());
                statement.setDouble(5, countries.get(i).getContinent().getId());
            }

            @Override
            public int getBatchSize() {
                return countries.size();
            }
        });
    }

    public List<Country> getBetweenLatitudes(final Double first,
                                          final Double second) {
        return repository.findAllCountryWithLatitudeBetween(first, second);
    }
}