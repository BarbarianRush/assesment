package com.example.assesment.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcastrillo on 04/10/2015.
 */

public class Gnome implements Parcelable {

    private String id;
    private String name;
    private String thumbnail;
    private String age;
    private String weight;
    private String height;
    private String hair_color;
    private String gender;

    private List<String> professions;
    private List<String> friends;

    public Gnome(){
        professions = new ArrayList<String>();
        friends = new ArrayList<String>();
    }

    public Gnome(Parcel in){

        String[] data = new String[8];
        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];
        this.thumbnail = data[2];
        this.age = data[3];
        this.weight = data[4];
        this.height = data[5];
        this.hair_color = data[6];
        this.gender = data[7];
        professions = in.readArrayList(String.class.getClassLoader());
        friends = in.readArrayList(String.class.getClassLoader());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.id,this.name,this.thumbnail,this.age,this.weight,this.height,this.hair_color,this.gender});
        dest.writeList(professions);
        dest.writeList(friends);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Gnome createFromParcel(Parcel in) {
            return new Gnome(in);
        }

        public Gnome[] newArray(int size) {
            return new Gnome[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHair_color() {
        return hair_color;
    }

    public void setHair_color(String hair_color) {
        this.hair_color = hair_color;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getProfessions() {
        return professions;
    }

    public void setProfessions(List<String> professions) {
        this.professions = professions;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }
}
