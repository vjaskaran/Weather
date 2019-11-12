package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    public class  DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url=null;
            HttpURLConnection urlConnection=null;
            try{
                url=new URL(urls[0]) ;
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Could not find weather!", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.i("weather","hello weather is  "+s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weather", weatherInfo);
                String output="";
                JSONArray jsonArray=new JSONArray(weatherInfo);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject obj=jsonArray.getJSONObject(i);
                    Log.i("main",obj.getString("main"));
                    Log.i("description",obj.getString("description"));
                    output+=obj.getString("main");
                    output+=" : ";
                    output+=obj.getString("description");
                    output+="\n";
                    //textView.setText(obj.getString("main")+"\n"+obj.getString("description"));
                }
                textView.setText(output);
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Could not find weather!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick(View view){
        try {
            DownloadTask task = new DownloadTask();
            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=b6907d289e10d714a6e88b30761fae22");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Could not find weather!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView2);
        editText=findViewById(R.id.editText);
    }
}
