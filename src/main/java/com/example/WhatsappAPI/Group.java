package com.example.WhatsappAPI;

public class Group {

    private String name;
    private int noOfParticipant;

    public Group(String name, int noOfParticipant) {
        this.name = name;
        this.noOfParticipant = noOfParticipant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfParticipant() {
        return noOfParticipant;
    }

    public void setNoOfParticipant(int noOfParticipant) {
        this.noOfParticipant = noOfParticipant;
    }
}
