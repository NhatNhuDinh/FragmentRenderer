package com.company.inlineeditingfragments.entity;

import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@JmixEntity
@Table(name = "BOOK", indexes = {
        @Index(name = "IDX_BOOK_AUTHOR", columnList = "AUTHOR_ID")
})
@Entity
public class Book {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @InstanceName
    @Column(name = "TITLE")
    private String title;

    @Column(name = "COVER", length = 1024)
    private FileRef cover;

    @JoinColumn(name = "AUTHOR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    @Column(name = "PUBLIC_DATE")
    private LocalDate publicDate;

    @Column(name = "PRICE")
    private Float price;

    @Column(name = "QUANTITY")
    private Integer quantity;

    public void setCover(FileRef cover) {
        this.cover = cover;
    }

    public FileRef getCover() {
        return cover;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDate getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(LocalDate publicDate) {
        this.publicDate = publicDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}