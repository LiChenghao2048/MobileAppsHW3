package com.example.mobileappshw3;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class FragmentEpisode extends Fragment {

    private View view;

    private TextView textView_episode_name;
    private TextView textView_air_date;
    private ImageView imageView_char1;
    private ImageView imageView_char2;
    private ImageView imageView_char3;
    private Button button_more_info;

    private String episode_url = "https://rickandmortyapi.com/api/episode";
    private static AsyncHttpClient client = new AsyncHttpClient();

    private String notification_episode;
    private String notification_name;
    private String notification_url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_episode, container, false);

        textView_episode_name = view.findViewById(R.id.textView_episode_name);
        textView_air_date = view.findViewById(R.id.textView_air_date);
        imageView_char1 = view.findViewById(R.id.imageView_char1);
        imageView_char2 = view.findViewById(R.id.imageView_char2);
        imageView_char3 = view.findViewById(R.id.imageView_char3);
        button_more_info = view.findViewById(R.id.button_more_info);

        client.addHeader("Accept", "application/json");
        client.get(episode_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    JSONObject episode_overview = new JSONObject(new String(responseBody));
                    int count = Integer.parseInt(episode_overview.getJSONObject("info").getString("count"));

                    Random rand = new Random();
                    int random = rand.nextInt(count-1) + 1;
                    Log.d("random", String.valueOf(random));
                    episode_url += "/";
                    episode_url += String.valueOf(random);

                    client.get(episode_url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                JSONObject episode = new JSONObject(new String(responseBody));
                                textView_episode_name.setText(episode.getString("episode") + " " + episode.getString("name"));
                                textView_air_date.setText(episode.getString("air_date"));

                                notification_episode = episode.getString("episode");
                                notification_name = episode.getString("name");
                                notification_url = episode.getString("url");

                                JSONArray characters = episode.getJSONArray("characters");

                                client.get(characters.getString(0), new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        try {
                                            JSONObject character = new JSONObject(new String(responseBody));
                                            Picasso.get().load(character.getString("image")).into(imageView_char1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        log.d("api response", new String(responseBody));
                                    }
                                });

                                client.get(characters.getString(1), new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        try {
                                            JSONObject character = new JSONObject(new String(responseBody));
                                            Picasso.get().load(character.getString("image")).into(imageView_char2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        log.d("api response", new String(responseBody));
                                    }
                                });

                                client.get(characters.getString(2), new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        try {
                                            JSONObject character = new JSONObject(new String(responseBody));
                                            Picasso.get().load(character.getString("image")).into(imageView_char3);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        log.d("api response", new String(responseBody));
                                    }
                                });


                                button_more_info.setOnClickListener(v -> sendNotification(v));


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

    public void sendNotification(View view) {


        Notification notification = new Notification.Builder(getContext())
                .setContentTitle(notification_episode + " " + notification_name)
                .setContentText("To read more information about Episode " + notification_episode
                        + ", please visit: " + notification_url)
                .build();

        synchronized (notification) {notification.notify();}


        return;
    }

}
