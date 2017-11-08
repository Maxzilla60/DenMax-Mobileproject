package be.pxl.denmax.poopchasers.Repo;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.pxl.denmax.poopchasers.Exceptions.ToiletLocationIDNotFoundException;
import be.pxl.denmax.poopchasers.Model.Toilet;
import be.pxl.denmax.poopchasers.Model.ToiletComment;
import be.pxl.denmax.poopchasers.Model.ToiletTag;
import be.pxl.denmax.poopchasers.Model.ToiletTags;

import static be.pxl.denmax.poopchasers.Model.ToiletTag.*;

public class ToiletRepository {
    private static List<Toilet> toiletLocations;
    private static int idIncrementer = 0;

    static {
        toiletLocations = new ArrayList<Toilet>();
        toiletLocations.add(new Toilet(idIncrementer++, "Hier", 1,2));
        toiletLocations.add(new Toilet(idIncrementer++, "Daar", 5,4));
        toiletLocations.add(new Toilet(idIncrementer++, "Overal", 50.953270,5.353262));
        toiletLocations.add(new Toilet(idIncrementer++, "Stink Twalet", 50.9382132,5.3461862));
        toiletLocations.add(new Toilet(idIncrementer++, "Dennis' Badkamer Emporium", 50.771984,5.4604808));
        Toilet quick = new Toilet(idIncrementer++, "Quick", 50.9303555,5.3692793);
        quick.addComment(new ToiletComment("Wauw, lekker eten en lekker kakken!", "JefDeVoetballer", 2000));
        quick.addComment(new ToiletComment("Het stinkt naar de frieten.", "Els_DikkeKont", 3));
        quick.addComment(new ToiletComment("Ik kom zeker nog eens langs. De zeep die ze hier hebben voor m'n handen te wassen ruikt lekker.", "PowerFlower2354", 5));
        quick.addComment(new ToiletComment("Ik kom zeker nog eens langs. De zeep die ze hier hebben voor m'n handen te wassen ruikt lekker.Ik kom zeker nog eens langs. De zeep die ze hier hebben voor m'n handen te wassen ruikt lekker.Ik kom zeker nog eens langs. De zeep die ze hier hebben voor m'n handen te wassen ruikt lekker.Ik kom zeker nog eens langs. De zeep die ze hier hebben voor m'n handen te wassen ruikt lekker.Ik kom zeker nog eens langs. De zeep die ze hier hebben voor m'n handen te wassen ruikt lekker.", "PowerFlower2354", 4));
        quick.addComment(new ToiletComment("Ik kom zeker nog eens langs. De zeep die ze hier hebben voor m'n handen te wassen ruikt lekker.", "PowerFlower2354", 5));
        quick.addComment(new ToiletComment("Ik kom zeker nog eens langs. De zeep die ze hier hebben voor m'n handen te wassen ruikt lekker.", "PowerFlower2354", 5));
        quick.addTags(FREE, MENS, WOMENS);
        toiletLocations.add(quick);
    }

    private ToiletRepository() {

    }

    public interface ToiletUpdateListener {
        void onToiletUpdate(List<Toilet> toilets);
    }

    public interface ToiletCommentUpdateListener {
        void onToiletCommentUpdate(ArrayList<ToiletComment> toiletComments);
    }

//    public static List<Toilet> getAllToiletLocations() {
//        return new ArrayList<Toilet>(toiletLocations);
//    }

    public static void getAllToiletLocations(final ToiletUpdateListener listener, RequestQueue queue) {
        String url = "http://dennisheperol.be/toilets";
        getToiletLocationsFrom(listener, queue, url);
    }

    public static void getToiletLocationByID(final ToiletUpdateListener listener, RequestQueue queue, int id){
        String url = "http://dennisheperol.be/toilets/id/"+id;
        getToiletLocationsFrom(listener, queue, url);
    }

//    public static Toilet getToiletLocationByID(int id) throws ToiletLocationIDNotFoundException {
//        for (Toilet t : toiletLocations) {
//            if (t.getId() == id) {
//                return new Toilet(t);
//            }
//        }
//        throw new ToiletLocationIDNotFoundException();
//    }


    private static void getToiletLocationsFrom(final ToiletUpdateListener listener, RequestQueue queue, final String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("test", url);
                            Log.i("test", response);
                            JSONArray arr = new JSONArray(response);
                            List<Toilet> toiletList= new ArrayList<>();
                            for(int i = 0; i<arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                int id = obj.getInt("id");
                                String name = obj.getString("name");
                                double lat = obj.getDouble("latitude");
                                double lon = obj.getDouble("longitude");
                                ToiletTags tags = intToTags(obj.getInt("tags"));
                                toiletList.add(new Toilet(id, name, lat, lon, tags));
                            }
                            listener.onToiletUpdate(toiletList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("test", "ErrorResponse");
            }
        });
        queue.add(stringRequest);
    }

    public static void addCommentToToiletLocation(RequestQueue queue, ToiletComment comment, int id) throws ToiletLocationIDNotFoundException {
        String url = "http://dennisheperol.be/comments/add";

        final JSONObject obj = new JSONObject();
        try {
            obj.put("toiletid", id);
            obj.put("rating", comment.getRating());
            obj.put("comment", comment.getContent());
            obj.put("user", comment.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("test", "ResponsePost: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("test", "ErrorPost: " + error);
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return obj.toString().getBytes();
            }
        };
        queue.add(stringRequest);
    }

    public static void addToiletLocation(RequestQueue queue, Toilet toilet){
        String url = "http://dennisheperol.be/toilets/add";

        final JSONObject obj = new JSONObject();
        try {
            obj.put("name", toilet.getName());
            obj.put("lat", toilet.getLatLng().latitude);
            obj.put("long", toilet.getLatLng().longitude);
            obj.put("tags", tagsToInt(toilet.getToiletTags()));
            obj.put("id", toilet.getId());
            obj.put("user", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("test", "ResponsePost: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("test", "ErrorPost: " + error);
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return obj.toString().getBytes();
            }
        };
        queue.add(stringRequest);
    }

//    public static List<Toilet> getToiletLocationsByTags(ToiletTag... queryTags) {
//        List<Toilet> filteredLocations = new ArrayList<Toilet>();
//        for (Toilet location : toiletLocations) {
//            for (ToiletTag query : queryTags) {
//                if (location.hasTag(query)) {
//                    filteredLocations.add(new Toilet(location));
//                    break;
//                }
//            }
//        }
//        return filteredLocations;
//    }

    public static List<Toilet> getToiletLocationsByTags(final ToiletUpdateListener listener, RequestQueue queue, final ToiletTag... queryTags) {
        String url = "http://dennisheperol.be/toilets";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Toilet> filteredLocations = new ArrayList<>();
                        try {
                            JSONArray arr = new JSONArray(response);
                            List<Toilet> toiletList= new ArrayList<>();
                            for(int i = 0; i<arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                int id = obj.getInt("id");
                                String name = obj.getString("name");
                                double lat = obj.getDouble("latitude");
                                double lon = obj.getDouble("longitude");
                                ToiletTags tags = intToTags(obj.getInt("tags"));
                                toiletList.add(new Toilet(id, name, lat, lon, tags));


                                for (Toilet location : toiletList) {
                                    for (ToiletTag query : queryTags) {
                                        if (location.hasTag(query)) {
                                            filteredLocations.add(new Toilet(location));
                                            break;
                                        }
                                    }
                                }
                            }
                            listener.onToiletUpdate(filteredLocations);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("test", "ErrorResponse");
            }
        });
        queue.add(stringRequest);


        List<Toilet> filteredLocations = new ArrayList<Toilet>();
        for (Toilet location : toiletLocations) {
            for (ToiletTag query : queryTags) {
                if (location.hasTag(query)) {
                    filteredLocations.add(new Toilet(location));
                    break;
                }
            }
        }
        return filteredLocations;
    }

    public static void getToiletCommentsById(final ToiletCommentUpdateListener listener, RequestQueue queue, int id){
        String url = "http://dennisheperol.be/comments/toiletid/"+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            ArrayList<ToiletComment> commentList= new ArrayList<>();
                            for(int i = 0; i<arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                //int id = obj.getInt("id");
                                //int toiletid = obj.getInt("toiletid");
                                int rating = obj.getInt("rating");
                                String comment = obj.getString("comment");
                                String user = obj.getString("user");
                                commentList.add(new ToiletComment(comment, user, rating));
                            }
                            listener.onToiletCommentUpdate(commentList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("test", "ErrorResponse");
            }
        });
        queue.add(stringRequest);
    }

    static private int tagsToInt(ToiletTags tags){
        int total = 0x00;
        if (tags.hasTag(MENS))
            total += 0x01;
        if (tags.hasTag(WOMENS))
            total += 0x02;
        if (tags.hasTag(BABIES))
            total += 0x04;
        if (tags.hasTag(ACCESSIBLE))
            total += 0x08;
        if (tags.hasTag(UNISEX))
            total += 0x0f;

        return total;
    }

    static private ToiletTags intToTags(int total){
        ToiletTags tags = new ToiletTags();
        if(total >= 0x0f) {
            tags.addTag(UNISEX);
            total -= 0x0f;
        }
        if(total >= 0x08){
            tags.addTag(ACCESSIBLE);
            total -= 0x08;
        }
        if(total >= 0x04){
            tags.addTag(BABIES);
            total -= 0x04;
        }
        if(total >= 0x02){
            tags.addTag(WOMENS);
            total -= 0x02;
        }
        if(total >= 0x01){
            tags.addTag(MENS);
            total -= 0x01;
        }
        return tags;
    }
}
