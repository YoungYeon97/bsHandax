package woodnsoft.bsHandax.DataListView.DataGet_Insp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
 
public class DataGet_Insp_ListView extends ListView {
 
    /**
     * DataAdapter for this instance
     */
    private DataGet_Insp_ListAdapter adapter;
     
    /**
     * Listener for data selection
     */
    private OnDataSelectionListener_DataGet_Insp selectionListener;
     
    public DataGet_Insp_ListView(Context context) {
        super(context);
 
        init();
    }
 
    public DataGet_Insp_ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
 
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
    public void setOnDataSelectionListener(OnDataSelectionListener_DataGet_Insp listener) {
       this.selectionListener = listener;
   }

    /**
     * get OnDataSelectionListener
     * 
     * @return
     */
    public OnDataSelectionListener_DataGet_Insp getOnDataSelectionListener() {
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