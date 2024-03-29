package com.atta.oncs.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.atta.oncs.R;
import com.atta.oncs.contracts.NewAddressContract;
import com.atta.oncs.model.Address;
import com.atta.oncs.model.SessionManager;
import com.atta.oncs.presenter.NewAddressPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewAddressActivity extends AppCompatActivity implements NewAddressContract.View, OnMapReadyCallback, View.OnClickListener {

    String district, street, lat, lon, fullAddress;

    float latitude, longitude;

    EditText floorEditText, apartmentNumberEditText, buildingNumberEditText, areaEditText, addressNameEditText, streetEditText, landMarkEditText;

    Button saveBtn;

    private GoogleMap mMap;

    NewAddressPresenter newAddressPresenter;

    boolean addEdie;

    int id;

    Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        initiateViews();

        if (getIntent().getSerializableExtra("address") == null) {

            addEdie = true;
            Intent intent = new Intent(NewAddressActivity.this, MapsActivity.class);
            startActivityForResult(intent, 1);

        }else {
            addEdie = false;
            address = (Address) getIntent().getSerializableExtra("address");

            setData();
        }


        newAddressPresenter = new NewAddressPresenter(this, this);
    }

    private void setData() {

        areaEditText.setText(address.getArea());
        addressNameEditText.setText(address.getAddressName());
        streetEditText.setText(address.getStreet());
        buildingNumberEditText.setText(address.getBuildingNumber());
        floorEditText.setText(address.getFloor());
        apartmentNumberEditText.setText(address.getApartmentNumber());
        landMarkEditText.setText(address.getLandMark());

        fullAddress = address.getFullAddress();
        latitude = address.getLatitude();
        longitude = address.getLongitude();
        id = address.getId();

        saveBtn.setText("Update");
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initiateViews(){
        floorEditText = findViewById(R.id.ed_floor);
        apartmentNumberEditText = findViewById(R.id.ed_apartment);
        buildingNumberEditText = findViewById(R.id.ed_building);
        areaEditText = findViewById(R.id.ed_area);
        addressNameEditText = findViewById(R.id.ed_name);
        streetEditText = findViewById(R.id.ed_street);
        landMarkEditText = findViewById(R.id.ed_landmark);
        saveBtn = findViewById(R.id.save_add);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void setStreet(String street) {

        streetEditText.setText(street);
    }

    @Override
    public void setArea(String area) {

        areaEditText.setText(area);
    }

    @Override
    public void setFullAddress(String formattedAddress) {

        fullAddress = formattedAddress;
    }

    @Override
    public void setBuildingNumber(String buildingNumber) {

        buildingNumberEditText.setText(buildingNumber);
    }

    @Override
    public void setCity(String cityName) {

        switch (cityName){
            case "Cairo Governorate":
            case "Cairo":
            case "Giza Governorate":
            case "Giza":

                Toast.makeText(NewAddressActivity.this, "برجاء ادخال باقي تفاصيل العنوان", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(NewAddressActivity.this, "برجاء اختيار عنوان داخل القاهرة و الجيزة فقط", Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void moveToAddressesActivity() {
        Intent intent = new Intent(NewAddressActivity.this, AddressesActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                if (!data.getBooleanExtra("error", false)){
                    district = data.getStringExtra("district");
                    street = data.getStringExtra("street");
                    lat = data.getStringExtra("latitude");
                    latitude = Float.parseFloat(lat);
                    lon = data.getStringExtra("longitude");
                    longitude = Float.parseFloat(lon);
                    //addressText.setText(data.getStringExtra("address"));

                    streetEditText.setText(street);
                    areaEditText.setText(district);

                    if (lat != null & lon != null ){
                        LatLng myLatLng = new LatLng(latitude, longitude);

                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(myLatLng).title(""));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 16.0f));
                    }


                    String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + data.getStringExtra("latitude") + "," +
                            data.getStringExtra("longitude") + "&key=AIzaSyCGtXN8YmSZSRqzgXkUhIobn1TTFmOC7HU";

                    newAddressPresenter.getAddress(url);
                }else {

                    String errorMessage = data.getStringExtra("error_message");

                    String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + data.getStringExtra("latitude") + "," +
                            data.getStringExtra("longitude") + "&key=AIzaSyCGtXN8YmSZSRqzgXkUhIobn1TTFmOC7HU";

                    newAddressPresenter.getAddress(url);

                    //Toast.makeText(NewAddressActivity.this, errorMessage + ", fill your data manually", Toast.LENGTH_LONG).show();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(NewAddressActivity.this, "fill your data manually", Toast.LENGTH_LONG).show();


                LatLng myLatLng = new LatLng(29.960426, 31.257656);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 16.0f));

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!addEdie){

            getMenuInflater().inflate(R.menu.address, menu);
        }
        return !addEdie;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete) {

            if (!addEdie){

                newAddressPresenter.deleteAddress(address.getId());
            }

            return !addEdie;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnMapClickListener(latLng -> {
            Intent intent1 = new Intent(NewAddressActivity.this, MapsActivity.class);
            if (lat != null & lon != null ){
                intent1.putExtra("location_latitude", latitude);
                intent1.putExtra("location_longitude", longitude);
            }
            startActivityForResult(intent1,1);
        });


        if (lat != null & lon != null ){
            LatLng myLatLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(myLatLng).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        }else {

            LatLng myLatLng = new LatLng(29.960426, 31.257656);
            mMap.addMarker(new MarkerOptions().position(myLatLng).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        }
    }

    @Override
    public void onClick(View v) {

        if (v == saveBtn){

            int userId = SessionManager.getInstance(this).getUserId();
            if (addEdie) {

                Address address = new Address(userId, floorEditText.getText().toString(), apartmentNumberEditText.getText().toString(), buildingNumberEditText.getText().toString(),
                        areaEditText.getText().toString(), addressNameEditText.getText().toString(), fullAddress, streetEditText.getText().toString(),
                        landMarkEditText.getText().toString(), latitude, longitude);
                newAddressPresenter.addAddress(address);
            }else {

                Address address = new Address(id, userId, floorEditText.getText().toString(), apartmentNumberEditText.getText().toString(), buildingNumberEditText.getText().toString(),
                        areaEditText.getText().toString(), addressNameEditText.getText().toString(), fullAddress, streetEditText.getText().toString(),
                        landMarkEditText.getText().toString(), latitude, longitude);
                newAddressPresenter.editAddress(address);
            }
        }
    }
}
