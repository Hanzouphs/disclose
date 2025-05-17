package com.disclose.paws.model;

import com.disclose.paws.model.enums.PetSizeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long age;
    private String gender;
    private String breed;
    private PetSizeEnum size;
    @Column(columnDefinition = "boolean default false")
    private Boolean castrated = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean dewormed  = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean vaccinated = false;
    private String description;
    private String imageUrl;

    @Version
    private Long version;

    @ManyToMany(mappedBy = "sponsoredPets")
    private Set<UserEntity> sponsors = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PetEntity petEntity = (PetEntity) o;
        return getId() != null && Objects.equals(getId(), petEntity.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}