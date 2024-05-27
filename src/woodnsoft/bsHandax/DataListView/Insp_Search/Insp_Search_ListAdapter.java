package woodnsoft.bsHandax.DataListView.Insp_Search;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class Insp_Search_ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Insp_Search_Item> mItems = new ArrayList<Insp_Search_Item>();
    public Insp_Search_ListAdapter(Context context) {
        mContext = context;
    }
 
    public void clear() {
        mItems.clear();
    }
     
    public void addItem(Insp_Search_Item it) {
        mItems.add(it);
    }
 
    public void setListItems(List<Insp_Search_Item> lit) {
        mItems = lit;
    }
 
    public int getCount() {
        return mItems.size();
    }
 
    public Object getItem(int position) {
        return mItems.get(position);
    }
 
    public boolean areAllItemsSelectable() {
        return false;
    }
    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Insp_Search_View itemView;
        if (convertView == null) {
            itemView = new Insp_Search_View(mContext, mItems.get(position));
        } else {
            itemView = (Insp_Search_View) convertView;
             
            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));
            itemView.setText(2, mItems.get(position).getData(2));
            itemView.setText(3, mItems.get(position).getData(3));
            itemView.setText(4, mItems.get(position).getData(4));
            itemView.setText(5, mItems.get(position).getData(5));
            itemView.setText(6, mItems.get(position).getData(6));
            itemView.setText(7, mItems.get(position).getData(7));
            itemView.setText(8, mItems.get(position).getData(8));
            itemView.setText(9, mItems.get(position).getData(9));
            itemView.setText(10, mItems.get(position).getData(10));
        }
        return itemView;
    }	
	
	
	
}