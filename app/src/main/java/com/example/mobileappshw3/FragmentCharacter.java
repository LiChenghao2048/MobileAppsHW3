package com.example.mobileappshw3;

import java.util.Random;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class FragmentCharacter extends Fragment {

    private View view;

    private ImageView imageView_photo;
    private TextView textView_name;
    private TextView textView_status;
    private TextView textView_species;
    private TextView textView_gender;
    private TextView textView_origin;
    private TextView textView_location;
    private TextView textView_episode;

    private String character_url = "https://rickandmortyapi.com/api/character";
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_character, container, false);

        imageView_photo = view.findViewById(R.id.imageView_photo);
        textView_name = view.findViewById(R.id.textView_name);
        textView_status = view.findViewById(R.id.textView_status);
        textView_species = view.findViewById(R.id.textView_species);
        textView_gender = view.findViewById(R.id.textView_gender);
        textView_origin = view.findViewById(R.id.textView_origin);
        textView_location = view.findViewById(R.id.textView_location);
        textView_episode = view.findViewById(R.id.textView_episode);

        client.addHeader("Accept", "application/json");
        client.get(character_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject character_overview = new JSONObject(new String(responseBody));
                    int count = Integer.parseInt(character_overview.getJSONObject("info").getString("count"));

                    Random rand = new Random();
                    int random = rand.nextInt(count-1) + 1;
                    Log.d("random", String.valueOf(random));
                    character_url += "/";
                    character_url += String.valueOf(random);

                    client.get(character_url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                JSONObject character = new JSONObject(new String(responseBody));
                                textView_name.setText(character.getString("name"));
                                textView_status.setText("Status: " + character.getString("status"));
                                textView_species.setText("Species: " + character.getString("species"));
                                textView_gender.setText("Gender: " + character.getString("gender"));
                                textView_origin.setText("Origin: " + character.getJSONObject("origin").getString("name"));
                                textView_location.setText("Location: " + character.getJSONObject("location").getString("name"));

                                String epi = "";
                                JSONArray episodes = character.getJSONArray("episode");
                                Log.d("episodes info", episodes.toString());
                                for (int i = 0; i < episodes.length() - 1; i++) {
                                    epi += episodes.getString(i).replaceAll("[^0-9]", "");
                                    epi += ", ";
                                }
                                epi += episodes.getString(episodes.length()-1).replaceAll("[^0-9]", "");

                                if (episodes.length() > 1) {
                                    textView_episode.setText("Appeared in Episodes " + epi);
                                } else {
                                    textView_episode.setText("Appeared in Episode " + epi);
                                }

                                Picasso.get().load(character.getString("image")).into(imageView_photo);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            log.d("api response", new String(responseBody));
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                log.d("api response", new String(responseBody));
            }
        });



        return view;
    }
}
