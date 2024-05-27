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
	protected static final int CAMERA = 0;  //카메라 실행시 리턴받는 상수
	
	Insp_History_LIstView listView;
	Insp_History_ListAdapter adapter, adapter_null;

	EditText iet_search;
	
	//타이틀 컨트롤
	ProgressBar ipbtitle;
	TextView    itvtitle;
	
	
	String is_imgubun;  //입력, 수정구분


	String is_reg_no,     is_reg_seq,   is_reg_skey,      is_reg_date,    is_reg_code;
	String is_make_code,  is_fact_code, is_fact_sub_code, is_line_code,   is_fact_line_seq,  is_emp_code;
	String is_op_no,      is_macn_code, is_macn_name,     is_macn_model,  is_macn_make_code, is_macn_make_name, is_maker_code, is_bar_code;
	String is_item_code,  is_item_spec, is_jata_flag,     is_bar_la_code, is_match_flag, is_make_date;
	String is_state_flag, is_chek_flag, is_case_text,     is_chek_text,   is_bigo;
	int    ii_qty;
	
	//f_retrieve에서만 사용
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
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
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
		ib_search = (Button) findViewById(R.id.b_search);   	ib_search.setOnClickListener(this);    //조회
		
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
         
		is_reg_no =  bundle.getString("reg_no"); //전표번호
		is_reg_seq = bundle.getString("reg_seq"); //전표SEQ
		if (Util.f_isnull(is_reg_no)) {
			is_imgubun = "입력";
		} else {
			is_imgubun = "수정";
		}
		
		//--->>>리스트뷰--->>>
		adapter = new Insp_History_ListAdapter(this);
		listView = new Insp_History_LIstView(this);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayoutDrugList );
		linLayout.addView(listView);

		iet_search = (EditText) findViewById(R.id.et_search);
		iet_search.requestFocus();  //포커스를 준다.
		
		
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

		// 엔터를 눌렀을시
		iet_search.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				
				if((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
					onClick(ib_search);
					
					iet_search.setSelection(0, iet_search.getText().length() );
					iet_search.requestFocus();  //포커스를 준다.
					return true;
				}
				
				return false;
			}
		});

		//삭제
		findViewById(R.id.b_delete).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				findViewById(R.id.b_delete).setEnabled(false);
				f_delete_insp_detail();
				
				iet_search.setSelection(0, iet_search.getText().length() );
				iet_search.requestFocus();  //포커스를 준다.
			}
		});

		//녹음
		findViewById(R.id.b_rec).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final Intent pintent = Insp_Input.this.getPackageManager().getLaunchIntentForPackage("de.fun2code.android.voicerecord");
		      try{
		      	Insp_Input.this.startActivity(pintent);
		      } catch (Exception e) {
					Log.e(TAG, "no install rec");
					Util.f_dialog(Insp_Input.this,"녹음앱을 설치하십시오!");
				}
		      
			}
		});

		//사진
		findViewById(R.id.b_pic).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//카메라 프로그램을 실행한다.
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent,CAMERA);
				
				iet_search.setSelection(0, iet_search.getText().length() );
				iet_search.requestFocus();  //포커스를 준다.
			}
		});

		//종료
		findViewById(R.id.b_close).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		if (gs_mobi_flag.equals("0")) {  //조회용이면
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
		iet_search.requestFocus();  //포커스를 준다.
		
	};

	public void f_set_title() {
		String ls_mobi_flag_text, ls_reg_seq_text;
		
		if (gs_mobi_flag.equals("1")) ls_mobi_flag_text = ""; else ls_mobi_flag_text = " [조회전용]";
			
		
		if (Util.f_isnull(is_reg_seq)) ls_reg_seq_text = ""; else ls_reg_seq_text = " [순번:" + is_reg_seq + "]";
		
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
		
		is_state_flag     = curSelect.getString(curSelect.getColumnIndex("STATE_FLAG"));  //상태 0:X, 1:0
		is_chek_flag      = curSelect.getString(curSelect.getColumnIndex("CHEK_FLAG"));   //확인요청
		is_case_text      = curSelect.getString(curSelect.getColumnIndex("CASE_TEXT"));
		is_chek_text      = curSelect.getString(curSelect.getColumnIndex("CHEK_TEXT"));
		is_bigo           = curSelect.getString(curSelect.getColumnIndex("BIGO"));

		
		
		ii_qty = 1;  //QTY는 그냥 1로 설정해준다.
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
					"          CASE IFNULL(D.JATA_FLAG, '0') WHEN '0' THEN '타사' WHEN '1' THEN '자사' END JATA_FLAG_NAME" +
		         "     FROM (((" +
					"          LB_INSP_DETAIL D LEFT OUTER JOIN LA_LINE LINE ON D.LINE_CODE = LINE.LINE_CODE)" +
		         "                           LEFT OUTER JOIN LA_MAKER MAKER ON D.MAKER_CODE = MAKER.MAKER_CODE) " +
		         "                           LEFT OUTER JOIN LA_BAR_LA BAR_LA ON D.BAR_LA_CODE = BAR_LA.BAR_LA_CODE) " +
					"    WHERE D.REG_NO = '" + as_reg_no + "'" +
					"      AND D.REG_SEQ = '" + as_reg_seq + "'", null);
			if (curSelect.getCount() <= 0) {
				Log.e(TAG, "f_retrieve-알수없는 오류가 발생하였습니다.");
				Util.f_dialog(Insp_Input.this,"알수없는 오류가 발생하였습니다.");
				return;
			}

			curSelect.moveToNext();
			
			f_get_lb_insp_detail(curSelect);  //LB_INSP_DETAIL의 내용을 인스턴스 변수에 담는다.

			curSelect.close();

			Message msg = handler.obtainMessage(); msg.obj = "retrieve"; handler.sendMessage(msg);
			
			if (is_imgubun.equals("수정") == false) is_imgubun = "수정";
			
		} catch (Exception e) {
			Log.e(TAG, "f_retrieve");
			Util.f_dialog(Insp_Input.this,"전표를 불러오는중에 오류가 발생하였습니다.");
			return;
		}
	}	
	
	//쓰레드때문에 별도 펑션으로 뺏음
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
				if (is_imgubun.equals("입력"))  f_reset();  //입력모드만 리셋
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
					"          CASE IFNULL(D.JATA_FLAG, '0') WHEN '0' THEN '타사' WHEN '1' THEN '자사' END JATA_FLAG_NAME" +
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
				
				if (is_imgubun.equals("입력"))  f_reset();  //입력모드만 리셋
				
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
				
				itv_chek_text_his.setText(getString(R.string.tv_chek_text_his_exists1) + String.format("%,d", li_count) + getString(R.string.tv_chek_text_his_exists2));  //"☞ 이력 총 ", "건"
				
				
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
					
					
					if (is_imgubun.equals("입력")) {  //입력모드만 가장 최신의 시리얼 번호를 채워준다. 
						if (i == 0) {  //조회용이면 가장 최신의 시리얼번호를 체워준다.
						//if ((i == 0) && !(Util.f_isnull(iet_bar_code.getText().toString()))) {  //조회용이면 가장 최신의 시리얼번호를 체워준다.
							f_get_lb_insp_detail(curSelect);  //LB_INSP_DETAIL의 내용을 인스턴스 변수에 담는다.
							if (ls_reg_no.equals(gs_reg_no)) {  //가장 최근의 데이타가 현전표에 있으면 현전표의 목록을 가져오고 없으면 입력모드로 채운다
								if (is_imgubun.equals("수정") == false) is_imgubun = "수정";
								
								f_retrieve_handle();  //쓰레드때문에 별도 펑션으로 뺏음
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
								
								//데이타를 가져온후 가져올 필요가 없는 데이타 초기화
								is_state_flag     = "1"; //상태 0:X, 1:0
								is_chek_flag      = "0"; //확인요청
								is_case_text      = "";
								is_chek_text      = "";
								is_bigo           = "";
	
								iet_bigo.setText(is_bigo);
								iet_chek_text.setText(is_chek_text);
	
								//입력전용이기때문에 수정모드일경우 입력모드로 변경
								is_reg_no = "";
								is_reg_seq = "";
							}
						}
					}
					
					if (Util.f_isnull(ls_chek_text)) ls_chek_text = "없음";
					adapter.addItem(new Insp_History_Item(ls_reg_no, ls_reg_seq, Util.getFormatDate(ls_reg_date), ls_chek_text));
				}
			}
			curSelect.close();

			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();  //변경사항을 refalsh
			
		} catch (Exception e) {
			Log.e(TAG, "f_retrieve");
			Util.f_dialog(Insp_Input.this,"이력정보를 불러오는중에 오류가 발생하였습니다.");
		}
		
	}	

	public void f_reset() {
		itv_chek_text_his.setText(getString(R.string.tv_chek_text_his));  //"☞ 이력없음"
		
		
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
		iet_jata_flag_name.setText("타사"); //존재하지 않을시 자타구분은 Default Value로 타사 설정
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
		adapter.notifyDataSetChanged();  //변경사항을 refalsh
		
	}
	
	static EditText edit = null;

	
	public void onClick(View v) {
		if(v.equals(findViewById(R.id.b_search))) {  //확인
			ib_search.setEnabled(false);
			//입력모드로 변경
			if (is_imgubun.equals("입력") == false) is_imgubun = "입력";
	
			f_history(iet_search.getText().toString());
			f_set_title();

			is_bar_code = iet_search.getText().toString();
			iet_bar_code.setText(is_bar_code);
			
			iet_search.setFocusable(true);
			iet_search.setSelection(0, iet_search.getText().length() );
			iet_search.requestFocus();  //포커스를 준다.
			
			//조회후 키보드 감춤
			InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mInputMethodManager.hideSoftInputFromWindow(iet_search.getWindowToken(), 0);
			
			f_save_insp_detail();
			
			iet_search.setFocusable(true);
			iet_search.setSelection(0, iet_search.getText().length() );
			
		} else if (v.equals(findViewById(R.id.et_item_spec))) {  //규격컬럼
			/*
			Dialog_sle dialog = new Dialog_sle( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "item_spec";
			final EditText let = (EditText) dialog.findViewById(R.id.et);
			
			let.setText(iet_item_spec.getText().toString());
			let.setSelection(0, let.getText().length() );
			
			//다이얼로그창 오픈후에 바로 키보드를 보이게 함
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

			//다이얼로그창 오픈후에 키보드를 감춤
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.hideSoftInputFromWindow(let.getWindowToken(), 0);

				}
			}, 1);

			
		} else if (v.equals(findViewById(R.id.et_line_name))) {  //작업라인
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

			//다이얼로그창 오픈후에 키보드를 감춤
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
			
			//다이얼로그창 오픈후에 바로 키보드를 보이게 함
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.showSoftInput(let, InputMethodManager.SHOW_FORCED);
				}
			}, 1);
		} else if (v.equals(findViewById(R.id.et_macn_name))) {  //판넬
			Dialog_sle dialog = new Dialog_sle( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "macn";
			final EditText let = (EditText) dialog.findViewById(R.id.et);
			final TextView ltv = (TextView) dialog.findViewById(R.id.tv);
			
			ltv.setText(getString(R.string.macn_name_)); //머신명 :
			let.setText(iet_macn_name.getText().toString());
			let.setSelection(0, let.getText().length() );
			
			//다이얼로그창 오픈후에 바로 키보드를 보이게 함
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.showSoftInput(let, InputMethodManager.SHOW_FORCED);
				}
			}, 1);
			
		} else if (v.equals(findViewById(R.id.et_macn_model))) {  //머신
			Dialog_sle dialog = new Dialog_sle( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "model";
			final EditText let = (EditText) dialog.findViewById(R.id.et);
			final TextView ltv = (TextView) dialog.findViewById(R.id.tv);
			
			ltv.setText(getString(R.string.macn_model_)); //머신명 :
			let.setText(iet_macn_model.getText().toString());
			let.setSelection(0, let.getText().length() );
			
			//다이얼로그창 오픈후에 바로 키보드를 보이게 함
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.showSoftInput(let, InputMethodManager.SHOW_FORCED);
				}
			}, 1);
			
		} else if (v.equals(findViewById(R.id.et_maker_name))) {  //메이커
			Dialog_Maker dialog = new Dialog_Maker( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "maker";
			
			
			final EditText let = (EditText) dialog.findViewById(R.id.et_filter);
			//let.setText(iet_bigo.getText().toString());
			//let.setSelection(0, let.getText().length() );

			//다이얼로그창 오픈후에 키보드를 감춤
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.hideSoftInputFromWindow(let.getWindowToken(), 0);

				}
			}, 1);
		} else if (v.equals(findViewById(R.id.et_jata_flag_name))) {  //자타구분
			Dialog_Jata dialog = new Dialog_Jata( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "jata";
		} else if (v.equals(findViewById(R.id.et_bar_la_name))) {  //제품구분
			Dialog_bar_la dialog = new Dialog_bar_la( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "bar_la";
			
			
			final EditText let = (EditText) dialog.findViewById(R.id.et_filter);
			//let.setText(iet_bigo.getText().toString());
			//let.setSelection(0, let.getText().length() );

			//다이얼로그창 오픈후에 키보드를 감춤
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.hideSoftInputFromWindow(let.getWindowToken(), 0);

				}
			}, 1);
		} else if (v.equals(findViewById(R.id.et_bigo))) {  //비고
			Dialog_mle dialog = new Dialog_mle( this ) ;  
			dialog.setOnDismissListener( this ) ;  
			dialog.show();
			is_onDismiss = "bigo";
			final EditText let = (EditText) dialog.findViewById(R.id.et);
			let.setText(iet_bigo.getText().toString());
			let.setSelection(0, let.getText().length() );

			
			//다이얼로그창 오픈후에 바로 키보드를 보이게 함
			new Handler().postDelayed(new Runnable(){
				public void run() {
					InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mInputMethodManager.showSoftInput(let, InputMethodManager.SHOW_FORCED);
				}
			}, 1);
			
			
			
		} else if (v.equals(findViewById(R.id.et_chek_text))) {  //조치사항
			
			if (Util.f_isnull(is_reg_no)) {
				Util.f_dialog(Insp_Input.this,"\"" + getString(R.string.bar_code) + "\" 또는 \"" + getString(R.string.item_spec) + "\"을 먼저 입력후 조치사항을 입력하실 수 있습니다.");
			} else {
				Bundle bundle = new Bundle();
				
				bundle.putString("reg_no", is_reg_no);    //전표번호
				bundle.putString("reg_seq", is_reg_seq);  //전표SEQ
				bundle.putString("reg_date", is_reg_date);  //전표일자
				bundle.putString("reg_code", "0001");  //전표CODE
				
				Intent linsp_case_chek = new Intent(getBaseContext(), Insp_Case_Chek.class);
				linsp_case_chek.putExtras(bundle);
				startActivityForResult(linsp_case_chek, 1);
			}
			
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK) { // 액티비티가 정상적으로 종료되었을 경우
			if(requestCode==1) {
				is_case_text = data.getStringExtra("case_text");
				is_chek_text = data.getStringExtra("chek_text");
				is_state_flag = data.getStringExtra("state_flag");   //0.X 1.O
				
				iet_chek_text.setText(is_chek_text);
				
				f_save_insp_detail();
			}
		}
		
		iet_search.setSelection(0, iet_search.getText().length() );
		iet_search.requestFocus();  //포커스를 준다.

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
   	} else if (is_onDismiss.equals("line")) {  //작업라인
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
   	} else if (is_onDismiss.equals("macn")) {  //판넬
   		Dialog_sle dialog = (Dialog_sle) ad_dialog ;
   		
   		is_macn_name = dialog.getName();
   		iet_macn_name.setText(is_macn_name);
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("model")) {  //머신
   		Dialog_sle dialog = (Dialog_sle) ad_dialog ;
   		
   		is_macn_model = dialog.getName();
   		iet_macn_model.setText(is_macn_model);
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("maker")) {  //메이커
   		Dialog_Maker dialog = (Dialog_Maker) ad_dialog ;
   		
   		is_maker_code = dialog.is_maker_code;
   		is_maker_name = dialog.is_maker_name;
   		iet_maker_name.setText(is_maker_name) ;
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("jata")) {  //자타구분
   		Dialog_Jata dialog = (Dialog_Jata) ad_dialog ;
   		
   		is_jata_flag = dialog.is_jata_flag;
   		is_jata_flag_name = dialog.is_jata_flag_name;
   		iet_jata_flag_name.setText(is_jata_flag_name) ;
   		
   		f_save_insp_detail();
   	} else if (is_onDismiss.equals("bar_la")) {  //제품구분
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
		iet_search.requestFocus();  //포커스를 준다.

   }

	public void f_save_insp_detail() {
		if (gs_mobi_flag.equals("0")) {
			Util.f_dialog(Insp_Input.this,"조회전용 모드에서는 저장하실 수 없습니다.");
			return;
		}
		
		final String ls_imgubun = is_imgubun;
		if (ls_imgubun.equals("입력")) 
			pd = ProgressDialog.show(Insp_Input.this, "","점검내역을 추가하고 있습니다.\n잠시만 기다려 주십시오!", true);
		else
			pd = ProgressDialog.show(Insp_Input.this, "","점검내역을 저장하고 있습니다.\n잠시만 기다려 주십시오!", true);
		
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
	   	
	   	//시리얼번호 Or 규격 Or 비고 Or 라인 Or OP/NO Or 머신 Or 메이커가 모두 채워져 있지 않으면 저장하지 않아도 된다.
	   	if ((Util.f_isnull(is_bar_code)) && (Util.f_isnull(is_item_spec)) && (Util.f_isnull(is_bigo)) && (Util.f_isnull(is_line_code)) && (Util.f_isnull(is_op_no)) && (Util.f_isnull(is_macn_name)) && (Util.f_isnull(is_macn_model)) && (Util.f_isnull(is_maker_code)) && (Util.f_isnull(is_bar_la_code))) {
	   		if ((Util.f_isnull(is_reg_no)) == false) {
	   			f_delete_insp_detail_run();  //항목 모두가 비어있고 전표가 존재하는 상황이면 전표를 삭제한다.
	   			
	   		}
	   		return 100;
	   	}
	   	
	   	
			if (is_imgubun.equals("입력")) {
				//Insert문
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
		 			Log.e(TAG, "점검내역(디테일) 추가 : " + ls_insert);
		 			Util.f_dialog(Insp_Input.this,"점검내역(디테일)을 생성중에 오류가 발생하였습니다.");
					return -1;
				}
	
			} else {
				//Update문
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
		 			Log.e(TAG, "점검내역(디테일) 수정 : " + ls_update);
		 			Util.f_dialog(Insp_Input.this,"점검내역(디테일)을 저장중에 오류가 발생하였습니다.");
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
 			Log.e(TAG, "점검내역(디테일) MAX REG_SEQ");
 			Util.f_dialog(Insp_Input.this,"점검내역(디테일)를 조회중에 오류가 발생하였습니다.");
			return "";
		}
	}

	
	public void f_delete_insp_detail() {
		pd = ProgressDialog.show(Insp_Input.this, "","점검내역을 삭제하고 있습니다.\n잠시만 기다려 주십시오!", true);
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
	 			Log.e(TAG, "점검내역(디테일) 삭제 : " + ls_delete);
	 			Util.f_dialog(Insp_Input.this,"점검내역(디테일)을 삭제중에 오류가 발생하였습니다.");
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
      			Toast.makeText(getBaseContext(), getString(R.string.bar_code) + " \"" + is_bar_code + "\", " + getString(R.string.item_spec) + " \"" + is_item_spec + "\"을 삭제했습니다.", Toast.LENGTH_SHORT).show();
      		else if (((Util.f_isnull(is_bar_code)) == false) && ((Util.f_isnull(is_item_spec)) == true))
      			Toast.makeText(getBaseContext(), getString(R.string.bar_code) + " \"" + is_bar_code + "\"를 삭제했습니다.", Toast.LENGTH_SHORT).show();
      		else if (((Util.f_isnull(is_bar_code)) == true) && ((Util.f_isnull(is_item_spec)) == false))
      			Toast.makeText(getBaseContext(), getString(R.string.item_spec) + " \"" + is_item_spec + "\"을 삭제했습니다.", Toast.LENGTH_SHORT).show();
      		else if (((Util.f_isnull(is_reg_no)) == false))
      			Toast.makeText(getBaseContext(), "점검내역을 삭제했습니다.", Toast.LENGTH_SHORT).show();
        		else if (((Util.f_isnull(is_bar_code)) == true) && ((Util.f_isnull(is_item_spec)) == true))
        			Toast.makeText(getBaseContext(), "삭제할 데이타가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
      		
      		f_reset();  //화면 초기화
      		iet_search.setText("");
      		if (is_imgubun.equals("입력") == false) is_imgubun = "입력";
      		f_set_title();
      		findViewById(R.id.b_delete).setEnabled(true);
      	}

      	if ( pd != null ) pd.hide();
      	iet_search.setSelection(0, iet_search.getText().length() );
			iet_search.requestFocus();  //포커스를 준다.

      }
	};	

	public void onBackPressed() {
		finish();
		
	}
	
}

