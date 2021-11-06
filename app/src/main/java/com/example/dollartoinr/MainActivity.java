package com.example.dollartoinr;

import static android.os.Debug.getNativeHeapAllocatedSize;
import static android.os.Debug.getNativeHeapFreeSize;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Debug;
import android.os.Process;
import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dollartoinr.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    // essential URL structure is built using constants
    public static final String ACCESS_KEY = "Add your access Key.";
    public static final String BASE_URL = "http://api.currencylayer.com/";
    public static final String ENDPOINT = "live";


    /**
     * Notes:
     * <p>
     * A JSON response of the form {"key":"value"} is considered a simple Java JSONObject.
     * To get a simple value from the JSONObject, use: .get("key");
     * <p>
     * A JSON response of the form {"key":{"key":"value"}} is considered a complex Java JSONObject.
     * To get a complex value like another JSONObject, use: .getJSONObject("key")
     * <p>
     * Values can also be JSONArray Objects. JSONArray objects are simple, consisting of multiple JSONObject Objects.
     */

    public void convertToINR(View view) throws IOException, JSONException {
        sendLiveRequest();
    }

    // sendLiveRequest() function is created to request and retrieve the data
    public void sendLiveRequest() throws MalformedURLException {

        // The following line initializes the HttpGet Object with the URL in order to send a request
        String url = BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Double dollarToINR = response.getJSONObject("quotes").getDouble("USDINR");
                            EditText text = findViewById(R.id.EnterAmount);
                            String dollar = text.getText().toString();
                            Double doubleDollar = Double.parseDouble(dollar);
                            double doubleINR = doubleDollar * dollarToINR;
                            String toast = "It is equal to " + doubleINR +" INR";
                            Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e){
                            Toast.makeText(MainActivity.this,
                                    "Please enter a valid number",
                                    Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this,
                                    e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,
                                error.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //int[] MemoryInfo memory = getProcessMemoryInfo(pids);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        int id = Process.myPid();
        long freeSize = getNativeHeapFreeSize();
        long alocate = getNativeHeapAllocatedSize();
        Log.i("Debug", "PID "+ id);
        Log.i("Debug", "Free "+ freeSize);
        Log.i("Debug", "Allocate " + alocate);

        int[] pids = new int[1];
        pids[0] = id;

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(pids);
        for (Debug.MemoryInfo i: memoryInfos){
            Log.i("Debug", i.toString());
            Log.i("Debug", "PD:  " + i.getTotalPrivateDirty());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.i("Debug", " Get Memory Stat" + i.getMemoryStats().toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
        return true;
    }
}