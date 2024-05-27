package woodnsoft.bsHandax.DataListView.DataGet;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DataGetListAdapter extends BaseAdapter {

    private Context mContext;
    private List<DataGetItem> mItems = new ArrayList<DataGetItem>();
    public DataGetListAdapter(Context context) {
        mContext = context;
    }
 
    public void clear() {
        mItems.clear();
    }
     
    public void addItem(DataGetItem it) {
        mItems.add(it);
    }
 
    public void setListItems(List<DataGetItem> lit) {
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
        DataGetView itemView;
        if (convertView == null) {
            itemView = new DataGetView(mContext, mItems.get(position));
        } else {
            itemView = (DataGetView) convertView;
             
            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));
            itemView.setText(2, mItems.get(position).getData(2));
            itemView.setText(3, mItems.get(position).getData(3));
        }
        return itemView;
    }	
	
	
	
}