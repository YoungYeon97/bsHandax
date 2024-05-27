package woodnsoft.bsHandax;

import woodnsoft.bsHandax.DataListView.Insp_History.Insp_History_Item;
import woodnsoft.bsHandax.DataListView.Insp_History.Insp_History_LIstView;
import woodnsoft.bsHandax.DataListView.Insp_History.Insp_History_ListAdapter;
import woodnsoft.bsHandax.common.DBActivity;
import woodnsoft.bsHandax.common.Util;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Insp_Input extends DBActivity implements OnClickListener, OnDismissListener  {
	TextView txtMsg;
	private static final String TAG = "Insp_Input";
	protected static final int CAMERA = 0;  //ī�޶� ����� ���Ϲ޴� ���
	
	Insp_History_LIstView listView;
	Insp_History_ListAdapter adapter, adapter_null;

	EditText iet_search;
	
	//Ÿ��Ʋ ��Ʈ��
	ProgressBar ipbtitle;
	TextView    itvtitle;
	
	
	String is_imgubun;  //�Է�, ��������


	String is_reg_no,     is_reg_seq,   is_reg_skey,      is_reg_date,    is_reg_code;
	String is_make_code,  is_fact_code, is_fact_sub_code, is_line_code,   is_fact_line_seq,  is_emp_code;
	String is_op_no,      is_macn_code, is_macn_name,     is_macn_model,  is_macn_make_code, is_macn_make_name, is_maker_code, is_bar_code;
	String is_item_code,  is_item_spec, is_jata_flag,     is_bar_la_code, is_match_flag, is_make_date;
	String is_state_flag, is_chek_flag, is_case_text,     is_chek_text,   is_bigo;
	int    ii_qty;
	
	//f_retrieve������ ���
	String is_line_name, is_maker_name, is_bar_la_name, is_jata_flag_name;
	
	Button ib_search;
	TextView itv_chek_text_his;
	EditText iet_bar_code, iet_item_spec, iet_line_name, iet_op_no, iet_macn_name, iet_macn_model, iet_maker_name, iet_jata_flag_name, iet_bar_la_name, iet_bigo, iet_chek_text;

	String is_max_reg_seq;
	
	String is_onDismiss;
	Cursor curSelect;
	
	//private OnDismissListener _listener ;  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //Ŀ���� Ÿ��Ʋ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insp_input);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		

		ipbtitle = (ProgressBar) findViewById(R.id.pbtitle);
		itvtitle = (TextView) findViewById(R.id.tvtitle);
		
		iet_bar_code   = (EditText) findViewById(R.id.et_bar_code);
		iet_item_spec  = (EditText) findViewById(R.id.et_item_spec); 		iet_item_spec.setOnClickListener(this);	
		iet_line_name  = (EditText) findViewById(R.id.et_line_name); 		iet_line_name.setOnClickListener(this);
		iet_op_no      = (EditText) findViewById(R.id.et_op_no); 		   iet_op_no.setOnClickListener(this);
		iet_macn_name  = (EditText) findViewById(R.id.et_macn_name); 		iet_macn_name.setOnClickListener(this);
		iet_macn_model  = (EditText) findViewById(R.id.et_macn_model); 	iet_macn_model.setOnClickListener(this);
		iet_maker_name = (EditText) findViewById(R.id.et_maker_name); 		iet_maker_name.setOnClickListener(this);
		iet_jata_flag_name  = (EditText) findViewById(R.id.et_jata_flag_name); 		iet_jata_flag_name.setOnClickListener(this);
		iet_bar_la_name = (EditText) findViewById(R.id.et_bar_la_name); 	iet_bar_la_name.setOnClickListener(this);
		iet_bigo       = (EditText) findViewById(R.id.et_bigo); 	      	iet_bigo.setOnClickListener(this);
		iet_chek_text  = (EditText) findViewById(R.id.et_chek_text); 	   iet_chek_text.setOnClickListener(this);
		
		itv_chek_text_his = (TextView) findViewById(R.id.tv_chek_text_his);
		ib_search = (Button) findViewById(R.id.b_search);   	ib_search.setOnClickListener(this);    //��ȸ
		
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
         
		is_reg_no =  bundle.getString("reg_no"); //��ǥ��ȣ
		is_reg_seq = bundle.getString("reg_seq"); //��ǥSEQ
		if (Util.f_isnull(is_reg_no)) {
			is_imgubun = "�Է�";
		} else {
			is_imgubun = "����";
		}
		
		//--->>>����Ʈ��--->>>
		adapter = new Insp_History_ListAdapter(this);
		listView = new Insp_History_LIstView(this);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayoutDrugList );
		linLayout.addView(listView);

		iet_search = (EditText) findViewById(R.id.et_search);
		iet_search.requestFocus();  //��Ŀ���� �ش�.
		
		
		f_retrieve(is_reg_no, is_reg_seq);
		f_history(iet_search.getText().toString());
		f_set_title();

		
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
					
					iet_search.setSelection(0, iet_search.getText().length() );
					iet_search.requestFocus();  //��Ŀ���� �ش�.
					return true;
				}
				
				return false;
			}
		});

		//����
		findViewById(R.id.b_delete).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				findViewById(R.id.b_delete).setEnabled(false);
				f_delete_insp_detail();
				
				iet_search.setSelection(0, iet_search.getText().length() );
				iet_search.requestFocus();  //��Ŀ���� �ش�.
			}
		});

		//����
		findViewById(R.id.b_rec).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final Intent pintent = Insp_Input.this.getPackageManager().getLaunchIntentForPackage("de.fun2code.android.voicerecord");
		      try{
		      	Insp_Input.this.startActivity(pintent);
		      } catch (Exception e) {
					Log.e(TAG, "no install rec");
					Util.f_dialog(Insp_Input.this,"�������� ��ġ�Ͻʽÿ�!");
				}
		      
			}
		});

		//����
		findViewById(R.id.b_pic).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//ī�޶� ���α׷��� �����Ѵ�.
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent,CAMERA);
				
				iet_search.setSelection(0, iet_search.getText().length() );
				iet_search.requestFocus();  //��Ŀ���� �ش�.
			}
		});

		//����
		findViewById(R.id.b_close).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		if (gs_mobi_flag.equals("0")) {  //��ȸ���̸�
			iet_search.setEnabled(false);           iet_search.setFocusable(false);
			iet_bar_code.setEnabled(false);         iet_bar_code.setFocusable(false);
			iet_item_spec.setEnabled(false);        iet_item_spec.setFocusable(false);
			iet_line_name.setEnabled(false);        iet_line_name.setFocusable(false);
			iet_op_no.setEnabled(false);            iet_op_no.setFocusable(false);
			iet_macn_name.setEnabled(false);        iet_macn_name.setFocusable(false);
			iet_macn_model.setEnabled(false);       iet_macn_model.setFocusable(false);
			iet_maker_name.setEnabled(false);       iet_maker_name.setFocusable(false);
			iet_jata_flag_name.setEnabled(false);   iet_jata_flag_name.setFocusable(false);
			iet_bar_la_name.setEnabled(false);      iet_bar_la_name.setFocusable(false);
			iet_bigo.setEnabled(false);             iet_bigo.setFocusable(false);
			iet_chek_text.setEnabled(false);        iet_chek_text.setFocusable(false);
			ib_search.setEnabled(false);
			findViewById(R.id.b_delete).setEnabled(false);
			findViewById(R.id.b_rec).setEnabled(false);
			findViewById(R.id.b_pic).setEnabled(false);
		}
		
		iet_search.setFocusable(true);
		iet_search.setSelection(0, iet_search.getText().length() );
		iet_search.requestFocus();  //��Ŀ���� �ش�.
		
	};

	public void f_set_title() {
		String ls_mobi_flag_text, ls_reg_seq_text;
		
		if (gs_mobi_flag.equals("1")) ls_mobi_flag_text = ""; else ls_mobi_flag_text = " [��ȸ����]";
			
		
		if (Util.f_isnull(is_reg_seq)) ls_reg_seq_text = ""; else ls_reg_seq_text = " [����:" + is_reg_seq + "]";
		
		itvtitle.setText("BS " + is_imgubun + " / " + Util.getFormatDate(gs_reg_date) + ls_reg_seq_text + ls_mobi_flag_text);		
	}
	
	public void f_get_lb_insp_detail(Cursor curSelect) {
		is_reg_no         = curSelect.getString(curSelect.getColumnIndex("REG_NO"));
		is_reg_seq        = curSelect.getString(curSelect.getColumnIndex("REG_SEQ"));
		is_reg_skey       = curSelect.getString(curSelect.getColumnIndex("REG_SKEY"));
		is_reg_date       = curSelect.getString(curSelect.getColumnIndex("REG_DATE"));
		is_reg_code       = curSelect.getString(curSelect.getColumnIndex("REG_CODE"));
		
		is_make_code      = curSelect.getString(curSelect.getColumnIndex("MAKE_CODE"));
		is_fact_code      = curSelect.getString(curSelect.getColumnIndex("FACT_CODE"));
		is_fact_sub_code  = curSelect.getString(curSelect.getColumnIndex("FACT_SUB_CODE"));
		is_line_code      = curSelect.getString(curSelect.getColumnIndex("LINE_CODE"));
		is_line_name      = curSelect.getString(curSelect.getColumnIndex("LINE_NAME"));
		is_fact_line_seq  = curSelect.getString(curSelect.getColumnIndex("FACT_LINE_SEQ"));
		is_emp_code       = curSelect.getString(curSelect.getColumnIndex("EMP_CODE"));
		
		is_op_no          = curSelect.getString(curSelect.getColumnIndex("OP_NO"));
		is_macn_code      = curSelect.getString(curSelect.getColumnIndex("MACN_CODE"));
		is_macn_name      = curSelect.getString(curSelect.getColumnIndex("MACN_NAME"));
		is_macn_model     = curSelect.getString(curSelect.getColumnIndex("MACN_MODEL"));
		is_macn_make_code = curSelect.getString(curSelect.getColumnIndex("MACN_MAKE_CODE"));
		is_macn_make_name = curSelect.getString(curSelect.getColumnIndex("MACN_MAKE_NAME"));
		is_maker_code     = curSelect.getString(curSelect.getColumnIndex("MAKER_CODE"));
		is_maker_name     = curSelect.getString(curSelect.getColumnIndex("MAKER_NAME"));
		is_bar_code       = curSelect.getString(curSelect.getColumnIndex("BAR_CODE"));
		
		is_item_code      = curSelect.getString(curSelect.getColumnIndex("ITEM_CODE"));
		is_item_spec      = curSelect.getString(curSelect.getColumnIndex("ITEM_SPEC"));
		is_jata_flag      = curSelect.getString(curSelect.getColumnIndex("JATA_FLAG"));
		is_jata_flag_name = curSelect.getString(curSelect.getColumnIndex("JATA_FLAG_NAME"));
		is_bar_la_code    = curSelect.getString(curSelect.getColumnIndex("BAR_LA_CODE"));
		is_bar_la_name    = curSelect.getString(curSelect.getColumnIndex("BAR_LA_NAME"));
		is_match_flag     = curSelect.getString(curSelect.getColumnIndex("MATCH_FLAG"));
		is_make_date      = curSelect.getString(curSelect.getColumnIndex("MAKE_DATE"));
		
		is_state_flag     = curSelect.getString(curSelect.getColumnIndex("STATE_FLAG"));  //���� 0:X, 1:0
		is_chek_flag      = curSelect.getString(curSelect.getColumnIndex("CHEK_FLAG"));   //Ȯ�ο�û
		is_case_text      = curSelect.getString(curSelect.getColumnIndex("CASE_TEXT"));
		is_chek_text      = curSelect.getString(curSelect.getColumnIndex("CHEK_TEXT"));
		is_bigo           = curSelect.getString(curSelect.getColumnIndex("BIGO"));

		
		
		ii_qty = 1;  //QTY�� �׳� 1�� �������ش�.
	}
	
	
	public void f_retrieve(String as_reg_no, String as_reg_seq) {
		try
		{
			if (Util.f_isnull(as_reg_no) || Util.f_isnull(as_reg_seq)) return;
			
			Cursor curSelect;

			curSelect = DBH.DB.rawQuery(
					"   SELECT D.REG_NO,     D.REG_SEQ,   D.REG_SKEY,      D.REG_DATE,    D.REG_CODE," +
					"          D.MAKE_CODE,  D.FACT_CODE, D.FACT_SUB_CODE, D.LINE_CODE,   D.FACT_LINE_SEQ,  D.EMP_CODE," +
					"          D.OP_NO,      D.MACN_CODE, D.MACN_NAME,     D.MACN_MODEL,  D.MACN_MAKE_CODE, D.MACN_MAKE_NAME, D.MAKER_CODE, D.BAR_CODE," +
					"          D.ITEM_CODE,  D.ITEM_SPEC, D.JATA_FLAG,     D.BAR_LA_CODE, D.MATCH_FLAG, D.MAKE_DATE," +
					"          D.STATE_FLAG, D.CHEK_FLAG, D.CASE_TEXT,     D.CHEK_TEXT,   D.QTY,            D.BIGO," +

					"          LINE.LINE_NAME, MAKER.MAKER_NAME, BAR_LA.BAR_LA_NAME," +
					"          CASE IFNULL(D.JATA_FLAG, '0') WHEN '0' THEN 'Ÿ��' WHEN '1' THEN '�ڻ�' END JATA_FLAG_NAME" +
		         "     FROM (((" +
					"          LB_INSP_DETAIL D LEFT OUTER JOIN LA_LINE LINE ON D.LINE_CODE = LINE.LINE_CODE)" +
		         "                           LEFT OUTER JOIN LA_MAKER MAKER ON D.MAKER_CODE = MAKER.MAKER_CODE) " +
		         "                           LEFT OUTER JOIN LA_BAR_LA BAR_LA ON D.BAR_LA_CODE = BAR_LA.BAR_LA_CODE) " +
					"    WHERE D.REG_NO = '" + as_reg_no + "'" +
					"      AND D.REG_SEQ = '" + as_reg_seq + "'", null);
			if (curSelect.getCount() <= 0) {
				Log.e(TAG, "f_retrieve-�˼����� ������ �߻��Ͽ����ϴ�.");
				Util.f_dialog(Insp_Input.this,"�˼����� ������ �߻��Ͽ����ϴ�.");
				return;
			}

			curSelect.moveToNext();
			
			f_get_lb_insp_detail(curSelect);  //LB_INSP_DETAIL�� ������ �ν��Ͻ� ������ ��´�.

			curSelect.close();

			Message msg = handler.obtainMessage(); msg.obj = "retrieve"; handler.sendMessage(msg);
			
			if (is_imgubun.equals("����") == false) is_imgubun = "����";
			
		} catch (Exception e) {
			Log.e(TAG, "f_retrieve");
			Util.f_dialog(Insp_Input.this,"��ǥ�� �ҷ������߿� ������ �߻��Ͽ����ϴ�.");
			return;
		}
	}	
	
	//�����嶧���� ���� ������� ����
	public void f_retrieve_handle() {
		iet_search.setText(is_bar_code);
		iet_bar_code.setText(is_bar_code);
		iet_item_spec.setText(is_item_spec);
		iet_line_name.setText(is_line_name);
		iet_op_no.setText(is_op_no);
		iet_macn_name.setText(is_macn_name);
		iet_macn_model.setText(is_macn_model);
		iet_maker_name.setText(is_maker_name);
		iet_jata_flag_name.setText(is_jata_flag_name);
		iet_bar_la_name.setText(is_bar_la_name);
		iet_bigo.setText(is_bigo);
		iet_chek_text.setText(is_chek_text);
	}
	
	public void f_history(String as_bar_code) {
		try
		{
			String ls_reg_no, ls_reg_seq, ls_reg_date;
			String /*ls_bar_code, */ls_item_spec, ls_line_code, ls_line_name, ls_op_no, ls_macn_code, ls_macn_name, ls_macn_model, ls_maker_code, ls_maker_name, ls_jata_flag, ls_bar_la_code, ls_bar_la_name, ls_jata_flag_name, ls_bigo, /*ls_case_text, */ls_chek_text;
			
			if (Util.f_isnull(as_bar_code)) {
				if (is_imgubun.equals("�Է�"))  f_reset();  //�Է¸�常 ����
				return;
			}
			Cursor curSelect;
			curSelect = DBH.DB.rawQuery(
					"   SELECT D.REG_NO,     D.REG_SEQ,   D.REG_SKEY,      D.REG_DATE,    D.REG_CODE," +
					"          D.MAKE_CODE,  D.FACT_CODE, D.FACT_SUB_CODE, D.LINE_CODE,   D.FACT_LINE_SEQ,  D.EMP_CODE," +
					"          D.OP_NO,      D.MACN_CODE, D.MACN_NAME,     D.MACN_MODEL,  D.MACN_MAKE_CODE, D.MACN_MAKE_NAME, D.MAKER_CODE, D.BAR_CODE," +
					"          D.ITEM_CODE,  D.ITEM_SPEC, D.JATA_FLAG,     D.BAR_LA_CODE, D.MATCH_FLAG, D.MAKE_DATE," +
					"          D.STATE_FLAG, D.CHEK_FLAG, D.CASE_TEXT,     D.CHEK_TEXT,   D.QTY,            D.BIGO," +
			      
					"          LINE.LINE_NAME, MAKER.MAKER_NAME, BAR_LA.BAR_LA_NAME," +
					"          CASE IFNULL(D.JATA_FLAG, '0') WHEN '0' THEN 'Ÿ��' WHEN '1' THEN '�ڻ�' END JATA_FLAG_NAME" +
		         "     FROM (((" +
					"          LB_INSP_DETAIL D LEFT OUTER JOIN LA_LINE LINE ON D.LINE_CODE = LINE.LINE_CODE)" +
		         "                           LEFT OUTER JOIN LA_MAKER MAKER ON D.MAKER_CODE = MAKER.MAKER_CODE) " +
		         "                           LEFT OUTER JOIN LA_BAR_LA BAR_LA ON D.BAR_LA_CODE = BAR_LA.BAR_LA_CODE) " +
					"    WHERE UPPER(D.BAR_CODE) = UPPER('" + as_bar_code + "')" +
					"      AND D.REG_DATE <= '" + gs_reg_date + "'" +
					" ORDER BY D.REG_DATE DESC, D.REG_NO DESC, D.REG_SEQ DESC", null);
			int li_count = curSelect.getCount();
			
			if (li_count <= 0) {
				ls_item_spec  = is_item_spec;
				ls_line_code  = is_line_code;
				ls_line_name  = is_line_name;
				ls_op_no      = is_op_no;
				ls_macn_code  = is_macn_code;
				ls_macn_name  = is_macn_name;
				ls_macn_model = is_macn_model;
				ls_maker_code = is_maker_code;
				ls_maker_name = is_maker_name;
				ls_jata_flag  = is_jata_flag;
				ls_jata_flag_name = is_jata_flag_name;
				ls_bar_la_code = is_bar_la_code;
				ls_bar_la_name = is_bar_la_name;
				ls_bigo       = is_bigo;
				
				if (is_imgubun.equals("�Է�"))  f_reset();  //�Է¸�常 ����
				
				is_item_spec  = ls_item_spec;
				is_line_code  = ls_line_code;
				is_line_name  = ls_line_name;
				is_op_no      = ls_op_no;
				is_macn_code  = ls_macn_code;
				is_macn_name  = ls_macn_name;
				is_macn_model = ls_macn_model;
				is_maker_code = ls_maker_code;
				is_maker_name = ls_maker_name;
				is_jata_flag  = ls_jata_flag;
				is_jata_flag_name = ls_jata_flag_name;
				is_bar_la_code = ls_bar_la_code;
				is_bar_la_name = ls_bar_la_name;
				is_bigo       = ls_bigo;

				iet_line_name.setText(is_line_name);
				iet_op_no.setText(is_op_no);
				iet_macn_name.setText(is_macn_name);
				iet_macn_model.setText(is_macn_model);
				iet_maker_name.setText(is_maker_name);
				iet_jata_flag_name.setText(is_jata_flag_name);
				iet_bar_la_name.setText(is_bar_la_name);
				iet_bigo.setText(is_bigo);
				
				iet_item_spec.setTextColor(getResources().getColor(R.color.insp_input_false));
				iet_line_name.setTextColor(getResources().getColor(R.color.insp_input_false));
				iet_op_no.setTextColor(getResources().getColor(R.color.insp_input_false));
				iet_macn_name.setTextColor(getResources().getColor(R.color.insp_input_false));
				iet_macn_model.setTextColor(getResources().getColor(R.color.insp_input_false));
				iet_maker_name.setTextColor(getResources().getColor(R.color.insp_input_false));
				iet_jata_flag_name.setTextColor(getResources().getColor(R.color.insp_input_false));
				iet_bar_la_name.setTextColor(getResources().getColor(R.color.insp_input_false));
				iet_bigo.setTextColor(getResources().getColor(R.color.insp_input_false));

			} else {
				adapter.clear();

				iet_item_spec.setTextColor(getResources().getColor(R.color.insp_input_true));
				iet_line_name.setTextColor(getResources().getColor(R.color.insp_input_true));
				iet_op_no.setTextColor(getResources().getColor(R.color.insp_input_true));
				iet_macn_name.setTextColor(getResources().getColor(R.color.insp_input_true));
				iet_macn_model.setTextColor(getResources().getColor(R.color.insp_input_true));
				iet_maker_name.setTextColor(getResources().getColor(R.color.insp_input_true));
				iet_jata_flag_name.setTextColor(getResources().getColor(R.color.insp_input_true));
				iet_bar_la_name.setTextColor(getResources().getColor(R.color.insp_input_true));
				iet_bigo.setTextColor(getResources().getColor(R.color.insp_input_true));
				
				itv_chek_text_his.setText(getString(R.string.tv_chek_text_his_exists1) + String.format("%,d", li_count) + getString(R.string.tv_chek_text_his_exists2));  //"�� �̷� �� ", "��"
				
				
				for (int i=0;i<li_count;i++){
					curSelect.moveToNext();
					ls_reg_no     = curSelect.getString(curSelect.getColumnIndex("REG_NO"));
					ls_reg_seq    = curSelect.getString(curSelect.getColumnIndex("REG_SEQ"));
					ls_reg_date   = curSelect.getString(curSelect.getColumnIndex("REG_DATE"));
					//ls_bar_code   = curSelect.getString(curSelect.getColumnIndex("BAR_CODE"));
					//ls_item_spec  = curSelect.getString(curSelect.getColumnIndex("ITEM_SPEC"));
					ls_line_name  = curSelect.getString(curSelect.getColumnIndex("LINE_NAME"));
					//ls_macn_name  = curSelect.getString(curSelect.getColumnIndex("MACN_NAME"));
					//ls_macn_model = curSelect.getString(curSelect.getColumnIndex("MACN_MODEL"));
					ls_maker_name = curSelect.getString(curSelect.getColumnIndex("MAKER_NAME"));
					//ls_jata_flag  = curSelect.getString(curSelect.getColumnIndex("JATA_FLAG"));
					ls_jata_flag_name  = curSelect.getString(curSelect.getColumnIndex("JATA_FLAG_NAME"));
					ls_bar_la_name = curSelect.getString(curSelect.getColumnIndex("BAR_LA_NAME"));
					//ls_bigo       = curSelect.getString(curSelect.getColumnIndex("BIGO"));
					//ls_case_text  = curSelect.getString(curSelect.getColumnIndex("CASE_TEXT"));
					ls_chek_text  = curSelect.getString(curSelect.getColumnIndex("CHEK_TEXT"));
					
					
					if (is_imgubun.equals("�Է�")) {  //�Է¸�常 ���� �ֽ��� �ø��� ��ȣ�� ä���ش�. 
						if (i == 0) {  //��ȸ���̸� ���� �ֽ��� �ø����ȣ�� ü���ش�.
						//if ((i == 0) && !(Util.f_isnull(iet_bar_code.getText().toString()))) {  //��ȸ���̸� ���� �ֽ��� �ø����ȣ�� ü���ش�.
							f_get_lb_insp_detail(curSelect);  //LB_INSP_DETAIL�� ������ �ν��Ͻ� ������ ��´�.
							if (ls_reg_no.equals(gs_reg_no)) {  //���� �ֱ��� ����Ÿ�� ����ǥ�� ������ ����ǥ�� ����� �������� ������ �Է¸��� ä���
								if (is_imgubun.equals("����") == false) is_imgubun = "����";
								
								f_retrieve_handle();  //�����嶧���� ���� ������� ����
							} else {
								iet_search.setText(is_bar_code);
								iet_item_spec.setText(is_item_spec);
								
//								iet_line_name.setText(ls_line_name);
//								iet_macn_name.setText(is_macn_name);
//								iet_macn_model.setText(is_macn_model);
//								iet_maker_name.setText(ls_maker_name);
//								iet_jata_flag_name.setText(ls_jata_flag_name);
//								iet_bar_la_name.setText(ls_bar_la_name);
								
								iet_line_name.setText(is_line_name);
								iet_op_no.setText(is_op_no);
								iet_macn_name.setText(is_macn_name);
								iet_macn_model.setText(is_macn_model);
								iet_maker_name.setText(is_maker_name);
								iet_jata_flag_name.setText(is_jata_flag_name);
								iet_bar_la_name.setText(is_bar_la_name);
								
								//����Ÿ�� �������� ������ �ʿ䰡 ���� ����Ÿ �ʱ�ȭ
								is_state_flag     = "1"; //���� 0:X, 1:0
								is_chek_flag      = "0"; //Ȯ�ο�û
								is_case_text      = "";
								is_chek_text      = "";
								is_bigo           = "";
	
								iet_bigo.setText(is_bigo);
								iet_chek_text.setText(is_chek_text);
	
								//�Է������̱⶧���� ��������ϰ�� �Է¸��� ����
								is_reg_no = "";
								is_reg_seq = "";
							}
						}
					}
					
					if (Util.f_isnull(ls_chek_text)) ls_chek_text = "����";
					adapter.addItem(new Insp_History_Item(ls_reg_no, ls_reg_seq, Util.getFormatDate(ls_reg_date), ls_chek_text));
				}
			}
			curSelect.close();

			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();  //��������� refalsh
			
		} catch (Exception e) {
			Log.e(TAG, "f_retrieve");
			Util.f_dialog(Insp_Input.this,"�̷������� �ҷ������߿� ������ �߻��Ͽ����ϴ�.");
		}
		
	}	

	public void f_reset() {
		itv_chek_text_his.setText(getString(R.string.tv_chek_text_his));  //"�� �̷¾���"
		
		
		is_reg_no = "";      is_reg_seq = "";   is_reg_skey = "";      is_reg_date = "";    is_reg_code = "";
		is_make_code = "";   is_fact_code = ""; is_fact_sub_code = ""; is_line_code = "";   is_fact_line_seq = "";  is_emp_code = "";
		is_op_no = "";       is_macn_code = ""; is_macn_name = "";     is_macn_model = "";  is_macn_make_code = ""; is_macn_make_name = ""; is_maker_code = ""; is_bar_code = "";
		is_item_code = "";   is_item_spec = ""; is_jata_flag = "0";    is_bar_la_code = ""; is_match_flag = "0";    is_make_date = "";
		is_state_flag = "1"; is_chek_flag = ""; is_case_text = "";     is_chek_text = "";   is_bigo = "";
		ii_qty = 1;

		is_line_name = ""; 
		is_maker_name = "";
		is_jata_flag_name = "";
		is_bar_la_name = "";
		
		//iet_search.setText(ls_bar_code);
		iet_bar_code.setText("");
		iet_item_spec.setText("");

		iet_line_name.setText("");
		iet_op_no.setText("");
		iet_macn_name.setText("");
		iet_macn_model.setText("");
		iet_maker_name.setText("");
		iet_jata_flag_name.setText("Ÿ��"); //�������� ������ ��Ÿ������ Default Value�� Ÿ�� ����
		iet_bar_la_name.setText("");
			
		iet_bigo.setText("");
		iet_chek_text.setText("");

		iet_item_spec.setTextColor(getResources().getColor(R.color.insp_input_true));
		iet_line_name.setTextColor(getResources().getColor(R.color.insp_input_true));
		iet_op_no.setTextColor(getResources().getColor(R.color.insp_input_true));
		iet_macn_name.setTextColor(getResources().getColor(R.color.insp_input_true));
		iet_macn_model.setTextColor(getResources().getColor(R.color.insp_input_true));
		iet_maker_name.setTextColor(getResources().getColor(R.color.insp_input_true));
		iet_jata_flag_name.setTextColor(getResources().getColor(R.color.insp_input_true));
		iet_bar_la_name.setTextColor(getResources().getColor(R.color.insp_input_true));
		iet_bigo.setTextColor(getResources().getColor(R.color.insp_input_true));
		
		adapter.clear();
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();  //��������� refalsh
		
	}
	
	static EditText edit = null;

	
	public void onClick(View v) {
		if(v.equals(findViewById(R.id.b_search))) {  //Ȯ��
			ib_search.setEnabled(false);
			//�Է¸��� ����
			if (is_imgubun.equals("�Է�") == false) is_imgubun = "�Է�";
	
			f_history(iet_search.getText().toString());
			f_set_title();

			is_bar_code = iet_search.getText().toString();
			iet_bar_code.setText(is_bar_code);
			
			iet_search.setFocusable(true);
			iet_search.setSelection(0, iet_search.getText().length() );
			iet_search.requestFocus();  //��Ŀ���� �ش�.
			
			//��ȸ�� Ű���� ����
			InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mInputMethodManager.hideSoftInputFromWindow(iet_search.getWindowToken(), 0);
			
			f_save_insp_detail();
			
			iet_search.setFocusable(true);
			iet_search.setSelection(0, iet_search.getText().length() );
			
		} else if (v.equals(findViewById(R.id.et_item_spec))) {  //�԰��÷�
			/*
			Dialog_sle dialog = new Dialog_sle( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "item_spec";
			final EditText let = (EditText) dialog.findViewById(R.id.et);
			
			let.setText(iet_item_spec.getText().toString());
			let.setSelection(0, let.getText().length() );
			
			//���̾�α�â �����Ŀ� �ٷ� Ű���带 ���̰� ��
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.showSoftInput(let, InputMethodManager.SHOW_FORCED);
				}
			}, 1);*/
			
			Dialog_item_spec dialog = new Dialog_item_spec( this ) ;  
			dialog.setOnDismissListener( this ) ;  

			dialog.is_maker_code = is_maker_code;
			
			dialog.show();
			is_onDismiss = "item_spec_list";
			
			
			final EditText let = (EditText) dialog.findViewById(R.id.et_filter);
			//let.setText(iet_bigo.getText().toString());
			//let.setSelection(0, let.getText().length() );

			//���̾�α�â �����Ŀ� Ű���带 ����
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.hideSoftInputFromWindow(let.getWindowToken(), 0);

				}
			}, 1);

			
		} else if (v.equals(findViewById(R.id.et_line_name))) {  //�۾�����
			Dialog_Line dialog = new Dialog_Line( this ) ;  
			dialog.setOnDismissListener( this ) ;  

			dialog.is_make_code = gs_make_code;
			dialog.is_fact_code = gs_fact_code;
			dialog.is_fact_sub_code = gs_fact_sub_code;
			
			dialog.show();
			is_onDismiss = "line";
			
			
			final EditText let = (EditText) dialog.findViewById(R.id.et_filter);
			//let.setText(iet_bigo.getText().toString());
			//let.setSelection(0, let.getText().length() );

			//���̾�α�â �����Ŀ� Ű���带 ����
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.hideSoftInputFromWindow(let.getWindowToken(), 0);

				}
			}, 1);
		} else if (v.equals(findViewById(R.id.et_op_no))) {  //OP/NO
			Dialog_sle dialog = new Dialog_sle( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "op";
			final EditText let = (EditText) dialog.findViewById(R.id.et);
			final TextView ltv = (TextView) dialog.findViewById(R.id.tv);
			
			ltv.setText(getString(R.string.op_no_)); //OP/NO :
			let.setText(iet_op_no.getText().toString());
			let.setSelection(0, let.getText().length() );
			
			//���̾�α�â �����Ŀ� �ٷ� Ű���带 ���̰� ��
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.showSoftInput(let, InputMethodManager.SHOW_FORCED);
				}
			}, 1);
		} else if (v.equals(findViewById(R.id.et_macn_name))) {  //�ǳ�
			Dialog_sle dialog = new Dialog_sle( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "macn";
			final EditText let = (EditText) dialog.findViewById(R.id.et);
			final TextView ltv = (TextView) dialog.findViewById(R.id.tv);
			
			ltv.setText(getString(R.string.macn_name_)); //�ӽŸ� :
			let.setText(iet_macn_name.getText().toString());
			let.setSelection(0, let.getText().length() );
			
			//���̾�α�â �����Ŀ� �ٷ� Ű���带 ���̰� ��
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.showSoftInput(let, InputMethodManager.SHOW_FORCED);
				}
			}, 1);
			
		} else if (v.equals(findViewById(R.id.et_macn_model))) {  //�ӽ�
			Dialog_sle dialog = new Dialog_sle( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "model";
			final EditText let = (EditText) dialog.findViewById(R.id.et);
			final TextView ltv = (TextView) dialog.findViewById(R.id.tv);
			
			ltv.setText(getString(R.string.macn_model_)); //�ӽŸ� :
			let.setText(iet_macn_model.getText().toString());
			let.setSelection(0, let.getText().length() );
			
			//���̾�α�â �����Ŀ� �ٷ� Ű���带 ���̰� ��
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.showSoftInput(let, InputMethodManager.SHOW_FORCED);
				}
			}, 1);
			
		} else if (v.equals(findViewById(R.id.et_maker_name))) {  //����Ŀ
			Dialog_Maker dialog = new Dialog_Maker( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "maker";
			
			
			final EditText let = (EditText) dialog.findViewById(R.id.et_filter);
			//let.setText(iet_bigo.getText().toString());
			//let.setSelection(0, let.getText().length() );

			//���̾�α�â �����Ŀ� Ű���带 ����
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.hideSoftInputFromWindow(let.getWindowToken(), 0);

				}
			}, 1);
		} else if (v.equals(findViewById(R.id.et_jata_flag_name))) {  //��Ÿ����
			Dialog_Jata dialog = new Dialog_Jata( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "jata";
		} else if (v.equals(findViewById(R.id.et_bar_la_name))) {  //��ǰ����
			Dialog_bar_la dialog = new Dialog_bar_la( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "bar_la";
			
			
			final EditText let = (EditText) dialog.findViewById(R.id.et_filter);
			//let.setText(iet_bigo.getText().toString());
			//let.setSelection(0, let.getText().length() );

			//���̾�α�â �����Ŀ� Ű���带 ����
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.hideSoftInputFromWindow(let.getWindowToken(), 0);

				}
			}, 1);
		} else if (v.equals(findViewById(R.id.et_bigo))) {  //���
			Dialog_mle dialog = new Dialog_mle( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "bigo";
			final EditText let = (EditText) dialog.findViewById(R.id.et);
			let.setText(iet_bigo.getText().toString());
			let.setSelection(0, let.getText().length() );

			
			//���̾�α�â �����Ŀ� �ٷ� Ű���带 ���̰� ��
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.showSoftInput(let, InputMethodManager.SHOW_FORCED);
				}
			}, 1);
			
			
			
		} else if (v.equals(findViewById(R.id.et_chek_text))) {  //��ġ����
			
			if (Util.f_isnull(is_reg_no)) {
				Util.f_dialog(Insp_Input.this,"\"" + getString(R.string.bar_code) + "\" �Ǵ� \"" + getString(R.string.item_spec) + "\"�� ���� �Է��� ��ġ������ �Է��Ͻ� �� �ֽ��ϴ�.");
			} else {
				Bundle bundle = new Bundle();
				
				bundle.putString("reg_no", is_reg_no);    //��ǥ��ȣ
				bundle.putString("reg_seq", is_reg_seq);  //��ǥSEQ
				bundle.putString("reg_date", is_reg_date);  //��ǥ����
				bundle.putString("reg_code", "0001");  //��ǥCODE
				
				Intent linsp_case_chek = new Intent(getBaseContext(), Insp_Case_Chek.class);
				linsp_case_chek.putExtras(bundle);
				startActivityForResult(linsp_case_chek, 1);
			}
			
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK) { // ��Ƽ��Ƽ�� ���������� ����Ǿ��� ���
			if(requestCode==1) {
				is_case_text = data.getStringExtra("case_text");
				is_chek_text = data.getStringExtra("chek_text");
				is_state_flag = data.getStringExtra("state_flag");   //0.X 1.O
				
				iet_chek_text.setText(is_chek_text);
				
				f_save_insp_detail();
			}
		}
		
		iet_search.setSelection(0, iet_search.getText().length() );
		iet_search.requestFocus();  //��Ŀ���� �ش�.

	}
	
   public void onDismiss(DialogInterface ad_dialog) {  
      // TODO Auto-generated method stub  
   	
   	if (is_onDismiss.equals("item_spec")) {
   		Dialog_sle dialog = (Dialog_sle) ad_dialog ;  
   		is_item_spec = dialog.getName();
   		iet_item_spec.setText(is_item_spec);
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("item_spec_list")) {
   		Dialog_item_spec dialog = (Dialog_item_spec) ad_dialog ;
   		
   		is_item_spec = dialog.is_item_spec;
   		iet_item_spec.setText(is_item_spec) ;
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("line")) {  //�۾�����
   		Dialog_Line dialog = (Dialog_Line) ad_dialog ;
   		
   		is_line_code = dialog.is_line_code;
   		is_line_name = dialog.is_line_name;
   		iet_line_name.setText(is_line_name) ;
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("op")) {  //OP/NO
   		Dialog_sle dialog = (Dialog_sle) ad_dialog ;
   		
   		is_op_no = dialog.getName();
   		iet_op_no.setText(is_op_no);
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("macn")) {  //�ǳ�
   		Dialog_sle dialog = (Dialog_sle) ad_dialog ;
   		
   		is_macn_name = dialog.getName();
   		iet_macn_name.setText(is_macn_name);
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("model")) {  //�ӽ�
   		Dialog_sle dialog = (Dialog_sle) ad_dialog ;
   		
   		is_macn_model = dialog.getName();
   		iet_macn_model.setText(is_macn_model);
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("maker")) {  //����Ŀ
   		Dialog_Maker dialog = (Dialog_Maker) ad_dialog ;
   		
   		is_maker_code = dialog.is_maker_code;
   		is_maker_name = dialog.is_maker_name;
   		iet_maker_name.setText(is_maker_name) ;
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("jata")) {  //��Ÿ����
   		Dialog_Jata dialog = (Dialog_Jata) ad_dialog ;
   		
   		is_jata_flag = dialog.is_jata_flag;
   		is_jata_flag_name = dialog.is_jata_flag_name;
   		iet_jata_flag_name.setText(is_jata_flag_name) ;
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("bar_la")) {  //��ǰ����
   		Dialog_bar_la dialog = (Dialog_bar_la) ad_dialog ;
   		
   		is_bar_la_code = dialog.is_bar_la_code;
   		is_bar_la_name = dialog.is_bar_la_name;
   		iet_bar_la_name.setText(is_bar_la_name) ;
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("bigo")) {
   		Dialog_mle dialog = (Dialog_mle) ad_dialog ;  
   		is_bigo = dialog.getName();
   		iet_bigo.setText(is_bigo) ;
   		
   		f_save_insp_detail();
   	}
   	
		iet_search.setSelection(0, iet_search.getText().length() );
		iet_search.requestFocus();  //��Ŀ���� �ش�.

   }

	public void f_save_insp_detail() {
		if (gs_mobi_flag.equals("0")) {
			Util.f_dialog(Insp_Input.this,"��ȸ���� ��忡���� �����Ͻ� �� �����ϴ�.");
			return;
		}
		
		final String ls_imgubun = is_imgubun;
		if (ls_imgubun.equals("�Է�")) 
			pd = ProgressDialog.show(Insp_Input.this, "","���˳����� �߰��ϰ� �ֽ��ϴ�.\n��ø� ��ٷ� �ֽʽÿ�!", true);
		else
			pd = ProgressDialog.show(Insp_Input.this, "","���˳����� �����ϰ� �ֽ��ϴ�.\n��ø� ��ٷ� �ֽʽÿ�!", true);
		
		handler.postDelayed(new Runnable () {public void run() {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					f_save_insp_detail_run();
				}
			});
			thread.start();
		}}, 300);
		
	}
	
   
   public int f_save_insp_detail_run() {
		try {
	   	String ls_insert = null, ls_update = null;
	   	
	   	//�ø����ȣ Or �԰� Or ��� Or ���� Or OP/NO Or �ӽ� Or ����Ŀ�� ��� ä���� ���� ������ �������� �ʾƵ� �ȴ�.
	   	if ((Util.f_isnull(is_bar_code)) && (Util.f_isnull(is_item_spec)) && (Util.f_isnull(is_bigo)) && (Util.f_isnull(is_line_code)) && (Util.f_isnull(is_op_no)) && (Util.f_isnull(is_macn_name)) && (Util.f_isnull(is_macn_model)) && (Util.f_isnull(is_maker_code)) && (Util.f_isnull(is_bar_la_code))) {
	   		if ((Util.f_isnull(is_reg_no)) == false) {
	   			f_delete_insp_detail_run();  //�׸� ��ΰ� ����ְ� ��ǥ�� �����ϴ� ��Ȳ�̸� ��ǥ�� �����Ѵ�.
	   			
	   		}
	   		return 100;
	   	}
	   	
	   	
			if (is_imgubun.equals("�Է�")) {
				//Insert��
				try {
					String ls_max_reg_seq = f_make_reg_seq(gs_reg_no);
					
					ls_insert =
							"INSERT INTO LB_INSP_DETAIL" +
							"           (REG_NO,     REG_SEQ,   REG_SKEY,      REG_DATE,    REG_CODE," +
							"            MAKE_CODE,  FACT_CODE, FACT_SUB_CODE, LINE_CODE,   FACT_LINE_SEQ,  EMP_CODE," +
							"            OP_NO,      MACN_CODE, MACN_NAME,     MACN_MODEL,  MACN_MAKE_CODE, MACN_MAKE_NAME, MAKER_CODE, BAR_CODE," +
							"            ITEM_CODE,  ITEM_SPEC, JATA_FLAG,     BAR_LA_CODE, MATCH_FLAG, MAKE_DATE," +
							"            STATE_FLAG, CHEK_FLAG, CASE_TEXT,     CHEK_TEXT,   QTY,            BIGO)" +
			            "    VALUES ('" + gs_reg_no + "', " + 
			                        "'" + ls_max_reg_seq + "', " + 
			                        "'" + ls_max_reg_seq + "', " + 
			                        "'" + gs_reg_date + "', " + 
			                        "'" + "0001" + "', " + 
		                                 
	                              "'" + gs_make_code + "', " + 
	                              "'" + gs_fact_code + "', " + 
	                              "'" + gs_fact_sub_code + "', " + 
	                              "'" + is_line_code + "', " + 
	                              "'" + is_fact_line_seq + "', " + 
	                              "'" + gs_emp_code + "', " +
	                              
	                              "'" + Util.f_replace(is_op_no, "'", "''") + "', " +
	                              "'" + is_macn_code + "', " +
	                              "'" + Util.f_replace(is_macn_name, "'", "''") + "', " +
	                              "'" + Util.f_replace(is_macn_model, "'", "''") + "', " +
	                              "'" + is_macn_make_code + "', " +
	                              "'" + Util.f_replace(is_macn_make_name, "'", "''") + "', " +
	                              "'" + is_maker_code + "', " +
	                              "'" + Util.f_replace(is_bar_code, "'", "''") + "', " +
	                              
	                              "'" + is_item_code + "', " +
	                              "'" + Util.f_replace(is_item_spec, "'", "''") + "', " +
	                              "'" + Util.f_replace(is_jata_flag, "'", "''") + "', " +
	                              "'" + is_bar_la_code + "', " +
	                              "'" + Util.f_replace(is_match_flag, "'", "''") + "', " +
	                              "'" + is_make_date + "', " +
	                              
	                              "'" + Util.f_replace(is_state_flag, "'", "''") + "', " +
	                              "'" + Util.f_replace(is_chek_flag, "'", "''") + "', " +
	                              "'" + Util.f_replace(is_case_text, "'", "''") + "', " +
	                              "'" + Util.f_replace(is_chek_text, "'", "''") + "', " +
	                                    ii_qty + ", " +
	                              "'" + Util.f_replace(is_bigo, "'", "''") + "')";
					
					
					
					DBH.DB.execSQL(ls_insert);
					f_retrieve(gs_reg_no, ls_max_reg_seq);
					
				} catch (Exception e) {
		 			Log.e(TAG, "���˳���(������) �߰� : " + ls_insert);
		 			Util.f_dialog(Insp_Input.this,"���˳���(������)�� �����߿� ������ �߻��Ͽ����ϴ�.");
					return -1;
				}
	
			} else {
				//Update��
				try {
					ls_update =
							"UPDATE LB_INSP_DETAIL " +
							"   SET LINE_CODE = '" + is_line_code + "'," +
							"       FACT_LINE_SEQ = '" + is_fact_line_seq + "'," +
							"       EMP_CODE = '" + gs_emp_code + "'," +
							
							"       OP_NO = '" + Util.f_replace(is_op_no, "'", "''") + "'," +
							"       MACN_CODE = '" + is_macn_code + "'," +
							"       MACN_NAME = '" + Util.f_replace(is_macn_name, "'", "''") + "'," +
							"       MACN_MODEL = '" + Util.f_replace(is_macn_model, "'", "''") + "'," +
							"       MACN_MAKE_CODE = '" + is_macn_make_code + "'," +
							"       MACN_MAKE_NAME = '" + Util.f_replace(is_macn_make_name, "'", "''") + "'," +
							"       MAKER_CODE = '" + is_maker_code + "'," +
							"       BAR_CODE = '" + is_bar_code + "'," +
							
							"       ITEM_CODE = '" + is_item_code + "'," +
							"       ITEM_SPEC = '" + Util.f_replace(is_item_spec, "'", "''") + "'," +
							"       JATA_FLAG = '" + Util.f_replace(is_jata_flag, "'", "''") + "'," +
							"       BAR_LA_CODE = '" + is_bar_la_code + "'," +
							"       MATCH_FLAG = '" + Util.f_replace(is_match_flag, "'", "''") + "'," +
							"       MAKE_DATE = '" + is_make_date + "'," +
							
							"       STATE_FLAG = '" + Util.f_replace(is_state_flag, "'", "''") + "'," +
							"       CHEK_FLAG = '" + Util.f_replace(is_chek_flag, "'", "''") + "'," +
							"       CASE_TEXT = '" + Util.f_replace(is_case_text, "'", "''") + "'," +
							"       CHEK_TEXT = '" + Util.f_replace(is_chek_text, "'", "''") + "'," +
							"       QTY = " + ii_qty + "," +
							"       BIGO = '" + Util.f_replace(is_bigo, "'", "''") + "'" +
							" WHERE REG_NO = '" + is_reg_no + "'" +
							"   AND REG_SEQ = '" + is_reg_seq + "'";
			 		DBH.DB.execSQL(ls_update);
				} catch (Exception e) {
		 			Log.e(TAG, "���˳���(������) ���� : " + ls_update);
		 			Util.f_dialog(Insp_Input.this,"���˳���(������)�� �����߿� ������ �߻��Ͽ����ϴ�.");
					return -1;
				}
	
			}
	   	
	   	return 0;
		} finally {
			Message msg = handler.obtainMessage();
			msg.obj = "save";
			handler.sendMessage(msg);
		}
   	
   }
   
	public String f_make_reg_seq(String as_reg_no) {
		try {
			curSelect = DBH.DB.rawQuery("SELECT MAX(REG_SEQ) REG_SEQ FROM LB_INSP_DETAIL WHERE REG_NO = '" + as_reg_no + "'", null);
			curSelect.moveToNext();
			String ls_reg_seq = curSelect.getString(curSelect.getColumnIndex("REG_SEQ"));
			curSelect.close();

			if (Util.f_isnull(ls_reg_seq)) {
				ls_reg_seq = "0001";
			} else {
				ls_reg_seq = String.format("%04d", Integer.parseInt(ls_reg_seq) + 1);
			}
			
			return ls_reg_seq;
		} catch (Exception e) {
 			Log.e(TAG, "���˳���(������) MAX REG_SEQ");
 			Util.f_dialog(Insp_Input.this,"���˳���(������)�� ��ȸ�߿� ������ �߻��Ͽ����ϴ�.");
			return "";
		}
	}

	
	public void f_delete_insp_detail() {
		pd = ProgressDialog.show(Insp_Input.this, "","���˳����� �����ϰ� �ֽ��ϴ�.\n��ø� ��ٷ� �ֽʽÿ�!", true);
		handler.postDelayed(new Runnable () {public void run() {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					f_delete_insp_detail_run();
									
					Message msg = handler.obtainMessage();
					msg.obj = "delete";
					handler.sendMessage(msg);
				}
			});
			thread.start();
		}}, 300);
	}
	
	public int f_delete_insp_detail_run() {
		String ls_delete = null;

			if ((Util.f_isnull(is_reg_no)) == true) return 100;
	
			try {
				DBH.DB.beginTransaction();
				ls_delete =	"DELETE FROM LB_INSP_DETAIL WHERE REG_NO = '" + is_reg_no + "' AND REG_SEQ = '" + is_reg_seq + "'";
		 		DBH.DB.execSQL(ls_delete);
		 		ls_delete =	"DELETE FROM LB_INSP_CASE_CHEK WHERE REG_NO = '" + is_reg_no + "' AND REG_SEQ = '" + is_reg_seq + "'";
		 		DBH.DB.execSQL(ls_delete);
		 		DBH.DB.setTransactionSuccessful();  //commit;
			} catch (Exception e) {
	 			Log.e(TAG, "���˳���(������) ���� : " + ls_delete);
	 			Util.f_dialog(Insp_Input.this,"���˳���(������)�� �����߿� ������ �߻��Ͽ����ϴ�.");
				return -1;
			} finally {
	 			DBH.DB.endTransaction();
			}
			
			return 0;
	}

	
	public Handler handler = new Handler() {
      public void handleMessage(Message msg) {
			
			if (msg.obj.equals("retrieve")) {
				f_retrieve_handle();
			} else if (msg.obj.equals("save")) {
				ib_search.setEnabled(true);
			} else if (msg.obj.equals("delete")) {
      		if (((Util.f_isnull(is_bar_code)) == false) && ((Util.f_isnull(is_item_spec)) == false))
      			Toast.makeText(getBaseContext(), getString(R.string.bar_code) + " \"" + is_bar_code + "\", " + getString(R.string.item_spec) + " \"" + is_item_spec + "\"�� �����߽��ϴ�.", Toast.LENGTH_SHORT).show();
      		else if (((Util.f_isnull(is_bar_code)) == false) && ((Util.f_isnull(is_item_spec)) == true))
      			Toast.makeText(getBaseContext(), getString(R.string.bar_code) + " \"" + is_bar_code + "\"�� �����߽��ϴ�.", Toast.LENGTH_SHORT).show();
      		else if (((Util.f_isnull(is_bar_code)) == true) && ((Util.f_isnull(is_item_spec)) == false))
      			Toast.makeText(getBaseContext(), getString(R.string.item_spec) + " \"" + is_item_spec + "\"�� �����߽��ϴ�.", Toast.LENGTH_SHORT).show();
      		else if (((Util.f_isnull(is_reg_no)) == false))
      			Toast.makeText(getBaseContext(), "���˳����� �����߽��ϴ�.", Toast.LENGTH_SHORT).show();
        		else if (((Util.f_isnull(is_bar_code)) == true) && ((Util.f_isnull(is_item_spec)) == true))
        			Toast.makeText(getBaseContext(), "������ ����Ÿ�� �������� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
      		
      		f_reset();  //ȭ�� �ʱ�ȭ
      		iet_search.setText("");
      		if (is_imgubun.equals("�Է�") == false) is_imgubun = "�Է�";
      		f_set_title();
      		findViewById(R.id.b_delete).setEnabled(true);
      	}

      	if ( pd != null ) pd.hide();
      	iet_search.setSelection(0, iet_search.getText().length() );
			iet_search.requestFocus();  //��Ŀ���� �ش�.

      }
	};	

	public void onBackPressed() {
		finish();
		
	}
	
}

