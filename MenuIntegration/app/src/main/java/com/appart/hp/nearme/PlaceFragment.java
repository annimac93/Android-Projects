package com.appart.hp.nearme;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.apache.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link PlaceFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link PlaceFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class PlaceFragment extends Fragment implements OnMapReadyCallback {
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
    Button add_fav;
    GoogleMap mMap;
    String userid;
    Marker mCurrLocationMarker;
    DBO obj;
    int flag = 0;

//    private OnFragmentInteractionListener mListener;

    public PlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public  PlaceFragment newInstance(String param1, String param2) {
        PlaceFragment fragment = new PlaceFragment();
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
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(com.appart.hp.menuintegration.R.id.map2);
        mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(com.appart.hp.menuintegration.R.layout.fragment_place, container, false);

        img_place = (ImageView)v.findViewById(com.appart.hp.menuintegration.R.id.image_place);
        txt_name = (TextView)v.findViewById(com.appart.hp.menuintegration.R.id.text_placename);
        txt_address = (TextView) v.findViewById(com.appart.hp.menuintegration.R.id.text_placeadd);
        add_fav = (Button) v.findViewById(com.appart.hp.menuintegration.R.id.btn_addtofav);

        obj = new DBO(getActivity(),null,null,1);
        Bundle args = getArguments();

        if (args != null) {
            place_name = args.getString("place_name");
            place_image_ref = args.getString("place_image");
            place_vicinity = args.getString("place_vicinity");
            place_lat = args.getString("place_lat");
            place_lng = args.getString("place_lng");
            userid = args.getString("userid");
            String photo_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference="+ place_image_ref +"&key=AIzaSyClvmT1pUito9fm7azRO-fgfa8Jyo00qMc";

//            Toast.makeText(getActivity(),mParam1 + " , " + mParam2,Toast.LENGTH_SHORT).show();
            if (img_place != null) {
                new ImageTask(img_place).execute(photo_url);
            }
            txt_name.setText(place_name);
            txt_address.setText(place_vicinity);

        }
        else
        {
            Toast.makeText(getActivity(), "Data not fetched" ,Toast.LENGTH_SHORT).show();
        }

        add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata();
            }
        });



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


    void savedata()
    {
        Cursor mCursor = obj.getallbyuserid(Integer.parseInt(userid));


        if (mCursor.moveToFirst()) {

            do {
                if(place_name.equalsIgnoreCase(mCursor.getString(2)) && place_vicinity.equalsIgnoreCase(mCursor.getString(3)))
                {
                    flag = 1;
                    break;
                }
                else
                {
                    flag = 0;
                    break;
                }

            } while (mCursor.moveToNext());


        }
        else
        {
//            Toast.makeText(getActivity(),"in else",Toast.LENGTH_SHORT).show();

        }
            if(flag == 0)
            {
                if(obj.addPlace(Integer.parseInt(userid),place_name,place_vicinity,place_lat,place_lng,imgplace))
                {
                    Toast.makeText(getActivity(),"Place Added to your Favourite list.",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getActivity(),"Insertion Failed ",Toast.LENGTH_SHORT).show();
                }
            }
        else if(flag == 1)
            {
                Toast.makeText(getActivity(),"Place already added ",Toast.LENGTH_SHORT).show();
            }

    }


    class ImageTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpStatus.SC_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);




//                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//                    imgplace = bytes.toByteArray();
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    imgplace = bytes.toByteArray();
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(com.appart.hp.menuintegration.R.drawable.no_bg);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }

//        public void getData(String placeName , String placeImage)
//        {
//            Toast.makeText(getActivity(),placeName + " , " + placeImage,Toast.LENGTH_SHORT).show();
//        }
    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }


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
//        void onFragmentInteraction(Uri uri);
//    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpStatus.SC_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(com.appart.hp.menuintegration.R.drawable.no_bg);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }
}
