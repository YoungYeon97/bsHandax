package woodnsoft.bsHandax.DataListView.DataPut;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DataPut_ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<DataPut_Item> mItems = new ArrayList<DataPut_Item>();
    public DataPut_ListAdapter(Context context) {
        mContext = context;
    }
 
    public void clear() {
        mItems.clear();
    }
     
    public void addItem(DataPut_Item it) {
        mItems.add(it);
    }
 
    public void setListItems(List<DataPut_Item> lit) {
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
        DataPut_View itemView;
        if (convertView == null) {
            itemView = new DataPut_View(mContext, mItems.get(position));
        } else {
            itemView = (DataPut_View) convertView;
             
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
	
 	public void remove(int i) {
		// TODO Auto-generated method stub
		if (mItems.size() - 1 >= i)	mItems.remove(i);
	}	
	
	
	
}