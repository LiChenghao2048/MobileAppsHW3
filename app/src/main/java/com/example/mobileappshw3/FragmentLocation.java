package com.example.mobileappshw3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class FragmentLocation extends Fragment {

    private View view;
    private RecyclerView recyclerView_locations;
    private ArrayList<Location> locations;

    private String location_url = "https://rickandmortyapi.com/api/location";
    private static AsyncHttpClient client = new AsyncHttpClient();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);
        recyclerView_locations = view.findViewById(R.id.recyclerView_locations);
        locations = new ArrayList<>();

        client.addHeader("Accept", "application/json");
        client.get(location_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject location_overview = new JSONObject(new String(responseBody));
                    JSONArray results = location_overview.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject locationObject = results.getJSONObject(i);
                        Location location = new Location(locationObject.getString("name"),
                                locationObject.getString("type"),
                                locationObject.getString("dimension"));
                        locations.add(location);
                    }
                    LocationAdapter adapter = new LocationAdapter(locations);
                    recyclerView_locations.setAdapter(adapter);
                    recyclerView_locations.setLayoutManager(new LinearLayoutManager(getContext()));

                    recyclerView_locations.setHasFixedSize(true);
                    RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                                                                    DividerItemDecoration.VERTICAL);
                    recyclerView_locations.addItemDecoration(itemDecoration);

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
