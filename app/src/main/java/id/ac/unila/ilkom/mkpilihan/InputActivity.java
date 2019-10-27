package id.ac.unila.ilkom.mkpilihan;

import android.os.Bundle;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class InputActivity extends AppCompatActivity {
    private String url = "http://didik.atwebpages.com/api/createMKPilihan.php";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static final String PROTOCOL_CHARSET = "utf-8";

    EditText kode;
    EditText namaMK;
    EditText sks;
    EditText kuota;
    EditText dosen;
    Spinner semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        kode = (EditText) findViewById(R.id.edKode);
        namaMK = (EditText) findViewById(R.id.edNamaMK);
        sks = (EditText) findViewById(R.id.edSKS);
        kuota = (EditText) findViewById(R.id.edKuota);
        dosen = (EditText) findViewById(R.id.edDosen);
        semester = (Spinner) findViewById(R.id.spSemester);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void saveData(View view){
        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // response
                    Log.d("Response", response);
                    try{
                        JSONObject res = new JSONObject(response);
                        Snackbar.make(view, res.getString("msg"), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (Exception e){
                        Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                },
                error -> {
                    // error
                    String msg="";
                    NetworkResponse  errorResponse=error.networkResponse;
                    if(error != null && error.getMessage() != null)
                    {
                        Log.e("Response error", error.getMessage());
                    }
                    try {
                        switch (error.networkResponse.statusCode){
                            case 503 :
                                String jsonString = new String(errorResponse.data,
                                        HttpHeaderParser.parseCharset(errorResponse.headers, PROTOCOL_CHARSET));

                                Log.e("Response error",jsonString);

                                JSONObject res = new JSONObject(jsonString);
                                msg=res.getString("msg");
                                break;
                            default :
                                Log.d("Response error", "onErrorResponse: "+error.toString());
                                break;
                        }
                        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (JSONException e){
                        Snackbar.make(view, "Error parsing json", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (UnsupportedEncodingException e) {
                        Snackbar.make(view, "Error parsing string", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode_mk",kode.getText().toString());
                params.put("nama_mk",namaMK.getText().toString());
                params.put("semester",semester.getSelectedItem().toString());
                params.put("kuota",kuota.getText().toString());
                params.put("sks",sks.getText().toString());
                params.put("dosen",dosen.getText().toString());
                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

}
