package woodnsoft.bsHandax.DataListView.DataGet_Insp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DataGet_Insp_ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<DataGet_Insp_Item> mItems = new ArrayList<DataGet_Insp_Item>();
    public DataGet_Insp_ListAdapter(Context context) {
        mContext = context;
    }
 
    public void clear() {
        mItems.clear();
    }
     
    public void addItem(DataGet_Insp_Item it) {
        mItems.add(it);
    }
 
    public void setListItems(List<DataGet_Insp_Item> lit) {
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
        DataGet_Insp_View itemView;
        if (convertView == null) {
            itemView = new DataGet_Insp_View(mContext, mItems.get(position));
        } else {
            itemView = (DataGet_Insp_View) convertView;
             
            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));
            itemView.setText(2, mItems.get(position).getData(2));
            itemView.setText(3, mItems.get(position).getData(3));
        }
        return itemView;
    }	
	
 	public void remove(int i) {
		// TODO Auto-generated method stub
		if (mItems.size() - 1 >= i)	mItems.remove(i);
	}	
	
	
	
}