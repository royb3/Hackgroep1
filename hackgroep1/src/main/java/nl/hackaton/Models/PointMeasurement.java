package nl.hackaton.Models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public class PointMeasurement {
    ZonedDateTime dateTime;
    long latestValue;
    private String unitCode;
    private String parameterId;

    PointMeasurement(ZonedDateTime dateTime, long latestValue, String unitCode, String parameterId) {
        this.dateTime = dateTime;
        this.latestValue = latestValue;
        this.unitCode = unitCode;
        this.parameterId = parameterId;
    }

    public static ArrayList<PointMeasurement> parseJSON(JsonArray pointMeasurements) {
        ArrayList<PointMeasurement> measurements = new ArrayList<>();
        for(JsonElement jsonElement : pointMeasurements) {
            measurements.add(parseJSON(jsonElement.getAsJsonObject()));
        }
        return measurements;
    }

    public static PointMeasurement parseJSON(JsonObject pointMeasurement) {
        ZonedDateTime dateTime = ZonedDateTime.parse(pointMeasurement.getAsJsonPrimitive("dateTime").getAsString());
        long latestValue = pointMeasurement.getAsJsonPrimitive("latestValue").getAsLong();
        String unitCode = pointMeasurement.getAsJsonPrimitive("unitCode").getAsString();
        String parameterId = pointMeasurement.getAsJsonPrimitive("parameterId").getAsString();

        return new PointMeasurement(dateTime, latestValue, unitCode, parameterId);
    }
}
