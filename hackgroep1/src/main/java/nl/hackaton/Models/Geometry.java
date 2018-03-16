package nl.hackaton.Models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Geometry {
    private double x, y;

    public Geometry(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static Geometry ParseJSON(JsonObject jsonObject) {
        JsonArray axis = jsonObject.getAsJsonArray("coordinates");
        assert axis.size() == 2;

        double x = axis.get(0).getAsDouble();
        double y = axis.get(1).getAsDouble();

        return new Geometry(x, y);
    }
}
