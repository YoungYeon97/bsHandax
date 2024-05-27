package woodnsoft.bsHandax;

import java.util.Timer;
import java.util.TimerTask;

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
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class Insp_Search extends DBActivity implements OnClickListener {
	
	//private static final String TAG = "Insp_Search"; 
	Insp_Search_LIstView listView;
	Insp_Search_ListAdapter adapter;
	
	//프로그래스 쓰레드 관련
	ProgressHandler handler;

	//타이틀 컨트롤
	ProgressBar ipbtitle;
	TextView    itvtitle;
	
	boolean btBackState = false; // 뒤로가기 버튼의 상태값을 갖는 변수
	Timer timer = new Timer(); // 타이머 변수
	
	TextView itv_result;
	
	EditText iet_search;
	Button ib_search;  //조회
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insp_search);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		handler = new ProgressHandler();  //프로그래스 쓰레드
		ipbtitle = (ProgressBar) findViewById(R.id.pbtitle);
		itvtitle = (TextView) findViewById(R.id.tvtitle);

		itvtitle.setText("BS 조회 / " + Util.getFormatDate(gs_reg_date) + " [" + gs_reg_no.substring(4, 8) + "-" + gs_reg_no.substring(8) + "]" + f_title_mobi_flag());
		
		//--->>>리스트뷰--->>>
		adapter = new Insp_Search_ListAdapter(this);
		listView = new Insp_Search_LIstView(this);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayoutDrugList );
		linLayout.addView(listView);
		
		iet_search = (EditText) findViewById(R.id.et_search);
		iet_search.setPrivateImeOptions("defaultInputmode=english;");  //기본값을 영문으로
		//iet_search.requestFocus();  //포커스를 준다.
		
		
		ib_search = (Button) findViewById(R.id.b_search);  //조회
		ib_search.setOnClickListener(this);

		itv_result = (TextView) findViewById(R.id.tv_result);

		Button lb_input = (Button) findViewById(R.id.b_input);  //입력
		lb_input.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
   			Bundle bundle = new Bundle();
   			bundle.putString("data0", "");  //시리얼번호
				
				Intent linsp_input = new Intent(getBaseContext(), Insp_Input.class);
				linsp_input.putExtras(bundle);
				startActivity(linsp_input);
			}
		});
		
		if (gs_mobi_flag.equals("0")) {  //조회용이면
			lb_input.setEnabled(false);
		}

		
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
					return true;
				}
				
				return false;
			}
		});

		listView.setOnDataSelectionListener( new OnDataSelectionListener () {
         
   		public void onDataSelected(AdapterView<?> parent, View v, int position, long id) {
   			// make intent
   			
   			Insp_Search_Item selectItem = (Insp_Search_Item)adapter.getItem(position);
                
   			//Toast.makeText(getBaseContext(), selectItem.getData(0), Toast.LENGTH_SHORT).show();
   			    
   			Bundle bundle = new Bundle();
   			
   			bundle.putString("reg_no", selectItem.getData(1));  //전표번호
   			bundle.putString("reg_seq", selectItem.getData(2));  //전표SEQ
   			
   			
				Intent linsp_input = new Intent(getBaseContext(), Insp_Input.class);
				linsp_input.putExtras(bundle);
				startActivity(linsp_input);
                
   		}
		});       		
		

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		onClick(ib_search);

		//조회후 키보드 감춤
		InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mInputMethodManager.hideSoftInputFromWindow(iet_search.getWindowToken(), 0);

		
	}
	
	public void fGetList() {
		try
		{
			String ls_reg_seq, ls_bar_code, ls_item_spec, ls_jata_flag, ls_line_name, ls_macn_name, ls_state_flag, ls_bigo, ls_op_no;
			
			String ls_search = iet_search.getText().toString();
			int li_state_flag0 = 0, li_state_flag1 = 0;
			
			if (Util.f_isnull(ls_search)) ls_search = "%"; else ls_search = "%" + ls_search + "%";
			 
			adapter.clear();
			Cursor curSelect;
			curSelect = DBH.DB.rawQuery(
			      "   SELECT D.REG_SEQ, D.BAR_CODE, D.ITEM_SPEC, D.JATA_FLAG, LINE.LINE_NAME, D.MACN_NAME, D.STATE_FLAG, D.BIGO, D.OP_NO" +
	            "     FROM LB_INSP_DETAIL D LEFT OUTER JOIN LA_LINE LINE ON D.LINE_CODE = LINE.LINE_CODE" +
	            "    WHERE D.REG_NO = '" + gs_reg_no + "'" +
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
				ls_state_flag = Util.f_nvl(curSelect.getString(curSelect.getColumnIndex("STATE_FLAG")), "1");
				if (ls_state_flag.equals("0")) li_state_flag0 ++;
				else li_state_flag1 ++;
				ls_bigo       = curSelect.getString(curSelect.getColumnIndex("BIGO"));
				ls_op_no      = curSelect.getString(curSelect.getColumnIndex("OP_NO"));
				
				fAddAdapter(gs_mobi_flag, gs_reg_no, ls_reg_seq, ls_bar_code, ls_item_spec, ls_jata_flag, ls_line_name, ls_macn_name, ls_state_flag, ls_bigo, ls_op_no);
			}
			curSelect.close();
			
			//Toast.makeText(getBaseContext(), "총 " + String.format("%,d", li_count) + "건 검색되었습니다.", Toast.LENGTH_SHORT).show();
			if (li_count <= 0) itv_result.setText("검색된 데이타가 존재하지 않습니다."); 
			else {
				itv_result.setText("총 " + String.format("%,d", li_count) + "건 검색되었습니다.\n" +
						             "(상태 X: " + String.format("%,d", li_state_flag0) + "건, 상태 O: " + String.format("%,d", li_state_flag1) + "건)");
			}

			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();  //변경사항을 refalsh
			
			//<<<---리스트뷰<<<---

		} finally {
 			if ( pd_bef != null ) pd_bef.hide();
		}
		
	}

	//                      모바일수정유무,      전표번호,            전표SEQ,
	public void fAddAdapter(String as_mobi_flag, String as_reg_no,    String as_reg_seq, 
	//                      시리얼번호,          규격,
			                  String as_bar_code,  String as_item_spec,
	//                      자타구분,            작업라인,            머신명,              상태,                 비고,           OP/NO
			                  String as_jata_flag, String as_line_name, String as_macn_name, String as_state_flag, String as_bigo, String as_op_no) {

		if (Util.f_isnull(as_bar_code)) as_bar_code = "-----없음-----";
		if (as_item_spec == null) as_item_spec = "";
		if (as_jata_flag == null) as_jata_flag = "";
		if (as_line_name == null) as_line_name = "";
		if (as_macn_name == null) as_macn_name = "";
		if (as_state_flag == null) as_state_flag = "";
		if (as_bigo == null) as_bigo = "";
		if (as_op_no == null) as_op_no = "";
		
		//자타구분(1.자사, 0.타사)
		if (as_jata_flag.equals("1")) as_jata_flag = "자사"; else as_jata_flag = "타사";

		//상태(0.X 1.O)
		if (as_state_flag.equals("0")) as_state_flag = "X"; else as_state_flag = "O";
		
		adapter.addItem(new Insp_Search_Item(gs_mobi_flag, gs_reg_no, as_reg_seq, as_bar_code, as_item_spec, as_jata_flag, as_line_name, as_macn_name, as_state_flag, as_bigo, as_op_no));
	}

	public String f_title_mobi_flag() {
		
		if (gs_mobi_flag.equals("1")) {
			return "";
		} else {
			return " [조회전용]";
		}
		
	}
	
	public void onBackPressed() {
	if(btBackState == false) { // 한 번만 누른 경우
		timer.schedule(new TimerTask() {
			public void run() {
					btBackState = false;
			}
		}, 3000);
		btBackState = true;
		Toast.makeText(getBaseContext(), "\"뒤로\"버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
	} else finish(); // 3초 내에 두 번 누른 경우
	

//		//백버튼으로 종료유무
//		new AlertDialog.Builder(Insp_Search.this)
//		.setTitle(R.string.MSG_TITLE)
//		.setIcon(R.drawable.title_icon)
//		.setMessage("BS 점검작업을 종료하시겠습니까?")
//		.setPositiveButton("예", mBack)
//		.setNegativeButton("아니요", mBack)
//		.show();	
		
	}
	
	DialogInterface.OnClickListener mClickLeft = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			if (whichButton == DialogInterface.BUTTON1) {
				finish();
			}
		}
	};	

	DialogInterface.OnClickListener mBack = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			if (whichButton == DialogInterface.BUTTON1) {
				finish();
			}
		}
	};	

	public void onClick(View v) {
		if(v.equals(findViewById(R.id.b_search))) {  //조회
			itv_result.setText("BS 점검목록을 수신중입니다..."); 
			
			pd_bef = ProgressDialog.show(Insp_Search.this, "","BS 점검목록을 수신중입니다...", true);		        	
			Handler v1 = new Handler();
			v1.postDelayed(
		    		new Runnable () {
						public void run() {
							fGetList();
						}
		    		}
		    		, 100);
			
	
			iet_search.setSelection(0, iet_search.getText().length() );
			iet_search.requestFocus();  //포커스를 준다.
			iet_search.setFocusable(true);

			//조회후 키보드 감춤
			InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mInputMethodManager.hideSoftInputFromWindow(iet_search.getWindowToken(), 0);
			
		}
	}
	
}
