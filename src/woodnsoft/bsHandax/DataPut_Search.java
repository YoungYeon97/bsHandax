package woodnsoft.bsHandax;

import woodnsoft.bsHandax.DataListView.Insp_Search.Insp_Search_Item;
import woodnsoft.bsHandax.DataListView.Insp_Search.Insp_Search_LIstView;
import woodnsoft.bsHandax.DataListView.Insp_Search.Insp_Search_ListAdapter;
import woodnsoft.bsHandax.DataListView.Insp_Search.OnDataSelectionListener;
import woodnsoft.bsHandax.common.DBActivity;
import woodnsoft.bsHandax.common.Util;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class DataPut_Search extends DBActivity implements OnClickListener {
	
	//private static final String TAG = "DataPut_Search"; 
	Insp_Search_LIstView listView;
	Insp_Search_ListAdapter adapter;
	
	//���α׷��� ������ ����
	ProgressHandler handler;

	//Ÿ��Ʋ ��Ʈ��
	ProgressBar ipbtitle;
	TextView    itvtitle;
	
	EditText iet_search;
	Button ib_search;  //��ȸ
	
	String is_reg_no, is_reg_date;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //Ŀ���� Ÿ��Ʋ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dataput_search);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		handler = new ProgressHandler();  //���α׷��� ������
		ipbtitle = (ProgressBar) findViewById(R.id.pbtitle);
		itvtitle = (TextView) findViewById(R.id.tvtitle);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
         
		is_reg_no = bundle.getString("reg_no");
		is_reg_date = bundle.getString("reg_date");

		
		itvtitle.setText("BS ��ȸ / " + Util.getFormatDate(is_reg_date) + " [" + is_reg_no.substring(4, 8) + "-" + is_reg_no.substring(8) + "]");
		
		
		//--->>>����Ʈ��--->>>
		adapter = new Insp_Search_ListAdapter(this);
		listView = new Insp_Search_LIstView(this);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayoutDrugList );
		linLayout.addView(listView);
		
		iet_search = (EditText) findViewById(R.id.et_search);
		iet_search.setPrivateImeOptions("defaultInputmode=english;");  //�⺻���� ��������
		//iet_search.requestFocus();  //��Ŀ���� �ش�.
		
		
		ib_search = (Button) findViewById(R.id.b_search);  //��ȸ
		ib_search.setOnClickListener(this);

		

		findViewById(R.id.bBack).setOnClickListener(new OnClickListener() {  //�ڷ�
			public void onClick(View v) {
   			finish();
			}
		});
		
		iet_search.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				iet_search.setText("");
				onClick(ib_search);
				return true;
			}
		});
		
		
		// ���͸� ��������
		iet_search.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
					onClick(ib_search);
					return true;
				}
				
				return false;
			}
		});


	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		onClick(ib_search);

		//��ȸ�� Ű���� ����
		InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mInputMethodManager.hideSoftInputFromWindow(iet_search.getWindowToken(), 0);

		
	}
	
	public void fGetList() {
		try
		{
			String ls_reg_seq, ls_bar_code, ls_item_spec, ls_jata_flag, ls_line_name, ls_macn_name, ls_state_flag, ls_bigo, ls_op_no;
			
			String ls_search = iet_search.getText().toString();
			if (Util.f_isnull(ls_search)) ls_search = "%"; else ls_search = "%" + ls_search + "%";
			 
			adapter.clear();
			Cursor curSelect;
			curSelect = DBH.DB.rawQuery(
			      "   SELECT D.REG_SEQ, D.BAR_CODE, D.ITEM_SPEC, D.JATA_FLAG, LINE.LINE_NAME, D.MACN_NAME, D.STATE_FLAG, D.BIGO, D.OP_NO" +
	            "     FROM LB_INSP_DETAIL D LEFT OUTER JOIN LA_LINE LINE ON D.LINE_CODE = LINE.LINE_CODE" +
	            "    WHERE D.REG_NO = '" + is_reg_no + "'" +
	            "      AND UPPER(IFNULL(D.BAR_CODE, ' ')) LIKE UPPER('" + ls_search + "')", null);
			int li_count = curSelect.getCount();
			
			for (int i=0;i<li_count;i++){
				curSelect.moveToNext();
				ls_reg_seq    = curSelect.getString(curSelect.getColumnIndex("REG_SEQ"));
				ls_bar_code   = curSelect.getString(curSelect.getColumnIndex("BAR_CODE"));
				ls_item_spec  = curSelect.getString(curSelect.getColumnIndex("ITEM_SPEC"));
				ls_jata_flag  = curSelect.getString(curSelect.getColumnIndex("JATA_FLAG"));
				ls_line_name  = curSelect.getString(curSelect.getColumnIndex("LINE_NAME"));
				ls_macn_name  = curSelect.getString(curSelect.getColumnIndex("MACN_NAME"));
				ls_state_flag = curSelect.getString(curSelect.getColumnIndex("STATE_FLAG"));
				ls_bigo       = curSelect.getString(curSelect.getColumnIndex("BIGO"));
				ls_op_no      = curSelect.getString(curSelect.getColumnIndex("OP_NO"));
				
				fAddAdapter("1", is_reg_no, ls_reg_seq, ls_bar_code, ls_item_spec, ls_jata_flag, ls_line_name, ls_macn_name, ls_state_flag, ls_bigo, ls_op_no);
			}
			curSelect.close();
			
			Toast.makeText(getBaseContext(), "�� " + String.format("%,d", li_count) + "�� �˻��Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();

			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();  //��������� refalsh
			
			//<<<---����Ʈ��<<<---

		} finally {
 			if ( pd_bef != null ) pd_bef.hide();
		}
		
	}

	//                      ����ϼ�������,      ��ǥ��ȣ,            ��ǥSEQ,
	public void fAddAdapter(String as_mobi_flag, String as_reg_no,    String as_reg_seq, 
	//                      �ø����ȣ,          �԰�,
			                  String as_bar_code,  String as_item_spec,
	//                      ��Ÿ����,            �۾�����,            �ӽŸ�,              ����,                 ���,           OP/NO
			                  String as_jata_flag, String as_line_name, String as_macn_name, String as_state_flag, String as_bigo, String as_op_no) {

		if (Util.f_isnull(as_bar_code)) as_bar_code = "-----����-----";
		if (as_item_spec == null) as_item_spec = "";
		if (as_jata_flag == null) as_jata_flag = "";
		if (as_line_name == null) as_line_name = "";
		if (as_macn_name == null) as_macn_name = "";
		if (as_state_flag == null) as_state_flag = "";
		if (as_bigo == null) as_bigo = "";
		
		//��Ÿ����(1.�ڻ�, 0.Ÿ��)
		if (as_jata_flag.equals("1")) as_jata_flag = "�ڻ�"; else as_jata_flag = "Ÿ��";

		//����(0.X 1.O)
		if (as_state_flag.equals("0")) as_state_flag = "X"; else as_state_flag = "O";
		
		adapter.addItem(new Insp_Search_Item("1", is_reg_no, as_reg_seq, as_bar_code, as_item_spec, as_jata_flag, as_line_name, as_macn_name, as_state_flag, as_bigo, as_op_no));
	}

	public void onClick(View v) {
		if(v.equals(findViewById(R.id.b_search))) {  //��ȸ
			
			pd_bef = ProgressDialog.show(DataPut_Search.this, "","BS ���˸���� �������Դϴ�...", true);		        	
			Handler v1 = new Handler();
			v1.postDelayed(
		    		new Runnable () {
						public void run() {
							fGetList();
						}
		    		}
		    		, 100);
			
	
			iet_search.setSelection(0, iet_search.getText().length() );
			iet_search.requestFocus();  //��Ŀ���� �ش�.
			iet_search.setFocusable(true);

			//��ȸ�� Ű���� ����
			InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mInputMethodManager.hideSoftInputFromWindow(iet_search.getWindowToken(), 0);
			
		}
	}
	
}
