package nl.hackaton.Models;

import com.google.gson.*;

import java.util.ArrayList;

public class Point{
    private Geometry geometry;
    private long locationCode;
    private String locationColor;
    private ArrayList<PointMeasurement> measurements;
    private String name;
    private String unitCode;
    private String windDirection;

    public Point(Geometry geometry, long locationCode, String locationColor, ArrayList<PointMeasurement> measurements, String name, String unitCode, String windDirection) {
        this.geometry = geometry;
        this.locationCode = locationCode;
        this.locationColor = locationColor;
        this.measurements = measurements;
        this.name = name;
        this.unitCode = unitCode;
        this.windDirection = windDirection;
    }

    public static ArrayList<Point> parseJSON(JsonArray jsonArray) {
        ArrayList<Point> points = new ArrayList<>();

        for(JsonElement jsonElement : jsonArray) {
            points.add(parseJSON(jsonElement.getAsJsonObject()));
        }

        return points;
    }

    public static Point parseJSON(JsonObject jsonObject) {
        Geometry geometry = Geometry.ParseJSON(jsonObject.getAsJsonObject("geometry"));
        JsonObject properties = jsonObject.getAsJsonObject("properties");
        long locationCode = properties.getAsJsonPrimitive("locationCode").getAsLong();
        String locationColor = null;
        if(properties.has("locationColor"))
            locationColor = properties.getAsJsonPrimitive("locationColor").getAsString();
        String name = properties.getAsJsonPrimitive("name").getAsString();

        ArrayList<PointMeasurement> measurements = PointMeasurement.parseJSON(properties.getAsJsonArray("measurements"));

        String unitCode = null;
        String windDirection = null;

        return new Point(geometry, locationCode, locationColor, measurements, name, unitCode, windDirection);
    }

    public static ArrayList<Point> filterPoints(ArrayList<Point> points) {
        return points;
    }

    public double sumMeasurements() {
        double sum = 0;
        for(PointMeasurement measurement : measurements) {
            sum += (double)measurement.getLatestValue() / (double)measurements.size();
        }
        return(sum);

    }
}
