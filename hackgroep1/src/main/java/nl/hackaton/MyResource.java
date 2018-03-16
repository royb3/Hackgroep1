package nl.hackaton;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import nl.hackaton.Models.Point;
import sun.net.www.http.HttpClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
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

            while((readBytes = inputStreamReader.read(buffer, 0, 128)) != -1) {
                jsonResponseStringBuilder.append(buffer, 0, readBytes);
            }

            inputStreamReader.close();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(jsonResponseStringBuilder.toString());

            ArrayList<Point> points = Point.parseJSON(jsonElement.getAsJsonObject().getAsJsonArray("features"));

            return jsonResponseStringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Got it!";
    }


}
