package de.thingweb.homematic.impl;

/**
 * Created by Johannes on 30.04.2016.
 */
public class HMProgram {
    int id;
    String name;
    String description;
    long timestamp;

    public HMProgram(int id, String name, String description, long timestamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "HMProgram{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
