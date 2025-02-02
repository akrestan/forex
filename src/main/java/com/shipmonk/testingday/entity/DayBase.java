package com.shipmonk.testingday.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.*;

/**
 * @author Ales Krestan
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "day_base")
public class DayBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day", nullable = false, length = 10)
    private String day;

    @Column(name = "base_code", nullable = false, length = 3)
    private String baseCode;

    @Column(name = "obtained_at")
    private Instant obtainedAt;

    @OneToMany(mappedBy = "dayBase", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CurrencyRate> currencyRates = new HashSet<>();

    public void setCurrencyRates(Set<CurrencyRate> newCurrencyRates) {
        this.currencyRates.clear();
        if (newCurrencyRates != null) {
            this.currencyRates.addAll(newCurrencyRates);
        }
    }

}
