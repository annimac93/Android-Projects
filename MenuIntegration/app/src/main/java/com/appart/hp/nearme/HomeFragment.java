package com.appart.hp.nearme;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link HomeFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link HomeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String userid,placename,placeimg,placeid;
    byte[] imgbyte = null;

    ArrayList<String> alplacename = new ArrayList<String>();
    ArrayList<String> alplaceid = new ArrayList<String>();
    ArrayList<byte[]> alplacephoto = new ArrayList<byte[]>();



    TextView nodata;
    ListView fav_place_list;
    DBO obj;

    FavAdapter favadapter;
    private AlertDialog.Builder build;

//    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(com.appart.hp.menuintegration.R.layout.fragment_home, container, false);
        Bundle args = getArguments();

        if (args != null) {
            userid = args.getString("userid");
        }

        nodata = (TextView)v.findViewById(com.appart.hp.menuintegration.R.id.textView_nodata);
        fav_place_list = (ListView)v.findViewById(com.appart.hp.menuintegration.R.id.listview_favplace);

        obj=new DBO(getActivity(), null, null, 1);

        nodata.setVisibility(View.VISIBLE);
        fav_place_list.setVisibility(View.GONE);
        displayData();

        if(fav_place_list.getVisibility() == View.VISIBLE)
        {
            fav_place_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle arguments = new Bundle();
                    FavFragment favFragment = new FavFragment();

                    arguments.putString( "placeid",alplaceid.get(position));
                    arguments.putString("userid",userid);

//                    Toast.makeText(getActivity(),alplaceid.get(position),Toast.LENGTH_LONG).show();
                    favFragment.setArguments(arguments);
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(com.appart.hp.menuintegration.R.id.content_menu,favFragment,favFragment.getTag()).addToBackStack(favFragment.getTag()).commit();

                }
            });

            fav_place_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               final int arg2, long arg3) {

                    build = new AlertDialog.Builder(getActivity());
                    build.setTitle("Delete " + alplacename.get(arg2));
                    build.setMessage("Do you want to delete ?");
                    build.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    Toast.makeText(
                                            getActivity(),
                                            alplacename.get(arg2)
                                                    + " is deleted.", Toast.LENGTH_SHORT).show();

                                    obj.deleteplace(Integer.valueOf(alplaceid.get(arg2)));
//                                fav_place_list.removeAllViews();
//                                displayData();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//                                favadapter.notifyDataSetChanged();

//                                Bundle arguments = new Bundle();
//                                HomeFragment homeFragment = new HomeFragment();
//                                arguments.putString("userid",userid);
//                                homeFragment.setArguments(arguments);
//                                FragmentManager manager = getFragmentManager();
//                                manager.beginTransaction().replace(R.id.content_menu,homeFragment,homeFragment.getTag()).commit();

                                    dialog.cancel();
                                }
                            });

                    build.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = build.create();
                    alert.show();

                    return true;
                }
            });
        }

        return v;
    }

    private void displayData() {

        Cursor mCursor = obj.getbyuserid(Integer.parseInt(userid));


        if (mCursor.moveToFirst()) {
            nodata.setVisibility(View.GONE);
            fav_place_list.setVisibility(View.VISIBLE);
            do {
                placeid = mCursor.getString(0);
                placename = mCursor.getString(1);
                imgbyte = mCursor.getBlob(2);


                alplaceid.add(placeid);
                alplacename.add(placename);
                alplacephoto.add(imgbyte);

            } while (mCursor.moveToNext());

            favadapter = new FavAdapter(getActivity(),alplacephoto,alplacename);
//            favadapter.notifyDataSetChanged();
            fav_place_list.setAdapter(favadapter);
            fav_place_list.setScrollingCacheEnabled(false);



        }
        else
        {
            nodata.setVisibility(View.VISIBLE);
            fav_place_list.setVisibility(View.GONE);
        }


        mCursor.close();
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
}
