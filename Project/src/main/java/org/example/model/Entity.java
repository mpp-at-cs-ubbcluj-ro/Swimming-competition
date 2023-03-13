package org.example.model;
import java.io.Serializable;

/**
 * Superclass for all the entities
 * Implements Serializable so every entity can be serialized
 * @param <ID> the type of the Entity's id
 */
public class Entity<ID> implements Serializable {
    private static final long serialVersionUID = 7331115341259248461L;
    private ID id;
    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }
}

