package woodnsoft.bsHandax.DataListView.Insp_Chek;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class Insp_Chek_ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Insp_Chek_Item> mItems = new ArrayList<Insp_Chek_Item>();
    public Insp_Chek_ListAdapter(Context context) {
        mContext = context;
    }
 
    public void clear() {
        mItems.clear();
    }
     
    public void addItem(Insp_Chek_Item it) {
        mItems.add(it);
    }
 
    public void setListItems(List<Insp_Chek_Item> lit) {
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
        Insp_Chek_View itemView;
        if (convertView == null) {
            itemView = new Insp_Chek_View(mContext, mItems.get(position));
        } else {
            itemView = (Insp_Chek_View) convertView;
             
            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));
            itemView.setText(2, mItems.get(position).getData(2));
        }
        return itemView;
    }	
	
	
	
}