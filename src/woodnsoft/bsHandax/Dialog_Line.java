package woodnsoft.bsHandax;

import woodnsoft.bsHandax.DataListView.Dialog_Line.Dialog_Line_ListAdapter;
import woodnsoft.bsHandax.DataListView.Dialog_Line.Dialog_Line_ListView;
import woodnsoft.bsHandax.DataListView.Dialog_Line.Dialog_Line_Item;
import woodnsoft.bsHandax.DataListView.Dialog_Line.OnDataSelectionListener_Dialog_Line;
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

public final class Dialog_Line extends Dialog {
	private Context mContext;
	
	//private String TAG = "Dialog_Line"; 
	Dialog_Line_ListView listView;
	Dialog_Line_ListAdapter adapter;
	
	Cursor curSelect;
	
	EditText iet_filter;
	public String is_make_code, is_fact_code, is_fact_sub_code;
	public String is_line_code, is_line_name;
	
	private OnDismissListener _listener;
	private OnCancelListener _cancle;

	public Dialog_Line(Context context) {  
      super(context);
      mContext = context;
      // TODO Auto-generated constructor stub  
	}  	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_line);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		iet_filter = (EditText) findViewById(R.id.et_filter);

		//--->>>리스트뷰--->>>
		adapter = new Dialog_Line_ListAdapter(mContext);
		listView = new Dialog_Line_ListView(mContext);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayoutDrugList );
		linLayout.addView(listView);
		
		
		
		Button lb_cancel = (Button) findViewById(R.id.b_cancel);
		lb_cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if( _cancle == null ) {} else {  
					_cancle.onCancel( Dialog_Line.this ) ;
				}
				
				cancel();
			}

		});

		findViewById(R.id.b_null).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if( _listener == null ) {} else {  
					is_line_code = "";
					is_line_name = "";
					_listener.onDismiss( Dialog_Line.this ) ;  
				}
				
				dismiss() ;
			}
		});
		
		listView.setOnDataSelectionListener( new OnDataSelectionListener_Dialog_Line () {
         
   		public void onDataSelected(AdapterView<?> parent, View v, int position, long id) {
				if( _listener == null ) {} else {
					
					Dialog_Line_Item selectItem = (Dialog_Line_Item)adapter.getItem(position);
	   			
	   			is_line_code = selectItem.getData(0);  //메이커코드
	   			is_line_name = selectItem.getData(1);  //메이커명

					
					_listener.onDismiss( Dialog_Line.this ) ;  
				}
				
				dismiss() ;
                
   		}
		});       		

		iet_filter.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String ls_line_code, ls_line_name, ls_upper_line_name;
				String ls_upper_s = s.toString().toUpperCase();
				
				int li_count = curSelect.getCount();
				
				
				adapter.clear();
				curSelect.moveToFirst();
				
				for (int i=0;i<li_count;i++){
					if (i > 0) curSelect.moveToNext();
					ls_line_code = curSelect.getString(curSelect.getColumnIndex("LINE_CODE"));
					ls_line_name = curSelect.getString(curSelect.getColumnIndex("LINE_NAME"));
					ls_upper_line_name = ls_line_name.toUpperCase();
					
					//Log.i("TAG", s.toString());
					if (ls_upper_line_name.indexOf(ls_upper_s) < 0) continue;
					
					
					//Log.i("TAG", "ls_Line_name:" + ls_Line_name);
					adapter.addItem(new Dialog_Line_Item(ls_line_code, ls_line_name));
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
			String ls_line_code, ls_line_name;
			 
			adapter.clear();
			
			curSelect = DBH.DB.rawQuery(
			      "   SELECT FL.LINE_CODE, LINE.LINE_NAME" +
			      "     FROM LA_FACT_LINE FL," +
			      "          LA_LINE LINE" +
	            "    WHERE FL.LINE_CODE = LINE.LINE_CODE" +
	            "      AND FL.MAKE_CODE = '" + is_make_code + "'" +
	            "      AND FL.FACT_CODE = '" + is_fact_code + "'" +
	            "      AND FL.FACT_SUB_CODE = '" + is_fact_sub_code + "'" +
	            " ORDER BY FL.SORT_KEY, LINE.LINE_NAME", null);
			int li_count = curSelect.getCount();
			
			for (int i=0;i<li_count;i++){
				curSelect.moveToNext();
				ls_line_code = curSelect.getString(curSelect.getColumnIndex("LINE_CODE"));
				ls_line_name = curSelect.getString(curSelect.getColumnIndex("LINE_NAME"));
				
				adapter.addItem(new Dialog_Line_Item(ls_line_code, ls_line_name));
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