package com.appart.hp.nearme;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavFragment extends Fragment  implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String place_name, place_image_ref, place_vicinity, place_lat, place_lng;
    byte[] imgplace;

    ImageView img_place;
    TextView txt_name , txt_address;

    GoogleMap mMap;
    String userid,placeid;
    Marker mCurrLocationMarker;
    DBO obj;
    int flag = 0;

//    private OnFragmentInteractionListener mListener;

    public FavFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavFragment newInstance(String param1, String param2) {
        FavFragment fragment = new FavFragment();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(com.appart.hp.menuintegration.R.id.map3);
        mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(com.appart.hp.menuintegration.R.layout.fragment_fav, container, false);
        img_place = (ImageView)v.findViewById(com.appart.hp.menuintegration.R.id.image_favplace);
        txt_name = (TextView)v.findViewById(com.appart.hp.menuintegration.R.id.text_favname);
        txt_address = (TextView) v.findViewById(com.appart.hp.menuintegration.R.id.text_favadd);


        obj = new DBO(getActivity(),null,null,1);
        Bundle args = getArguments();

        if (args != null) {
            placeid = args.getString("placeid");
            userid = args.getString("userid");

            Cursor mCursor = obj.getbyuser_place(Integer.parseInt(placeid) , Integer.parseInt(userid));


            if (mCursor.moveToFirst()) {

                do {
                 place_name = mCursor.getString(2);
                    place_vicinity = mCursor.getString(3);
                    place_lat = mCursor.getString(4);
                    place_lng = mCursor.getString(5);
                    imgplace = mCursor.getBlob(6);

                    txt_name.setText(place_name);
                    txt_address.setText(place_vicinity);

                    Bitmap bm;
//                Toast.makeText(getActivity(),imgbyte.toString(),Toast.LENGTH_SHORT).show();
                    bm = BitmapFactory.decodeByteArray(imgplace, 0, imgplace.length);
                    img_place.setImageBitmap(bm);
                } while (mCursor.moveToNext());


            }
        }
        else
        {
            Toast.makeText(getActivity(), "Data not fetched" ,Toast.LENGTH_SHORT).show();
        }





        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        // Add a marker in Sydney and move the camera

        LatLng place = new LatLng(Double.valueOf(place_lat), Double.valueOf(place_lng));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place);
        markerOptions.title(place_name);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
