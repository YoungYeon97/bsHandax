package woodnsoft.bsHandax;

import woodnsoft.bsHandax.DataListView.Dialog_Jata.Dialog_Jata_ListAdapter;
import woodnsoft.bsHandax.DataListView.Dialog_Jata.Dialog_Jata_ListView;
import woodnsoft.bsHandax.DataListView.Dialog_Jata.Dialog_Jata_Item;
import woodnsoft.bsHandax.DataListView.Dialog_Jata.OnDataSelectionListener_Dialog_Jata;
import woodnsoft.bsHandax2.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public final class Dialog_Jata extends Dialog {
	private Context mContext;
	
	//private String TAG = "Dialog_Jata"; 
	Dialog_Jata_ListView listView;
	Dialog_Jata_ListAdapter adapter;
	
	//Cursor curSelect;
	
	public String is_jata_flag, is_jata_flag_name;
	
	private OnDismissListener _listener;
	private OnCancelListener _cancle;

	public Dialog_Jata(Context context) {  
      super(context);
      mContext = context;
      // TODO Auto-generated constructor stub  
	}  	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_jata);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		//--->>>리스트뷰--->>>
		adapter = new Dialog_Jata_ListAdapter(mContext);
		listView = new Dialog_Jata_ListView(mContext);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayoutDrugList );
		linLayout.addView(listView);
		
		Button lb_cancel = (Button) findViewById(R.id.b_cancel);
		lb_cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if( _cancle == null ) {} else {  
					_cancle.onCancel( Dialog_Jata.this ) ;
				}
				
				cancel();
			}

		});

		listView.setOnDataSelectionListener( new OnDataSelectionListener_Dialog_Jata () {
         
   		public void onDataSelected(AdapterView<?> parent, View v, int position, long id) {
   			// make intent
   			
				if( _listener == null ) {} else {
					
					Dialog_Jata_Item selectItem = (Dialog_Jata_Item)adapter.getItem(position);
	   			
	   			is_jata_flag = selectItem.getData(0);       //자타구분코드
	   			is_jata_flag_name = selectItem.getData(1);  //자타구분명

					
					_listener.onDismiss( Dialog_Jata.this ) ;  
				}
				
				dismiss() ;
                
   		}
		});       		

		fGetList();
	};
	
	

	@Override
	public void setOnCancelListener(OnCancelListener $listener) {
		// TODO Auto-generated method stub
		_cancle = $listener ; 
	}

	public void setOnDismissListener( OnDismissListener $listener ) {  
      _listener = $listener ; 
  }
	
	public void fGetList() {
		try
		{
			adapter.clear();

			adapter.addItem(new Dialog_Jata_Item("0", "타사"));
			adapter.addItem(new Dialog_Jata_Item("1", "자사"));
			
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();  //변경사항을 refalsh
		} finally {
 			//if ( pd_bef != null ) pd_bef.hide();
		}
		
	}
   
   
}