package id.ac.unila.ilkom.mkpilihan;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    ListView listView;
    String[] listItems;
    private String url = "http://employeee.dx.am/api/getDataEmployee.php?view=json";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    ArrayList<String> employeeList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAndRequestResponse();
            }
        });

        listView=(ListView) findViewById(R.id.listView);
//        listItems=getResources().getStringArray(R.array.array_technology);
//
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,
//                android.R.id.text1,listItems
//        );
//
//        listView.setAdapter(adapter);
        sendAndRequestResponse();
    }

    private void sendAndRequestResponse() {

        //RequestQueue initialized
        if (employeeList.size()>0)
            employeeList.clear();

        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

                parseJSON(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void parseJSON(String data){

        try{
            JSONArray jsonMainNode = new JSONArray(data);

            int jsonArrLength = jsonMainNode.length();

            for(int i=0; i < jsonArrLength; i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String first_name = jsonChildNode.getString("first_name");
                String last_name = jsonChildNode.getString("last_name");
                employeeList.add(first_name+" "+last_name);
            }

            // Get ListView object from xml
//            listView = (ListView) findViewById(R.id.list);

            // Define a new Adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, employeeList);

            // Assign adapter to ListView
            listView.setAdapter(adapter);

        }catch(Exception e){
            Log.i("App", "Error parsing data" +e.getMessage());

        }
    }

}
