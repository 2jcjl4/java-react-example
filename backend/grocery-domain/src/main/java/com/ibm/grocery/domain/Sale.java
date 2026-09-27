package com.ibm.grocery.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale", uniqueConstraints = @UniqueConstraint(columnNames = "reference"))
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String reference;

    @Column(name = "sold_by", nullable = false, length = 40)
    private String soldBy;

    @Column(name = "sold_at", nullable = false)
    private Instant soldAt = Instant.now();

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SaleLine> lines = new ArrayList<>();

    public void addLine(SaleLine line) {
        line.setSale(this);
        lines.add(line);
        totalAmount = totalAmount.add(line.getLineTotal());
    }

    public Long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public Instant getSoldAt() {
        return soldAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<SaleLine> getLines() {
        return lines;
    }
}
