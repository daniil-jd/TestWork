package mimi.model;


import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Cat implements Serializable, Comparable<Cat>{
    private String name;
    private byte[] photo;
    private int rating;

    public Cat(String name, byte[] photo, int rating) {
        this.name = name;
        this.photo = photo;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return Base64.encodeBase64String(photo);
    }

    public byte[] getBytes() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getRating() {
        return rating;
    }

    public void setRating() {
        this.rating++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat cat = (Cat) o;
        return rating == cat.rating &&
                Objects.equals(name, cat.name) &&
                Arrays.equals(photo, cat.photo);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(name, rating);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }

    @Override
    public int compareTo(Cat o) {
        return Integer.compare(o.rating, rating);
    }
}
