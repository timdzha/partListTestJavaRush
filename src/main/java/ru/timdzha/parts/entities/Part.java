package ru.timdzha.parts.entities;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "parts")
public class Part {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "is_main")
    private Boolean isMainPart;

    public Part() {
    }

    public Part(String name, String category, Boolean isMainPart) {
        this.name = name;
        this.category = category;
        this.isMainPart = isMainPart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsMainPart() {
        return isMainPart;
    }

    public void setIsMainPart(Boolean mainPart) {
        isMainPart = mainPart;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
