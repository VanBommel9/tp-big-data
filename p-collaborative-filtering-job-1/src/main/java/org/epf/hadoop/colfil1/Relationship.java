package org.epf.hadoop.colfil1;

public class Relationship {
    private String user1;
    private String user2;
    private double weight;

    public Relationship(String user1, String user2, double weight) {
        this.user1 = user1;
        this.user2 = user2;
        this.weight = weight;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return user1 + "\t" + user2 + "\t" + weight;
    }
}
