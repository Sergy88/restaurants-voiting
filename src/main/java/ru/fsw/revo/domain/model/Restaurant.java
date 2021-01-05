package ru.fsw.revo.domain.model;

import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Data
@NamedQueries({
        @NamedQuery(name = Restaurant.GET, query = "SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menu m WHERE r.id=:rId")

})
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "restaurants_name_unique_idx")})
public class Restaurant extends AbstractNamedEntity {
    public static final String GET = "Restaurant.get";

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "dishes", joinColumns = {@JoinColumn(name = "rest_id", referencedColumnName = "id", nullable = false)},
            uniqueConstraints = {@UniqueConstraint(columnNames = { "rest_id", "dish_name"}, name = "dish_unique_idx")})
    @MapKeyColumn(name = "dish_name")
    @Column(name = "price", nullable = false)
    @BatchSize(size = 5)
    private Map<String, Integer> menu = new TreeMap<>();

    public Restaurant(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Restaurant() {
    }

    public Restaurant(String name, Map<String, Integer> menu) {
        this.name = name;
        this.menu = menu;
    }

    public Restaurant(long id, String name, Map<String, Integer> menu) {
        this.id = id;
        this.name = name;
        this.menu = menu;
    }

}
