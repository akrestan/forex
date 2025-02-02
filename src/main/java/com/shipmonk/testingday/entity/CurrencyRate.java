package com.shipmonk.testingday.entity;

import java.math.BigDecimal;

import javax.persistence.*;

import lombok.*;

/**
 * @author Ales Krestan
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency_rate")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_base_id")
    private DayBase dayBase;

    @EqualsAndHashCode.Include
    @Column(length = 3)
    private String code;

    @Column(precision = 10, scale = 10)
    private BigDecimal rate;

}
