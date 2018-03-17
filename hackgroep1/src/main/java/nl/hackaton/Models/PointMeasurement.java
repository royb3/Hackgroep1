package nl.hackaton.Models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public class PointMeasurement {
    public static class MeasurementOutOfBoundsException extends Exception {}
    private ZonedDateTime dateTime;
    private long latestValue;
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
            try {
                measurements.add(parseJSON(jsonElement.getAsJsonObject()));
            } catch (MeasurementOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return measurements;
    }

    public static PointMeasurement parseJSON(JsonObject pointMeasurement) throws MeasurementOutOfBoundsException {
        ZonedDateTime dateTime = ZonedDateTime.parse(pointMeasurement.getAsJsonPrimitive("dateTime").getAsString());
        long latestValue = pointMeasurement.getAsJsonPrimitive("latestValue").getAsLong();
        if(latestValue > 2000000) {
            throw new MeasurementOutOfBoundsException();
        }

        String unitCode = pointMeasurement.getAsJsonPrimitive("unitCode").getAsString();
        String parameterId = pointMeasurement.getAsJsonPrimitive("parameterId").getAsString();

        return new PointMeasurement(dateTime, latestValue, unitCode, parameterId);
    }

    public long getLatestValue() {
        return latestValue;
    }
}
