package com.avans.avanstv.Domain;

import androidx.annotation.NonNull;

public class Cast {
    public boolean adult;
    public int gender;
    public int id;
    public String known_for_department;
    public String name;
    public String original_name;
    public double popularity;
    public String profile_path;
    public int cast_id;
    public String character;
    public String credit_id;
    public int order;

    public boolean isAdult() {
        return adult;
    }

    public int getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public String getKnown_for_department() {
        return known_for_department;
    }

    public String getName() {
        return name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public int getCast_id() {
        return cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public int getOrder() {
        return order;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cast{" + "adult=" + adult + ", gender=" + gender + ", id=" + id + ", known_for_department='" + known_for_department +
                '\'' + ", name='" + name + '\'' + ", original_name='" + original_name + '\'' + ", popularity=" + popularity +
                ", profile_path='" + profile_path + '\'' + ", cast_id=" + cast_id + ", character='" + character + '\'' +
                ", credit_id='" + credit_id + '\'' + ", order=" + order + '}';
    }
}