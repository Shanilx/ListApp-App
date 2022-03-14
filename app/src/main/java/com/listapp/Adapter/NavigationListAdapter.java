package com.listapp.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.listapp.R;
import java.util.HashMap;
import java.util.List;

public class NavigationListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private static List<String> _listDataHeader;
    private static HashMap<String, List<String>> _listDataChild;


    public NavigationListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        _listDataHeader = listDataHeader;
        _listDataChild = listChildData;
    }

    @Override
    public String getChild(int groupPosition, int childPosititon) {
        return _listDataChild.get(_listDataHeader.get(groupPosition))
                .get(childPosititon);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_navigation_child, null);
        }

        convertView.setPadding(0, 20, 0, 20);

        TextView childTV = convertView
                .findViewById(R.id.childText);
        childTV.setText(childText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition==0)
        return _listDataChild.get(_listDataHeader.get(groupPosition))
                .size();


        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }



    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_navigation_parent, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.row_text);
        ImageView imageView = convertView.findViewById(R.id.row_image);
        lblListHeader.setText(headerTitle);

        switch (groupPosition)
        {
            case 0:
                imageView.setImageResource(R.drawable.search_white);
                break;

            case 1:
                imageView.setImageResource(R.drawable.location_white);

                break;

            case 2:
                imageView.setImageResource(R.drawable.user_white);
                break;

            case 3:
                imageView.setImageResource(R.drawable.calculator);
                break;

            case 4:
                imageView.setImageResource(R.drawable.registration);
                break;

            case 5:
                imageView.setImageResource(R.drawable.about_us);
                break;

            case 6:
                imageView.setImageResource(R.drawable.notification_white);
                break;

            case 7:
                imageView.setImageResource(R.drawable.contact_us);
                imageView.setPadding(0,12,0,12);
                break;

            case 8:
                imageView.setImageResource(R.drawable.logout_white);
                break;
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void changeCity(String city){

    }

}