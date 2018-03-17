package com.appart.hp.nearme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by HP on 7/28/2017.
 */

public class FavAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<byte[]> place_img;
    private ArrayList<String> place_name;

    byte[] img;
    Bitmap bm;


    public FavAdapter(Context c, ArrayList<byte[]> place_image,ArrayList<String> place_name)
    {
        this.mContext = c;
        this.place_img = place_image;
        this.place_name = place_name;

//        Toast.makeText(c,String.valueOf(place_name),Toast.LENGTH_SHORT).show();

        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    @Override
    public int getCount() {
        return place_name.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder mHolder;
        LayoutInflater layoutInflater;

        if(convertView == null)
        {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(com.appart.hp.menuintegration.R.layout.listcell, null);
            //final Organization currentOrg = organizationlist.get(pos).getOrganization();
            mHolder = new Holder();

            mHolder.image_place = (ImageView) convertView.findViewById(com.appart.hp.menuintegration.R.id.imageView_place);
            mHolder.text_placename = (TextView) convertView.findViewById(com.appart.hp.menuintegration.R.id.textView_placename);

//            mHolder.image_place.setImageResource(R.drawable.no_bg);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (Holder)convertView.getTag();
        }

//            Toast.makeText(mContext,"inhome",Toast.LENGTH_SHORT).show();
//            img = place_img.get(position);
//            Bitmap bm;

                img = place_img.get(position);
                bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                mHolder.image_place.setImageBitmap(bm);

//                mHolder.image_place.setImageBitmap(Bitmap.createScaledBitmap(bitmap, mHolder.image_place.getWidth(),
//                        mHolder.image_place.getHeight(), false));

//

        mHolder.text_placename.setText(place_name.get(position));



        return convertView;
    }

    public class Holder{

        ImageView image_place;
        TextView text_placename;

    }
}
