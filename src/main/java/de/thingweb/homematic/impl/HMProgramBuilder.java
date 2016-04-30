package de.thingweb.homematic.impl;

public class HMProgramBuilder {
    private int id;
    private String name;
    private String description;
    private long timestamp = 0;

    public HMProgramBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public HMProgramBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public HMProgramBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public HMProgramBuilder setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public HMProgram createHMProgram() {
        return new HMProgram(id, name, description, timestamp);
    }
}