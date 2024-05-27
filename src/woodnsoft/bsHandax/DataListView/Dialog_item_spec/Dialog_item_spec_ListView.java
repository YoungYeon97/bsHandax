package woodnsoft.bsHandax.DataListView.Dialog_item_spec;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
 
public class Dialog_item_spec_ListView extends ListView {
 
	/**
     * DataAdapter for this instance
     */
     
    /**
     * Listener for data selection
     */
    private OnDataSelectionListener_Dialog_item_spec selectionListener;
     
    public Dialog_item_spec_ListView(Context context) {
        super(context);
 
        init();
    }
 
	/**
     * set initial properties
     */
    private void init() {
        // set OnItemClickListener for processing OnDataSelectionListener
        setOnItemClickListener(new OnItemClickAdapter());
    }
 
    /**
     * set DataAdapter
     * 
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);
 
    }
 
    /**
     * get DataAdapter
     * 
     * @return
     */
    public BaseAdapter getAdapter() {
        return (BaseAdapter)super.getAdapter();
    }
     
    /**
     * set OnDataSelectionListener
     * 
     * @param onDataSelectionListener
     */
    public void setOnDataSelectionListener(OnDataSelectionListener_Dialog_item_spec listener) {
       this.selectionListener = listener;
   }
    
    /**
     * get OnDataSelectionListener
     * 
     * @return
     */
    public OnDataSelectionListener_Dialog_item_spec getOnDataSelectionListener() {
        return selectionListener;
    }
     
    class OnItemClickAdapter implements OnItemClickListener {
         
        public OnItemClickAdapter() {
             
        }
 
        public void onItemClick(AdapterView parent, View v, int position, long id) {
             
            if (selectionListener == null) {
                return;
            }
             
            // get row and column
            int rowIndex = -1;
            int columnIndex = -1;
             
            // call the OnDataSelectionListener method
            selectionListener.onDataSelected(parent, v, position, id);
             
        }
         
    }
     
}