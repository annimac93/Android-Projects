package com.appart.hp.nearme;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link MapFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link MapFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class MapFragment extends Fragment implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    LocationManager locationManager ;
    boolean GpsStatus;
    String myJSON;

    String userid;


    PlaceAdapter placeadpt;
    private ArrayList<String> place_image = new ArrayList<String>();
    private ArrayList<String> place_name = new ArrayList<String>();
    private ArrayList<String> place_vicinity = new ArrayList<String>();
    private ArrayList<String> place_lat = new ArrayList<String>();
    private ArrayList<String> place_lng = new ArrayList<String>();
    ListView placelist;

    JSONArray results = null;

    JSONArray photos = null;
//    Context context;
    private AlertDialog.Builder build;

//    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(com.appart.hp.menuintegration.R.layout.fragment_map, container, false);

        placelist = (ListView)v.findViewById(com.appart.hp.menuintegration.R.id.listview_place);

        Bundle args = getArguments();

        if (args != null) {
            userid = args.getString("userid");
        }
        //Permission StrictMode for network thread
        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }



        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(com.appart.hp.menuintegration.R.id.map1);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
//               checkLocationPermission();
                // startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getApplicationContext(),"in first if",Toast.LENGTH_LONG).show();
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Toast.makeText(getContext(),"in second if",Toast.LENGTH_LONG).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));


            } else {
//                Toast.makeText(getContext(),"in second else",Toast.LENGTH_LONG).show();
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
            return false;
        }
        else {
//            Toast.makeText(getApplicationContext(),"in first else",Toast.LENGTH_SHORT).show();
            locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            Toast.makeText(getContext(),"gps status :"+String.valueOf(GpsStatus),Toast.LENGTH_SHORT).show();
            if(!GpsStatus)
            {
                build = new AlertDialog.Builder(getActivity());
                build.setMessage("To continue, let your device turn on location using Google's location service.");
                build.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                dialog.cancel();
                            }
                        });

                build.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = build.create();
                alert.show();
            }
            return true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+location.getLatitude()+","+location.getLongitude()+"&radius=500&type=restaurant,bar,cafe&name=restaurant&key=AIzaSyClvmT1pUito9fm7azRO-fgfa8Jyo00qMc";

//        Log.d("$$$$$$url",url);
//        Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
        myJSON = getData(url);
        showList();

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }

    }


    public String getData(String url)
    {
        StringBuilder str=new StringBuilder();
        HttpClient client=new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);


//        Toast.makeText(getContext(),"in getdata",Toast.LENGTH_LONG).show();
        try{

            HttpResponse response=client.execute(httpget);
            StatusLine statusLine=response.getStatusLine();
            int statusCode=statusLine.getStatusCode();

            if(statusCode==200) //status ok
            {
                HttpEntity entity=response.getEntity();
                InputStream content=entity.getContent();
                BufferedReader reader=new BufferedReader(new InputStreamReader(content));
                String line;
                while((line=reader.readLine())!=null)
                {
                    str.append(line);
                }
            }
            else {
                Log.e("Log","Failed to download result...");
                Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }
        catch(ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
//    	Log.d("result",str.toString());
        return str.toString();
    }

    public void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            String status = jsonObj.getString("status");
            if(status.equalsIgnoreCase("OK")) {
                results = jsonObj.getJSONArray("results");
//            Toast.makeText(getContext(),"result : "+myJSON,Toast.LENGTH_LONG).show();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject c = results.getJSONObject(i);

                    String name = c.getString("name");
                    final String vicinity = c.getString("vicinity");

                    JSONObject geo = c.getJSONObject("geometry");

                    JSONObject location = geo.getJSONObject("location");

                    Double lat = Double.valueOf(location.getString("lat"));

                    Double lng = Double.valueOf(location.getString("lng"));

                    photos = c.getJSONArray("photos");
                    for (int j = 0; j < photos.length(); j++) {
                        JSONObject d = photos.getJSONObject(j);
                        String photores = d.getString("photo_reference");

                        place_image.add(photores);

                    }

                    place_name.add(name);
                    place_vicinity.add(vicinity);
                    place_lat.add(String.valueOf(lat));
                    place_lng.add(String.valueOf(lng));
//                Toast.makeText(getContext(),String.valueOf(place_name) ,Toast.LENGTH_LONG).show();
//                Toast.makeText(getContext(),lat + " ,"+lng,Toast.LENGTH_LONG).show();

                    //setting marker
                    LatLng latLng = new LatLng(lat, lng);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(name);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mCurrLocationMarker = mMap.addMarker(markerOptions);

                    placeadpt = new PlaceAdapter(getActivity(), place_image, place_name,"map");
                    placelist.setAdapter(placeadpt);
                    placelist.setScrollingCacheEnabled(false);


                    placelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Bundle arguments = new Bundle();
                        PlaceFragment placeFragment = new PlaceFragment();

                            arguments.putString( "place_name",place_name.get(position));
                            arguments.putString( "place_image",place_image.get(position));
                            arguments.putString("place_vicinity",place_vicinity.get(position));
                            arguments.putString("place_lat",place_lat.get(position));
                            arguments.putString("place_lng",place_lng.get(position));
                            arguments.putString("userid",userid);
                            placeFragment.setArguments(arguments);
                            FragmentManager manager = getFragmentManager();
                        manager.beginTransaction().replace(com.appart.hp.menuintegration.R.id.content_menu,placeFragment,placeFragment.getTag()).addToBackStack(placeFragment.getTag()).commit();


                        }
                    });
                }
            }
            else
            {
                Toast.makeText(getContext(),"No Internet Connection" ,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
//        Toast.makeText(getContext(),"req code :"+String.valueOf(requestCode),Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),"In first if",Toast.LENGTH_SHORT).show();
                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {


                        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

                        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                        Toast.makeText(getContext(),"gps status :"+String.valueOf(GpsStatus),Toast.LENGTH_SHORT).show();
                        if(!GpsStatus)
                        {
                            build = new AlertDialog.Builder(getActivity());
                            build.setMessage("To continue, let your device turn on location using Google's location service.");
                            build.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                            dialog.cancel();
                                        }
                                    });

                            build.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int which) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = build.create();
                            alert.show();
                        }


                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        if (context instanceof OnFragmentInteractionListener) {
////            mListener = (OnFragmentInteractionListener) context;
////        } else {
////            throw new RuntimeException(context.toString()
////                    + " must implement OnFragmentInteractionListener");
////        }
//        try{
//            mListener = (OnFragmentInteractionListener) context;
//        }catch (Exception e){}
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void setData(String placename, String placeImage );
//    }
}
