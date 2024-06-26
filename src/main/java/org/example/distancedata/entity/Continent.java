package org.example.distancedata.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "continent")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Continent {
    public Continent(Long id, String name, List<Country> countries) {
        this.id = id;
        this.name = name;
        this.countries = countries;
    }

    public Continent(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "continent",
            cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Country> countries = new ArrayList<>();
    @ManyToMany(cascade = {CascadeType.MERGE,
            CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "connection",
            joinColumns = {@JoinColumn(name = "id_continent")},
            inverseJoinColumns = {@JoinColumn(name = "id_language")})
    @JsonManagedReference
    private Set<Language> languages = new HashSet<>();


        public Continent(String name) {
            this.name = name;
        }


    public void addLanguage(final Language language) {
        languages.add(language);
    }

    public void removeLanguage(final Language language) {
        languages.remove(language);
    }
}