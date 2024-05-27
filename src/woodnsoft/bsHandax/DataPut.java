package woodnsoft.bsHandax;

import org.json.JSONObject;

import woodnsoft.bsHandax.DataListView.DataPut.DataPut_Item;
import woodnsoft.bsHandax.DataListView.DataPut.DataPut_ListAdapter;
import woodnsoft.bsHandax.DataListView.DataPut.DataPut_ListView;
import woodnsoft.bsHandax.DataListView.DataPut.OnDataSelectionListener_DataPut;
import woodnsoft.bsHandax.common.DBActivity;
import woodnsoft.bsHandax.common.Util;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class DataPut extends DBActivity {
	
	private static final String TAG = "DataPut"; 
	DataPut_ListView listView;
	DataPut_ListAdapter adapter;
	
	//프로그래스 쓰레드 관련
	ProgressHandler handler;

	//타이틀 컨트롤
	ProgressBar ipbtitle;
	TextView    itvtitle;
	
	String[] is_arr = {"선택한 점검항목 전송", "리스트보기"};
	
	int ii_cnt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dataput);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		handler = new ProgressHandler();  //프로그래스 쓰레드
		ipbtitle = (ProgressBar) findViewById(R.id.pbtitle);
		itvtitle = (TextView) findViewById(R.id.tvtitle);

		if (f_db_conn_chk() == false) return;  //서버 DB연결 체크
		
		//--->>>리스트뷰--->>>
		adapter = new DataPut_ListAdapter(this);
		listView = new DataPut_ListView(this);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.ll_list_dataput );
		linLayout.addView(listView);
		
		TextView ltv_fact_make = (TextView) findViewById(R.id.tv_fact_make);
		if (Util.f_isnull(gs_make_code)) {
			ltv_fact_make.setText("미수신");
		} else {
			ltv_fact_make.setText(gs_make_name + " " + gs_fact_name);
		}

		pd_bef = ProgressDialog.show(DataPut.this, "","전송 데이타 작업목록을 수신중입니다...", true);		        	
		Handler v = new Handler();
		v.postDelayed(
	    		new Runnable () {
					public void run() {
						fGetList();
					}
	    		}
	    		, 1000);

		
		Button lbback = (Button) findViewById(R.id.bBack);  //뒤로
		lbback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		Button lbok = (Button) findViewById(R.id.bOk);  //실행
		lbok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ipbtitle.setVisibility(View.VISIBLE);
				ipbtitle.setProgress(0);
				
//				DataPut_Item ldgi = (DataPut_Item)adapter.getItem(0);
//				final String ls_reg_no   = ldgi.getData(0);

				pd = ProgressDialog.show(DataPut.this, "","BS점검 데이타를 전송중입니다...", true);		        	
			    v.postDelayed(new Runnable () {public void run() {
								//-------->>>>프로그레스 쓰레드 시작-------->>>>>>>
								Thread thread2 = new Thread(new Runnable() {
									public void run() {
										// TODO Auto-generated method stub
										f_dataput();
										//f_dataput_run("abc");
									}
								});
								thread2.start();
								//<<<<<<<--------프로그레스 쓰레드 시작<<<<<--------
			    }}, 300);

			}
		});
	
		listView.setOnDataSelectionListener( new OnDataSelectionListener_DataPut () {
            
   		public void onDataSelected(AdapterView<?> parent, final View v, int position, long id) {
   			
   			
   			//0:전표번호 1:세부공장코드 2:세부공장명 3:점검코드 4:점검코드텍스트 5:작업일자 6:작업일자텍스트 7:작업자코드 8:작업자명 9:점검건수 10:전송여부
   			final DataPut_Item selectItem = (DataPut_Item)adapter.getItem(position);
   			final int li_position = position;
   			
   			new AlertDialog.Builder(DataPut.this)
				.setTitle(R.string.MSG_TITLE)
				.setIcon(R.drawable.title_icon)
				.setItems(is_arr, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//Toast.makeText(getBaseContext(), is_arr[which], Toast.LENGTH_SHORT).show();
						switch (which) {
							case 0:
								
								String ls_put_flag;
								ls_put_flag = ((DataPut_Item) adapter.getItem(li_position)).getData(10);
								if (ls_put_flag.equals("완료")) {
									new AlertDialog.Builder(DataPut.this)
									.setTitle(R.string.MSG_TITLE)
									.setIcon(R.drawable.title_icon)
									.setMessage("이미 전송이 완료된 점검항목입니다.\n무시하고 전송하시겠습니까?")
									.setPositiveButton("예", new DialogInterface.OnClickListener(){
										public void onClick(DialogInterface dialog, int whichButton) {
											ipbtitle.setVisibility(View.VISIBLE);
											ipbtitle.setProgress(0);
											
											pd = ProgressDialog.show(DataPut.this, "","BS점검 데이타를 전송중입니다...", true);		        	
										    v.postDelayed(new Runnable () {public void run() {
															//-------->>>>프로그레스 쓰레드 시작-------->>>>>>>
															Thread thread2 = new Thread(new Runnable() {
																public void run() {
																	// TODO Auto-generated method stub
																	f_dataput(li_position);
																}
															});
															thread2.start();
															//<<<<<<<--------프로그레스 쓰레드 시작<<<<<--------
										    }}, 300);

										
										}
									})

									.setNegativeButton("아니요", new DialogInterface.OnClickListener(){
										public void onClick(DialogInterface dialog, int whichButton) {
											Toast.makeText(getBaseContext(), "전송취소", Toast.LENGTH_SHORT).show();
											return;
										}
									})
									.show();

								} else {
									ipbtitle.setVisibility(View.VISIBLE);
									ipbtitle.setProgress(0);
									
									pd = ProgressDialog.show(DataPut.this, "","BS점검 데이타를 전송중입니다...", true);		        	
								    v.postDelayed(new Runnable () {public void run() {
													//-------->>>>프로그레스 쓰레드 시작-------->>>>>>>
													Thread thread2 = new Thread(new Runnable() {
														public void run() {
															// TODO Auto-generated method stub
															f_dataput(li_position);
														}
													});
													thread2.start();
													//<<<<<<<--------프로그레스 쓰레드 시작<<<<<--------
								    }}, 300);
								}
								break;
								
								
							case 1:
				   			Bundle bundle = new Bundle();
				   			bundle.putString("reg_no", selectItem.getData(0));
				   			bundle.putString("reg_date", selectItem.getData(5));
				
				   			Intent liDetail = new Intent(getBaseContext(), DataPut_Search.class);
				   			liDetail.putExtras(bundle);
				   			startActivity(liDetail);
								break;
						}
						
					}
				})
				.setNegativeButton("취소", null)
				.show();	
   			
   			
   		}
		});     
		
		

	}

	
	public boolean f_db_conn_chk() {  //서버 DB연결 체크
		try
		{
			f_web_select("SELECT 1 CHK FROM DUAL");
			if (gjArr.length() > 0) {
				return true;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			
			new AlertDialog.Builder(DataPut.this)
			.setTitle(R.string.MSG_TITLE)
			.setIcon(R.drawable.title_icon)
			.setMessage("서버에서 데이타를 가져오는중에 오류가 발생하였습니다.\n" +
					      "데이타 베이스 연결 오류!!!")
			.setPositiveButton("확인"    , new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton) {
					finish();
				} 
			}).show();
			
			return false;
		}
	}
	
	public void fGetList() {
		String ls_update = null, ls_delete = null;

		try {
			ls_update =
					"UPDATE LB_INSP_MASTER " +
					"   SET QTY = (SELECT COUNT(1) CNT FROM LB_INSP_DETAIL D WHERE LB_INSP_MASTER.REG_NO = D.REG_NO)" +
					" WHERE MOBI_FLAG = '1'";
	 		DBH.DB.execSQL(ls_update);
	 		
		} catch (Exception e) {
 			Log.e(TAG, "점검내역(마스터) 수정 : " + ls_update);
 			Util.f_dialog(DataPut.this,"점검내역(마스터)을 수정중에 오류가 발생하였습니다.");
			//return -1;
		}

		try {
			ls_delete =
					"DELETE FROM LB_INSP_MASTER WHERE MOBI_FLAG = '1' AND IFNULL(QTY, 0) = 0";
	 		DBH.DB.execSQL(ls_delete);
	 		
		} catch (Exception e) {
 			Log.e(TAG, "점검내역(마스터) 삭제 : " + ls_delete);
 			Util.f_dialog(DataPut.this,"점검내역(마스터)을 삭제중에 오류가 발생하였습니다.");
			//return -1;
		}
		
		try
		{
			String ls_reg_no, ls_fact_sub_code, ls_fact_sub_name, ls_insp_reg_no, ls_insp_reg_no_text, ls_reg_date, ls_reg_date_text, ls_emp_code, ls_emp_name, ls_qty, ls_put_flag;
			
			adapter.clear();
			Cursor curSelect;
			curSelect = DBH.DB.rawQuery(
			      "   SELECT M.REG_NO, M.FACT_SUB_CODE, FS.FACT_SUB_NAME, M.LK_INSP_NO, M.REG_DATE, M.QTY, M.EMP_CODE, EMP.EMP_NAME, CASE M.PUT_FLAG WHEN '0' THEN '대기' WHEN '1' THEN '완료' END PUT_FLAG" +
	            "     FROM ((" +
	            "          LB_INSP_MASTER M LEFT OUTER JOIN LA_FACT_SUB FS ON M.FACT_SUB_CODE = FS.FACT_SUB_CODE)" +
	            "                           LEFT OUTER JOIN BE_DEPT_EMP EMP ON M.EMP_CODE = EMP.EMP_CODE)" +
	            "    WHERE M.MOBI_FLAG = '1'", null);
			int li_count = curSelect.getCount();

			for (int i=0;i<li_count;i++){
				curSelect.moveToNext();
				ls_reg_no    = curSelect.getString(curSelect.getColumnIndex("REG_NO"));
				ls_fact_sub_code = curSelect.getString(curSelect.getColumnIndex("FACT_SUB_CODE"));
				ls_fact_sub_name = curSelect.getString(curSelect.getColumnIndex("FACT_SUB_NAME"));
				ls_insp_reg_no  = curSelect.getString(curSelect.getColumnIndex("LK_INSP_NO"));
				ls_insp_reg_no_text  = String.format("%s-%s", ls_insp_reg_no.substring(0, 4), ls_insp_reg_no.substring(4));
				ls_reg_date  = curSelect.getString(curSelect.getColumnIndex("REG_DATE"));
				ls_reg_date_text  = Util.getFormatDate(curSelect.getString(curSelect.getColumnIndex("REG_DATE")));
				ls_emp_code  = curSelect.getString(curSelect.getColumnIndex("EMP_CODE"));
				ls_emp_name  = curSelect.getString(curSelect.getColumnIndex("EMP_NAME"));
				ls_qty       = String.format("%,d", curSelect.getInt(curSelect.getColumnIndex("QTY"))) + "건";
				ls_put_flag  = curSelect.getString(curSelect.getColumnIndex("PUT_FLAG"));

				//0:전표번호 1:세부공장코드 2:세부공장명 3:점검코드 4:점검코드텍스트 5:작업일자 6:작업일자텍스트 7:작업자코드 8:작업자명 9:점검건수 10:전송여부
				adapter.addItem(new DataPut_Item(ls_reg_no, ls_fact_sub_code, ls_fact_sub_name, ls_insp_reg_no, ls_insp_reg_no_text, ls_reg_date, ls_reg_date_text, ls_emp_code, ls_emp_name, ls_qty, ls_put_flag));

			}
			curSelect.close();

			if (li_count <= 0) { 
				new AlertDialog.Builder(DataPut.this)
				.setTitle(R.string.MSG_TITLE)
				.setIcon(R.drawable.title_icon)
				.setMessage("전송할 DATA가 존재하지 않습니다.")
				.setPositiveButton("예", mOk)
				.show();

			} else {
				Toast.makeText(getBaseContext(), "총 " + String.format("%,d", li_count) + "건 검색되었습니다.", Toast.LENGTH_SHORT).show();
			}
			
			listView.setAdapter(adapter);
//			adapter.notifyDataSetChanged();  //변경사항을 refalsh
			
			//<<<---리스트뷰<<<---
		} catch (Exception e) {
			Log.i(TAG, "f_getlist");
			e.printStackTrace();
		} finally {
 			if ( pd_bef != null ) pd_bef.hide();
		}
		
	}
	
	//전체 일괄실행
	public void f_dataput() {
		try
		{
			Message msg;
			ii_cnt = -1;
			
			int li_cnt, li_cnt_plus = 0;
			String ls_in_reg_no = "";
			int li_count = adapter.getCount();
			int li_start = 1;
			
			for (int i = li_start; i <= li_count; i++){
				DataPut_Item ldgi = (DataPut_Item)adapter.getItem(i - 1);
				
				if (ldgi.getData(10).equals("대기") == false) continue;
				
				li_cnt_plus = li_cnt_plus + 3;
				if (Util.f_isnull(ls_in_reg_no)) {
					ls_in_reg_no = "'" + ldgi.getData(0) + "'";
				} else {
					ls_in_reg_no = ls_in_reg_no + ", '" + ldgi.getData(0) + "'";
				}
			}
			
			//Log.i(TAG, "ls_in_reg_no:" + ls_in_reg_no);
			if (Util.f_isnull(ls_in_reg_no)) {
				msg = handler.obtainMessage();
				msg.arg1 = 100;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함, 100.아무것도안함)
				msg.arg2 = 1;  //최종실패건수
				msg.obj = "전송할 데이타가 존재하지 않습니다.";
				handler.sendMessage(msg);
				return;
			}
			
			String ls_select = "";
			try
			{
				ls_select =    
				   "   SELECT SUM(CNT) CNT" +
				   "     FROM (SELECT COUNT(1) CNT FROM LB_INSP_DETAIL WHERE REG_NO IN (" + ls_in_reg_no + ")" +
	            "           UNION ALL" +
	            "           SELECT COUNT(1) CNT FROM LB_INSP_CASE_CHEK WHERE REG_NO IN (" + ls_in_reg_no + "))";
	         Cursor curSelect = DBH.DB.rawQuery(ls_select, null);

	      	curSelect.moveToNext();
				li_cnt = curSelect.getInt(curSelect.getColumnIndex("CNT"));
				li_cnt = li_cnt +  + li_cnt_plus;

				if (li_cnt <= 0) {
					msg = handler.obtainMessage();
					msg.arg1 = 100;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함, 100.아무것도안함)
					msg.arg2 = 1;  //최종실패건수
					msg.obj = "전송할 데이타가 존재하지 않습니다.(SELECT 오류)";
					handler.sendMessage(msg);
					return;
				}
			} catch (Exception e) {
	 			Log.e(TAG, "갯수SELECT:" + ls_select);
	 			msg = handler.obtainMessage();
				msg.arg1 = 100;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함, 100.아무것도안함)
				msg.arg2 = 1;  //최종실패건수
				msg.obj = "전송할 데이타가 존재하지 않습니다.(SELECT 오류2)";
				handler.sendMessage(msg);

	 			return;
	 		}
			
			ipbtitle.setMax(li_cnt);
			
			String ls_reg_no = null;
			String ls_put_flag;

			for (int i = li_start; i <= li_count; i++){
				DataPut_Item ldgi = (DataPut_Item)adapter.getItem(i - 1);
				ls_reg_no   = ldgi.getData(0);
				ls_put_flag = ldgi.getData(10);
				
				if (ls_put_flag.equals("대기")) {
	    			f_dataput_run(ls_reg_no);  //9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함
	    		} else {
	    			//Log.i(TAG, ls_table_code + "일치 : " + ls_flag);
	    			//li_chk = 100;
	    			ii_cnt = ii_cnt + 1;
	    		}
	    		
			}

			
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	//선택된 Row만 실행
	public void f_dataput(int ai_getrow) {
		try
		{
			Message msg;
			ii_cnt = ai_getrow - 1;
			
			int li_cnt, li_cnt_plus = 0;
			String ls_in_reg_no = "";
			int li_count = ai_getrow + 1; //adapter.getCount();
			int li_start = ai_getrow + 1;
			
			for (int i = li_start; i <= li_count; i++){
				DataPut_Item ldgi = (DataPut_Item)adapter.getItem(i - 1);
				
				//if (ldgi.getData(10).equals("대기") == false) continue;
				
				li_cnt_plus = li_cnt_plus + 3;
				if (Util.f_isnull(ls_in_reg_no)) {
					ls_in_reg_no = "'" + ldgi.getData(0) + "'";
				} else {
					ls_in_reg_no = ls_in_reg_no + ", '" + ldgi.getData(0) + "'";
				}
			}
			
			//Log.i(TAG, "ls_in_reg_no:" + ls_in_reg_no);
			if (Util.f_isnull(ls_in_reg_no)) {
				msg = handler.obtainMessage();
				msg.arg1 = 100;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함, 100.아무것도안함)
				msg.arg2 = 1;  //최종실패건수
				msg.obj = "전송할 데이타가 존재하지 않습니다.";
				handler.sendMessage(msg);
				return;
			}
			
			String ls_select = "";
			try
			{
				ls_select =    
				   "   SELECT SUM(CNT) CNT" +
				   "     FROM (SELECT COUNT(1) CNT FROM LB_INSP_DETAIL WHERE REG_NO IN (" + ls_in_reg_no + ")" +
	            "           UNION ALL" +
	            "           SELECT COUNT(1) CNT FROM LB_INSP_CASE_CHEK WHERE REG_NO IN (" + ls_in_reg_no + "))";
	         Cursor curSelect = DBH.DB.rawQuery(ls_select, null);

	      	curSelect.moveToNext();
				li_cnt = curSelect.getInt(curSelect.getColumnIndex("CNT"));
				li_cnt = li_cnt +  + li_cnt_plus;

				if (li_cnt <= 0) {
					msg = handler.obtainMessage();
					msg.arg1 = 100;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함, 100.아무것도안함)
					msg.arg2 = 1;  //최종실패건수
					msg.obj = "전송할 데이타가 존재하지 않습니다.(SELECT 오류)";
					handler.sendMessage(msg);
					return;
				}
			} catch (Exception e) {
	 			Log.e(TAG, "갯수SELECT:" + ls_select);
	 			msg = handler.obtainMessage();
				msg.arg1 = 100;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함, 100.아무것도안함)
				msg.arg2 = 1;  //최종실패건수
				msg.obj = "전송할 데이타가 존재하지 않습니다.(SELECT 오류2)";
				handler.sendMessage(msg);

	 			return;
	 		}
			
			ipbtitle.setMax(li_cnt);
			
			String ls_reg_no = null;
			String ls_put_flag;

			for (int i = li_start; i <= li_count; i++){
				DataPut_Item ldgi = (DataPut_Item)adapter.getItem(i - 1);
				ls_reg_no   = ldgi.getData(0);
				ls_put_flag = ldgi.getData(10);
				
				//if (ls_put_flag.equals("대기")) {
	    			f_dataput_run(ls_reg_no);  //9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함
	    		//} else {
	    			//Log.i(TAG, ls_table_code + "일치 : " + ls_flag);
	    			//li_chk = 100;
	    		//}
	    		
			}

			
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void f_dataput_run(String as_reg_no) {
		/* -------------->>>> 저장절차  --------------->>>>>>>>>
			순번	구분(O/M)	전표			Insert/Update	PUT_FLAG
			-----------------------------------------------------
			1		Oracle		Master		Insert			0	
			2		Oracle		Detail		Insert			없음	-> 실패시 빠져나감(마스터는 생성되어 있으나 PUT_FLAG값은 '0'값을 가지고 있음)
			3		Oracle		Case_Chek	Insert			없음	-> 실패시 빠져나감(마스터는 생성되어 있으나 PUT_FLAG값은 '0'값을 가지고 있음)
			4		Oracle		Master		Update			1		->Oracle 완료(마스터에 PUT_FLAG값을 '1'으로 설정함으로써 전송완료 처리)
			5		Mobile		Master		Update			1		->Mobile 전송완료
			----------------------------------------------------*/
		String ls_insert = null, ls_update = null;
		int li_count;
		Cursor curSelect;
		String ls_max_reg_no, ls_max_reg_code;
		
		Message msg = handler.obtainMessage();  //-------------------------------------------------------------------------------------------------------
		msg.arg1 = 1;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함 100.아무것도안함)
		msg.arg2 = 0;  //최종실패건수
		msg.obj = "작업준비중...";
		handler.sendMessage(msg);
		
		String ls_lk_insp_no;
		try
		{
			String ls_reg_date;
			String ls_make_code, ls_fact_code, ls_fact_sub_code, ls_fact_detl_seq, ls_emp_code, ls_qty, ls_bigo;
			
			
			curSelect = DBH.DB.rawQuery(
			      "   SELECT M.REG_NO,    M.LK_INSP_NO, M.REG_DATE,      M.REG_CODE," +
					"          M.MAKE_CODE, M.FACT_CODE,  M.FACT_SUB_CODE, M.FACT_DETL_SEQ, M.EMP_CODE, M.QTY, M.BIGO" +
	            "     FROM LB_INSP_MASTER M" +
	            "    WHERE M.REG_NO = '" + as_reg_no + "'", null);
			
			if (curSelect.getCount() <= 0) {
				msg = handler.obtainMessage();
				msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
				msg.arg2 = 1;  //최종실패건수
				msg.obj = "BS점검 데이타 전송 작업을 실패했습니다.\n(MASTER 조회실패)";
				handler.sendMessage(msg);
				
				return;
			}
			curSelect.moveToNext();
			//ls_reg_no        = curSelect.getString(curSelect.getColumnIndex("REG_NO"));
			ls_lk_insp_no    = curSelect.getString(curSelect.getColumnIndex("LK_INSP_NO"));
			ls_reg_date      = curSelect.getString(curSelect.getColumnIndex("REG_DATE"));
			//ls_reg_code      = curSelect.getString(curSelect.getColumnIndex("REG_CODE"));
			ls_make_code     = curSelect.getString(curSelect.getColumnIndex("MAKE_CODE"));
			ls_fact_code     = curSelect.getString(curSelect.getColumnIndex("FACT_CODE"));
			ls_fact_sub_code = curSelect.getString(curSelect.getColumnIndex("FACT_SUB_CODE"));
			ls_fact_detl_seq = curSelect.getString(curSelect.getColumnIndex("FACT_DETL_SEQ"));
			ls_emp_code      = curSelect.getString(curSelect.getColumnIndex("EMP_CODE"));
			ls_qty           = curSelect.getInt(curSelect.getColumnIndex("QTY")) + "";
			ls_bigo          = curSelect.getString(curSelect.getColumnIndex("BIGO"));
			curSelect.close();
				
			try {
				f_web_select("SELECT FN_REG_NO_LB_INSP_MASTER('" + ls_reg_date + "') REG_NO, FN_REG_CODE_LB_INSP_MASTER('" + ls_reg_date + "') REG_CODE FROM DUAL");
				JSONObject jObj = gjArr.getJSONObject(0);  //추출
				ls_max_reg_no = jObj.getString("REG_NO");
				ls_max_reg_code = jObj.getString("REG_CODE");
				
			} catch (Exception e) {
	 			Log.e(TAG, "f_dataput_run- web_max_select:");
	 			msg = handler.obtainMessage();  //-------------------------------------------------------------------------------------------------------
				msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
				msg.arg2 = 1;  //최종실패건수
				msg.obj = "BS점검 데이타 전송 작업을 실패했습니다.\n(ERP MASTER 조회실패)";
				handler.sendMessage(msg);
				
	 			return;
	 		}

			try {  //순번 1
				ls_insert =
						"INSERT INTO LB_INSP_MASTER M" +
						"           (M.REG_NO,    M.LK_INSP_NO, M.REG_DATE,      M.REG_CODE," +
						"            M.MAKE_CODE, M.FACT_CODE,  M.FACT_SUB_CODE, M.FACT_DETL_SEQ, M.EMP_CODE, M.QTY, M.BIGO," +
						"            M.MOBI_SERI, M.PUT_FLAG)" +
						"    VALUES ('" + ls_max_reg_no + "', " +
						            "'" + ls_lk_insp_no + "', " +
						            "'" + ls_reg_date + "', " +
						            "'" + ls_max_reg_code + "', " +

						            "'" + ls_make_code + "', " +
						            "'" + ls_fact_code + "', " +
						            "'" + ls_fact_sub_code + "', " +
						            "'" + ls_fact_detl_seq + "', " +
						            "'" + ls_emp_code + "', " +
						            "" + ls_qty + ", " +
						            "'" + Util.f_replace_sql(ls_bigo) + "', " +

						            "'" + gs_serial_key + "', " +
						            "'" + "0" + "')";
				
				f_web_exec(ls_insert);
				if (!gsDbRtn.equals("1")) {  //1:성공 0:실패
					Log.e(TAG, "ls_insert(M):" + ls_insert);
					
					msg = handler.obtainMessage();  //-------------------------------------------------------------------------------------------------------
					msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
					msg.arg2 = 1;  //최종실패건수
					msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(MASTER 등록)";
					handler.sendMessage(msg);
					
					return; 
				}
				
				msg = handler.obtainMessage();  //-------------------------------------------------------------------------------------------------------
				msg.arg1 = 100;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
				msg.arg2 = 0;  //최종실패건수
				msg.obj = "[" + String.format("%s-%s", ls_lk_insp_no.substring(0, 4), ls_lk_insp_no.substring(4)) + "] MASTER 전송중...";
				handler.sendMessage(msg);
				
			} catch (Exception e) {
	 			Log.e(TAG, "f_dataput_run- web_master_insert:" + ls_insert);
	 			
	 			msg = handler.obtainMessage();
	 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
	 			msg.arg2 = 1;  //최종실패건수
	 			msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(MASTER 등록2)";
	 			handler.sendMessage(msg);
	 			return;
	 		}
		} catch (Exception e) {
			e.printStackTrace();
 			//Util.f_dialog(DataGet_Detail.this, ls_table_name + "(" + ls_table_code + ") 데이타 설정중에 오류가 발생하였습니다.");
 			
			msg = handler.obtainMessage();
 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
 			msg.arg2 = 1;  //최종실패건수
 			msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(MASTER 등록3)";
 			handler.sendMessage(msg);
			return;
		}

		try {  //순번 2
			String ls_reg_seq, ls_reg_skey, ls_reg_date;
			String ls_make_code, ls_fact_code, ls_fact_sub_code, ls_line_code, ls_fact_line_seq, ls_emp_code;
			String ls_op_no, ls_macn_code, ls_macn_name, ls_macn_model, ls_macn_make_code, ls_macn_make_name, ls_maker_code, ls_bar_code;
			String ls_item_code, ls_item_spec, ls_jata_flag, ls_bar_la_code, ls_match_flag, ls_make_date;
			String ls_state_flag, ls_chek_flag, ls_case_text, ls_chek_text, ls_qty, ls_bigo;
			
			curSelect = DBH.DB.rawQuery(
							"   SELECT D.REG_NO,     D.REG_SEQ,   D.REG_SKEY,      D.REG_DATE,   D.REG_CODE," +
							"          D.MAKE_CODE,  D.FACT_CODE, D.FACT_SUB_CODE, D.LINE_CODE,  D.FACT_LINE_SEQ,  D.EMP_CODE," +
							"          D.OP_NO,      D.MACN_CODE, D.MACN_NAME,     D.MACN_MODEL, D.MACN_MAKE_CODE, D.MACN_MAKE_NAME, D.MAKER_CODE, D.BAR_CODE," +
							"          D.ITEM_CODE,  D.ITEM_SPEC, D.JATA_FLAG,     D.BAR_LA_CODE,D.MATCH_FLAG, D.MAKE_DATE," +
							"          D.STATE_FLAG, D.CHEK_FLAG, D.CASE_TEXT,     D.CHEK_TEXT,  D.QTY,            D.BIGO" +
							"     FROM LB_INSP_DETAIL D" +
							"    WHERE D.REG_NO = '" + as_reg_no + "'", null);
			
			li_count = curSelect.getCount();
			if (li_count <= 0) {
				msg = handler.obtainMessage();
	 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
				msg.arg2 = 1;  //최종실패건수
				msg.obj = "BS점검 데이타 전송 작업을 실패했습니다.\n(DETAIL 조회실패)";
				handler.sendMessage(msg);
				return;
			}
			
			for (int i=0;i<li_count;i++){
				curSelect.moveToNext();
				//ls_reg_no         = curSelect.getString(curSelect.getColumnIndex("REG_NO"));
				ls_reg_seq        = curSelect.getString(curSelect.getColumnIndex("REG_SEQ"));
				ls_reg_skey       = curSelect.getString(curSelect.getColumnIndex("REG_SKEY"));
				ls_reg_date       = curSelect.getString(curSelect.getColumnIndex("REG_DATE"));
				//ls_reg_code       = curSelect.getString(curSelect.getColumnIndex("REG_CODE"));
				
				ls_make_code      = curSelect.getString(curSelect.getColumnIndex("MAKE_CODE"));
				ls_fact_code      = curSelect.getString(curSelect.getColumnIndex("FACT_CODE"));
				ls_fact_sub_code  = curSelect.getString(curSelect.getColumnIndex("FACT_SUB_CODE"));
				ls_line_code      = curSelect.getString(curSelect.getColumnIndex("LINE_CODE"));       if (Util.f_isnull(ls_line_code)) ls_line_code = "000";
				ls_fact_line_seq  = curSelect.getString(curSelect.getColumnIndex("FACT_LINE_SEQ"));   if (Util.f_isnull(ls_fact_line_seq)) ls_fact_line_seq = "0000";
				ls_emp_code       = curSelect.getString(curSelect.getColumnIndex("EMP_CODE"));
				
				ls_op_no          = curSelect.getString(curSelect.getColumnIndex("OP_NO"));
				ls_macn_code      = curSelect.getString(curSelect.getColumnIndex("MACN_CODE"));
				ls_macn_name      = curSelect.getString(curSelect.getColumnIndex("MACN_NAME"));
				ls_macn_model     = curSelect.getString(curSelect.getColumnIndex("MACN_MODEL"));
				ls_macn_make_code = curSelect.getString(curSelect.getColumnIndex("MACN_MAKE_CODE"));
				ls_macn_make_name = curSelect.getString(curSelect.getColumnIndex("MACN_MAKE_NAME"));
				ls_maker_code     = curSelect.getString(curSelect.getColumnIndex("MAKER_CODE"));
				ls_bar_code       = curSelect.getString(curSelect.getColumnIndex("BAR_CODE")).toUpperCase();  //시리얼번호는 대문자로
				
				ls_item_code      = curSelect.getString(curSelect.getColumnIndex("ITEM_CODE"));
				ls_item_spec      = curSelect.getString(curSelect.getColumnIndex("ITEM_SPEC"));
				ls_jata_flag      = curSelect.getString(curSelect.getColumnIndex("JATA_FLAG"));
				ls_bar_la_code    = curSelect.getString(curSelect.getColumnIndex("BAR_LA_CODE"));
				ls_match_flag     = curSelect.getString(curSelect.getColumnIndex("MATCH_FLAG"));
				ls_make_date      = curSelect.getString(curSelect.getColumnIndex("MAKE_DATE"));
				
				ls_state_flag     = curSelect.getString(curSelect.getColumnIndex("STATE_FLAG"));
				ls_chek_flag      = curSelect.getString(curSelect.getColumnIndex("CHEK_FLAG"));
				ls_case_text      = curSelect.getString(curSelect.getColumnIndex("CASE_TEXT"));
				ls_chek_text      = curSelect.getString(curSelect.getColumnIndex("CHEK_TEXT"));
				ls_qty            = curSelect.getInt(curSelect.getColumnIndex("QTY")) + "";
				ls_bigo           = curSelect.getString(curSelect.getColumnIndex("BIGO"));
				
				
				try {
					ls_insert =
							"INSERT INTO LB_INSP_DETAIL D" +
							"           (D.REG_NO,     D.REG_SEQ,   D.REG_SKEY,      D.REG_DATE,   D.REG_CODE," +
							"            D.MAKE_CODE,  D.FACT_CODE, D.FACT_SUB_CODE, D.LINE_CODE,  D.FACT_LINE_SEQ,  D.EMP_CODE," +
							"            D.OP_NO,      D.MACN_CODE, D.MACN_NAME,     D.MACN_MODEL, D.MACN_MAKE_CODE, D.MACN_MAKE_NAME, D.MAKER_CODE, D.BAR_CODE," +
							"            D.ITEM_CODE,  D.ITEM_SPEC, D.JATA_FLAG,     D.BAR_LA_CODE,D.MATCH_FLAG, D.MAKE_DATE," +
							"            D.STATE_FLAG, D.CHEK_FLAG, D.CASE_TEXT,     D.CHEK_TEXT,  D.QTY,            D.BIGO)" +
							"    VALUES ('" + ls_max_reg_no + "', " +
							            "'" + ls_reg_seq + "', " +
							            "'" + ls_reg_skey + "', " +
							            "'" + ls_reg_date + "', " +
							            "'" + ls_max_reg_code + "', " +

							            "'" + ls_make_code + "', " +
							            "'" + ls_fact_code + "', " +
							            "'" + ls_fact_sub_code + "', " +
							            "'" + ls_line_code + "', " +
							            "'" + ls_fact_line_seq + "', " +
							            "'" + ls_emp_code + "', " +
							            
							            "'" + Util.f_replace_sql(ls_op_no) + "', " +
							            "'" + ls_macn_code + "', " +
							            "'" + Util.f_replace_sql(ls_macn_name) + "', " +
							            "'" + Util.f_replace_sql(ls_macn_model) + "', " +
							            "'" + ls_macn_make_code + "', " +
							            "'" + Util.f_replace_sql(ls_macn_make_name) + "', " +
							            "'" + Util.f_replace_sql(ls_maker_code) + "', " +
							            "'" + Util.f_replace_sql(ls_bar_code) + "', " +
							            
							            "'" + ls_item_code + "', " +
							            "'" + Util.f_replace_sql(ls_item_spec) + "', " +
							            "'" + Util.f_replace_sql(ls_jata_flag) + "', " +
							            "'" + Util.f_replace_sql(ls_bar_la_code) + "', " +
							            "'" + Util.f_replace_sql(ls_match_flag) + "', " +
							            "'" + ls_make_date + "', " +
							            
							            "'" + Util.f_replace_sql(ls_state_flag) + "', " +
							            "'" + Util.f_replace_sql(ls_chek_flag) + "', " +
							            "'" + Util.f_replace_sql(ls_case_text) + "', " +
							            "'" + Util.f_replace_sql(ls_chek_text) + "', " +
							            "" + ls_qty + ", " +
							            "'" + Util.f_replace_sql(ls_bigo) + "')";
					//Log.i(TAG, "insert D:" + ls_insert);
					
					f_web_exec(ls_insert);
					if (!gsDbRtn.equals("1")) {  //1:성공 0:실패
						ls_insert = ls_insert.replace(", 'null'", ", null");
						Log.e(TAG, "ls_insert(D):" + ls_insert);
						
						msg = handler.obtainMessage();
			 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
						msg.arg2 = 1;  //최종실패건수
						msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(DETAIL 등록실패)";
						handler.sendMessage(msg);
						return;
					}
					msg = handler.obtainMessage();  //-------------------------------------------------------------------------------------------------------
					msg.arg1 = 100;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
					msg.arg2 = 0;  //최종실패건수
					msg.obj = "[" + String.format("%s-%s", ls_lk_insp_no.substring(0, 4), ls_lk_insp_no.substring(4)) + "] DETAIL 전송중...";
					handler.sendMessage(msg);
					
				} catch (Exception e) {
					Log.e(TAG, "f_dataput_run- web_detail_insert:" + ls_insert);
		 			
					msg = handler.obtainMessage();
		 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
					msg.arg2 = 1;  //최종실패건수
					msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(DETAIL 등록실패2)";
					handler.sendMessage(msg);
		 			return;
		 		}
				
			}
			curSelect.close();

		} catch (Exception e) {
 			Log.e(TAG, "f_dataput_run- web_insert:" + ls_insert);
 			
 			msg = handler.obtainMessage();
 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
 			msg.arg2 = 1;  //최종실패건수
 			msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(DETAIL 등록실패3)";
 			handler.sendMessage(msg);
 			return;
		}

		try {  //순번 3
			String ls_reg_seq, ls_reg_sseq, ls_reg_sskey;
			String ls_reg_date, ls_case_code, ls_chek_code, ls_bigo;
			
			curSelect = DBH.DB.rawQuery(
							"   SELECT C.REG_NO,    C.REG_SEQ,   C.REG_SSEQ,      C.REG_SSKEY,  C.REG_DATE,       C.REG_CODE," +
							"          C.CASE_CODE, C.CHEK_CODE, C.BIGO" +
							"     FROM LB_INSP_CASE_CHEK C" +
							"    WHERE C.REG_NO = '" + as_reg_no + "'", null);
			
			li_count = curSelect.getCount();
			//if (li_count <= 0) return false;  //없을수도 있음
			
			for (int i=0;i<li_count;i++){
				curSelect.moveToNext();
				//ls_reg_no         = curSelect.getString(curSelect.getColumnIndex("REG_NO"));
				ls_reg_seq        = curSelect.getString(curSelect.getColumnIndex("REG_SEQ"));
				ls_reg_sseq       = curSelect.getString(curSelect.getColumnIndex("REG_SSEQ"));
				ls_reg_sskey      = curSelect.getString(curSelect.getColumnIndex("REG_SSKEY"));
				ls_reg_date       = curSelect.getString(curSelect.getColumnIndex("REG_DATE"));
				//ls_reg_code       = curSelect.getString(curSelect.getColumnIndex("REG_CODE"));
				
				ls_case_code      = curSelect.getString(curSelect.getColumnIndex("CASE_CODE"));
				ls_chek_code      = curSelect.getString(curSelect.getColumnIndex("CHEK_CODE"));
				ls_bigo           = curSelect.getString(curSelect.getColumnIndex("BIGO"));
				
				try {
					ls_insert =
							"INSERT INTO LB_INSP_CASE_CHEK C" +
							"           (C.REG_NO,    C.REG_SEQ,   C.REG_SSEQ,      C.REG_SSKEY,  C.REG_DATE,       C.REG_CODE," +
							"            C.CASE_CODE, C.CHEK_CODE, C.BIGO)" +
							"    VALUES ('" + ls_max_reg_no + "', " +
							            "'" + ls_reg_seq + "', " +
							            "'" + ls_reg_sseq + "', " +
							            "'" + ls_reg_sskey + "', " +
							            "'" + ls_reg_date + "', " +
							            "'" + ls_max_reg_code + "', " +

							            "'" + ls_case_code + "', " +
							            "'" + ls_chek_code + "', " +
							            "'" + Util.f_replace_sql(ls_bigo) + "')";

					f_web_exec(ls_insert);
					if (!gsDbRtn.equals("1")) {  //1:성공 0:실패
						Log.e(TAG, "f_dataput_run-web:" + ls_insert);
						
						msg = handler.obtainMessage();
			 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
						msg.arg2 = 1;  //최종실패건수
						msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(조치사항 등록실패)";
						handler.sendMessage(msg);
						return; 
					}
					msg = handler.obtainMessage();  //-------------------------------------------------------------------------------------------------------
					msg.arg1 = 100;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
					msg.arg2 = 0;  //최종실패건수
					msg.obj = "[" + String.format("%s-%s", ls_lk_insp_no.substring(0, 4), ls_lk_insp_no.substring(4)) + "] 조치내역 전송중...";
					handler.sendMessage(msg);
					
				} catch (Exception e) {
					ls_insert = ls_insert.replace(", 'null'", ", null");
		 			Log.e(TAG, "f_dataput_run- web_case_chek_insert:" + ls_insert);
		 			
		 			msg = handler.obtainMessage();
		 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
					msg.arg2 = 1;  //최종실패건수
					msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(조치사항 등록실패2)";
					handler.sendMessage(msg);
		 			return;
		 		}
				
			}
			curSelect.close();

		} catch (Exception e) {
 			Log.e(TAG, "f_dataput_run- web_insert:" + ls_insert);
 			
 			msg = handler.obtainMessage();
 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
			msg.arg2 = 1;  //최종실패건수
			msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(조치사항 등록실패3)";
			handler.sendMessage(msg);
 			return;
		}

		try {  //순번 4
			ls_update =
					" UPDATE LB_INSP_MASTER" +
					"    SET PUT_FLAG = '1'" +
					"  WHERE REG_NO = '" + ls_max_reg_no + "'";
			
			f_web_exec(ls_update);
			if (!gsDbRtn.equals("1")) {  //1:성공 0:실패
				Log.e(TAG, "f_dataput_run-web:" + ls_update);
				
				msg = handler.obtainMessage();
	 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
				msg.arg2 = 1;  //최종실패건수
				msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(ERP 작업완료 설정)";
				handler.sendMessage(msg);
				
				return;
			}
		} catch (Exception e) {
 			Log.e(TAG, "f_dataput_run- web_master_update:" + ls_update);
 			return;
 		}

		
		try {  //순번 5
			ls_update =
					"UPDATE LB_INSP_MASTER" +
			      "   SET PUT_FLAG = '1'" +
			      " WHERE REG_NO = '" + as_reg_no + "'";
			DBH.DB.execSQL(ls_update);
		} catch (Exception e) {
 			Log.e(TAG, "f_dataput_run- master_update:" + ls_update);
 			
 			msg = handler.obtainMessage();
 			msg.arg1 = 9;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
			msg.arg2 = 1;  //최종실패건수
			msg.obj = "BS점검 데이타 등록 작업을 실패했습니다.\n(작업완료 설정)";
			handler.sendMessage(msg);
			return;
 		}
		
		
		msg = handler.obtainMessage();  //-------------------------------------------------------------------------------------------------------
		msg.arg1 = 2;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함)
		msg.arg2 = 0;  //최종실패건수
		handler.sendMessage(msg);
		

	}
	
	public class ProgressHandler extends Handler {
		public void handleMessage(Message msg) {
//			//Log.i(TAG, "progress:" + ipbtitle.getProgress());
			
			if ((msg.arg1 != 100)) {
				if (msg.arg1 == 1) ii_cnt = ii_cnt + 1;  //진행으로 들어왔을때문 아답터를 선택하도록
				
				//DataPut_Item selectItem = (DataPut_Item)adapter.getItem(ipbtitle.getProgress());
				DataPut_Item selectItem = (DataPut_Item)adapter.getItem(ii_cnt);
				String mData[] = selectItem.getData();
				
	   		//9.실패, 0.대기 1.진행 2.완료 3.일치, 100.아무것도안함
	   		if (msg.arg1 == 0) {
	   			mData[10] = "대기"; //변경된 내용만 변수에 적용
	   		} else if (msg.arg1== 1) {
	   			mData[10] = "진행";
	   		} else if (msg.arg1== 2) {
	   			mData[10] = "완료";
	   		} else if (msg.arg1== 3) {
	   			mData[10] = "일치";
	   		} else if (msg.arg1== 9) {
	   			mData[10] = "실패";
	   		} else if (msg.arg1== 100) {
	   			//아무것도 안함
	   		} else {
	   			mData[10] = "오류(" + msg.arg1 + ")";
	   		}
	   		selectItem.setData(mData);   //적용된 변수를 SetData
	   		adapter.notifyDataSetChanged();  //변경사항을 refalsh
			}
   		
			ipbtitle.incrementProgressBy(1);
			
			if (msg.arg2 > 0) {
				Util.f_dialog(DataPut.this, (String) msg.obj);  //BS점검 데이타 실패
				if ( pd != null ) pd.hide();
				ipbtitle.setVisibility(View.GONE);
			} else {
			
				if (ipbtitle.getProgress() == ipbtitle.getMax()) {
					if ( pd != null ) pd.hide();
					Util.f_dialog(DataPut.this, "BS점검 데이타 전송작업을 완료했습니다.");
					itvtitle.setText("점검내역 전송완료!!!");
				} else {
	         	itvtitle.setText((String) msg.obj);
				}
			}
		}
	}
	
	DialogInterface.OnClickListener mClickLeft = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			if (whichButton == DialogInterface.BUTTON1) {
				finish();
			}
		}
	};
	DialogInterface.OnClickListener mOk = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			if (whichButton == DialogInterface.BUTTON1) {
				finish();
			}
		}
	};	

}
