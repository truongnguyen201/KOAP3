package topica.edu.vn.koap3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import topica.edu.vn.config.Configuration;
import topica.edu.vn.model.Contact;

public class MainActivity extends AppCompatActivity {
    Button btnLay;

    ProgressDialog progressDialog;
     ListView lvContact;
     ArrayList<Contact>dsContatct;
     ArrayAdapter adapterContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addControls() {

        btnLay.findViewById(R.id.btnLay);
        lvContact=findViewById(R.id.lvContact);
        dsContatct=new ArrayList<>();
        adapterContact=new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,dsContatct);

        lvContact.setAdapter(adapterContact);


        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Thông Báo");
        progressDialog.setMessage("Đang tải danh sách Contact,vui lòng chờ.....");
        progressDialog.setCanceledOnTouchOutside(false);

    }

    private void addEvents() {
        btnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLiLayDanhSach();

            }
        });

    }

    private void xuLiLayDanhSach() {
        ListContatctTask task=new ListContatctTask();
        task.execute();
    }

    class  ListContatctTask extends AsyncTask<Void,Void,ArrayList<Contact>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapterContact.clear();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {
            super.onPostExecute(contacts);
            adapterContact.clear();
            adapterContact.addAll(contacts);
            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Contact> doInBackground(Void... voids) {
            ArrayList<Contact> ds=new ArrayList<Contact>();
            try {
                SoapObject request= new SoapObject(Configuration.NAME_SPACE,Configuration.METHOD_GET_5_CONTACT);
                SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE =new HttpTransportSE(Configuration.SERVER_URL);
                httpTransportSE.call(Configuration.SOAP_ACTION_GETDETAIL,envelope);

                SoapObject data= (SoapObject) envelope.getResponse();

                for (int i=0;i<data.getPropertyCount();i++)
                {
                    SoapObject object= (SoapObject) data.getProperty(i);
                    Contact contact=new Contact();
                    if(object.hasProperty("Ma"))
                    {
                        contact.setMa(Integer.parseInt(object.getPropertyAsString("Ma")));
                    }
                    if(object.hasProperty("Ten"))
                    {
                        contact.setTen(object.getPropertyAsString("Ten"));

                    }
                    if(object.hasProperty("Phone"))
                    {
                        contact.setTen(object.getPropertyAsString("Phone"));

                    }
                    ds.add(contact);
                }
            }
            catch (Exception ex)
            {
                Log.e("Loi",ex.toString());
            }
            return null;
        }
    }
}