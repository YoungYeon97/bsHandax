package woodnsoft.bsHandax;

import woodnsoft.bsHandax.DataListView.Dialog_bar_la.Dialog_bar_la_ListAdapter;
import woodnsoft.bsHandax.DataListView.Dialog_bar_la.Dialog_bar_la_ListView;
import woodnsoft.bsHandax.DataListView.Dialog_bar_la.Dialog_bar_la_Item;
import woodnsoft.bsHandax.DataListView.Dialog_bar_la.OnDataSelectionListener_Dialog_bar_la;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public final class Dialog_bar_la extends Dialog {
	private Context mContext;
	
	//private String TAG = "Dialog_bar_la"; 
	Dialog_bar_la_ListView listView;
	Dialog_bar_la_ListAdapter adapter;
	
	Cursor curSelect;
	
	EditText iet_filter;
	public String is_bar_la_code, is_bar_la_name;
	
	private OnDismissListener _listener;
	private OnCancelListener _cancle;

	public Dialog_bar_la(Context context) {  
      super(context);
      mContext = context;
      // TODO Auto-generated constructor stub  
	}  	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_bar_la);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		iet_filter = (EditText) findViewById(R.id.et_filter);

		//--->>>리스트뷰--->>>
		adapter = new Dialog_bar_la_ListAdapter(mContext);
		listView = new Dialog_bar_la_ListView(mContext);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayoutDrugList );
		linLayout.addView(listView);
		
		
		
		Button lb_cancel = (Button) findViewById(R.id.b_cancel);
		lb_cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if( _cancle == null ) {} else {  
					_cancle.onCancel( Dialog_bar_la.this ) ;
				}
				
				cancel();
			}

		});

		findViewById(R.id.b_null).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if( _listener == null ) {} else {  
					is_bar_la_code = "";
					is_bar_la_name = "";
					_listener.onDismiss( Dialog_bar_la.this ) ;  
				}
				
				dismiss() ;
			}
		});
		
		listView.setOnDataSelectionListener( new OnDataSelectionListener_Dialog_bar_la () {
         
   		public void onDataSelected(AdapterView<?> parent, View v, int position, long id) {
   			// make intent
   			
				if( _listener == null ) {} else {
					
					Dialog_bar_la_Item selectItem = (Dialog_bar_la_Item)adapter.getItem(position);
	   			
	   			is_bar_la_code = selectItem.getData(0);  //제품구분
	   			is_bar_la_name = selectItem.getData(1);  //제품구분명

					
					_listener.onDismiss( Dialog_bar_la.this ) ;  
				}
				
				dismiss() ;
                
   		}
		});       		

		iet_filter.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				String ls_bar_la_code, ls_bar_la_name, ls_upper_bar_la_name;
				String ls_upper_s = s.toString().toUpperCase();
				
				int li_count = curSelect.getCount();
				
				
				adapter.clear();
				curSelect.moveToFirst();
				
				for (int i=0;i<li_count;i++){
					if (i > 0) curSelect.moveToNext();
					ls_bar_la_code = curSelect.getString(curSelect.getColumnIndex("BAR_LA_CODE"));
					ls_bar_la_name = curSelect.getString(curSelect.getColumnIndex("BAR_LA_NAME"));
					ls_upper_bar_la_name = ls_bar_la_name.toUpperCase();
					
					//Log.i("TAG", s.toString());
					if (ls_upper_bar_la_name.indexOf(ls_upper_s) < 0) continue;
					
					
					//Log.i("TAG", "ls_bar_la_name:" + ls_bar_la_name);
					adapter.addItem(new Dialog_bar_la_Item(ls_bar_la_code, ls_bar_la_name));
				}
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();  //변경사항을 refalsh

				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
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
	
   public String getName() {  
   	String ls_filter = iet_filter.getText().toString();
   	
//   	if (Util.f_isnull(ls_user_emp_text)) {
//   		Util.f_dialog(Dialog_seri_appr_chk.this, "");
//   		return 
//   	}
   	
      return ls_filter ;  
  }


   
   
	public void fGetList() {
		try
		{
			String ls_bar_la_code, ls_bar_la_name;
			 
			adapter.clear();
			
			curSelect = DBH.DB.rawQuery(
			      "   SELECT BAR_LA_CODE, BAR_LA_NAME" +
	            "     FROM LA_BAR_LA" +
	            "    WHERE USE_FLAG = '1'" +
	            " ORDER BY SORT_KEY, BAR_LA_NAME", null);
			int li_count = curSelect.getCount();
			
			for (int i=0;i<li_count;i++){
				curSelect.moveToNext();
				ls_bar_la_code = curSelect.getString(curSelect.getColumnIndex("BAR_LA_CODE"));
				ls_bar_la_name = curSelect.getString(curSelect.getColumnIndex("BAR_LA_NAME"));
				
				adapter.addItem(new Dialog_bar_la_Item(ls_bar_la_code, ls_bar_la_name));
			}
			
			//커서를 닫지않고 계속 가지고 있는다.
			//curSelect.close();
			

			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();  //변경사항을 refalsh
			
			//<<<---리스트뷰<<<---

		} finally {
 			//if ( pd_bef != null ) pd_bef.hide();
		}
		
	}
   
   
}