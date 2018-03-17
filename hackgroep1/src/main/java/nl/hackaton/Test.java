package nl.hackaton;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.iotf.client.app.ApplicationClient;
import com.ibm.iotf.client.device.DeviceClient;
import com.ibm.iotf.client.gateway.GatewayClient;
import nl.hackaton.Models.Point;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Properties;

public class Test {
    private final static String PROPERTIES_FILE_NAME = "/device.properties";

    public static void main(String[] args) throws Exception {

        /**
         * Load device properties
         */
        Properties props = new Properties();
        try {
            props.load(Test.class.getResourceAsStream(PROPERTIES_FILE_NAME));
        } catch (IOException e1) {
            System.err.println("Not able to read the properties file, exiting..");
            System.exit(-1);
        }

        DeviceClient myClient = null;
        try {
            //Instantiate and connect to IBM Watson IoT Platform
            myClient = new DeviceClient(props);
            myClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }


        /**
         * Publishes the process load event for every 1 second
         */
        long counter = 0;
        boolean status = true;
        while(true) {
            try {
                //Generate a JSON object of the event to be published
                JsonObject event = new JsonObject();
                bla(event);
                status = myClient.publishEvent("load", event);
                System.out.println(event);
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(!status) {
                System.out.println("Failed to publish the event......");
                System.exit(-1);
            }
        }
    }

    static void bla(JsonObject event) throws IOException {
        URI uri = URI.create("http://waterinfo.rws.nl/api/point/latestmeasurements?parameterid=waterhoogte-t-o-v-nap");
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            StringBuilder jsonResponseStringBuilder = new StringBuilder();

            int readBytes, availableBytes;
            char[] buffer = new char[128];

            while ((readBytes = inputStreamReader.read(buffer, 0, 128)) != -1) {
                jsonResponseStringBuilder.append(buffer, 0, readBytes);
            }

            inputStreamReader.close();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(jsonResponseStringBuilder.toString());

            ArrayList<Point> points = Point.parseJSON(jsonElement.getAsJsonObject().getAsJsonArray("features"));

            double sum = 0.0;
            for(Point point : points) {
                sum += (double)point.sumMeasurements() / (double)points.size();
            }
            event.addProperty("meanWaterLevel", (sum));
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
