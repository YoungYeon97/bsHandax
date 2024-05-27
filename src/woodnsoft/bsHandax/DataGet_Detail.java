package woodnsoft.bsHandax;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import woodnsoft.bsHandax.DataListView.DataGet.DataGetItem;
import woodnsoft.bsHandax.DataListView.DataGet.DataGetListAdapter;
import woodnsoft.bsHandax.DataListView.DataGet.DataListView;
import woodnsoft.bsHandax.DataListView.DataGet.OnDataSelectionListener;
import woodnsoft.bsHandax.common.DBActivity;
import woodnsoft.bsHandax.common.Util;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
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

public class DataGet_Detail extends DBActivity {
	
	private static final String TAG = "DataGet_Detail"; 
	DataListView listView;
	DataGetListAdapter adapter;
	
	//프로그래스 쓰레드 관련
	ProgressHandler handler;

	//타이틀 컨트롤
	ProgressBar ipbtitle;
	TextView    itvtitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dataget_detail);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		handler = new ProgressHandler();  //프로그래스 쓰레드
		ipbtitle = (ProgressBar) findViewById(R.id.pbtitle);
		itvtitle = (TextView) findViewById(R.id.tvtitle);

		
		//--->>>리스트뷰--->>>
		adapter = new DataGetListAdapter(this);
		listView = new DataListView(this);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayoutDrugList );
		linLayout.addView(listView);
		

		pd_bef = ProgressDialog.show(DataGet_Detail.this, "","기초 데이타 작업목록을 수신중입니다...", true);		        	
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
				//fSelect2();
				
				/*
				DataGetItem selectItem = (DataGetItem)adapter.getItem(2);
				String mData[] = selectItem.getData();  //기존내용을 변수에 셋팅
				mData[3] = "진행";  //변경된 내용만 변수에 적용
				
				selectItem.setData(mData);   //적용된 변수를 SetData
				adapter.notifyDataSetChanged();  //변경사항을 refalsh
				*/
			}
		});
		
		Button lbok = (Button) findViewById(R.id.bOk);  //실행
		lbok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ipbtitle.setVisibility(View.VISIBLE);
				ipbtitle.setProgress(0);
				
				pd = ProgressDialog.show(DataGet_Detail.this, "","기초 데이타를 수신중입니다...", true);		        	
			    v.postDelayed(new Runnable () {public void run() {
								//-------->>>>프로그레스 쓰레드 시작-------->>>>>>>
								Thread thread2 = new Thread(new Runnable() {
									public void run() {
										// TODO Auto-generated method stub
										f_dataget();
									}
								});
								thread2.start();
								//<<<<<<<--------프로그레스 쓰레드 시작<<<<<--------
			    }}, 300);

			}
		});
	
		listView.setOnDataSelectionListener( new OnDataSelectionListener () {
            
   		public void onDataSelected(AdapterView<?> parent, View v, int position, long id) {
   			// make intent
   			DataGetItem selectItem = (DataGetItem)adapter.getItem(position);
                
   			//Toast.makeText(getBaseContext(), selectItem.getData(0), Toast.LENGTH_SHORT).show();

                
   			Bundle bundle = new Bundle();
   			bundle.putString("data0", selectItem.getData(0));
   			bundle.putString("data1", selectItem.getData(1));
   			bundle.putString("data2", selectItem.getData(2));
   			bundle.putString("data3", selectItem.getData(3));
                
   			Intent liDetail = new Intent(getBaseContext(), Detail.class);
   			liDetail.putExtras(bundle);
   			startActivity(liDetail);
                
   		}
		});        

	}

	public void fAddAdapter(String as_table_name, String as_table_code) {
		String ls_modi_date, ls_flag;
		
		Cursor cur = DBH.selectAZ_MODI_DATE(as_table_code);
		if (cur.getCount() > 0) {
			cur.moveToNext();
			int li_modi_date   = cur.getColumnIndex("MODI_DATE");    //최종수정일
			int li_modi_date_m = cur.getColumnIndex("MODI_DATE_M");  //최종수정일(모)
			
			ls_modi_date   = Util.getFormatDate(cur.getString(li_modi_date));
			
			if (cur.getString(li_modi_date).equals(cur.getString(li_modi_date_m))) ls_flag = "일치"; else ls_flag = "대기";
			
		} else {
			ls_modi_date = "미수신";
			ls_flag = "대기";
		}
		
		adapter.addItem(new DataGetItem(as_table_name, as_table_code, ls_modi_date, ls_flag));
		cur.close();
	}

	private boolean fChk_AZ_MODI_DATE() {  //최종수정일관리
		String ls_select = "", ls_update = "", ls_insert = "";
		
		try
		{
			if (DBH.fCreate("AZ_MODI_DATE") == false) {
				Util.f_dialog(DataGet_Detail.this,"최종수정일관리(AZ_MODI_DATE)를 생성중에 오류가 발생하였습니다.");
				return false;
			}

			//----->>>>>실데이타를 가져온다.
			ls_select = "SELECT MODI.TABLE_CODE, MODI.MODI_DATE" +
                     "  FROM AZ_MODI_DATE MODI";
			
			f_web_select(ls_select);
			
	 		//Log.i(TAG,"[결과 : " + gsDbRtn + " ----- 전체건수 : " + gjArr.length() + "-------------]");

		   
	 		DBH.DB.beginTransaction();
	 		try {
	 			Cursor curSelect;
		    	for (int i = 0; i < gjArr.length(); i++) {
		    		JSONObject jObj = gjArr.getJSONObject(i);  //추출
		    		
		    		curSelect = DBH.DB.rawQuery("SELECT COUNT(1) CNT FROM AZ_MODI_DATE WHERE TABLE_CODE = '" + jObj.getString("TABLE_CODE") + "'", null);
					curSelect.moveToNext();
		    		if (curSelect.getInt(curSelect.getColumnIndex("CNT")) <= 0) {
				 		ls_insert =	"INSERT INTO AZ_MODI_DATE" +
	    				      "           (TABLE_CODE, MODI_DATE, MODI_DATE_M, VER_M, USE_FLAG)" +
	    				      "    VALUES ('" + jObj.getString("TABLE_CODE") + "', '" + 
	    				                        jObj.getString("MODI_DATE") + "', '" + 
	    				                        "19000101 00000000" + "', '" + 
	    				                        DBH.VER + "', '" +
                                          '0' + "')";
				 		DBH.DB.execSQL(ls_insert);
				 		//Log.i(TAG, ls_insert);
		    		} else {
		    			ls_update = "UPDATE AZ_MODI_DATE" +
     							      "   SET MODI_DATE = '" + jObj.getString("MODI_DATE") + "'" +
    							      " WHERE TABLE_CODE = '" + jObj.getString("TABLE_CODE") + "'";
		    			DBH.DB.execSQL(ls_update);
		    			//Log.i(TAG, ls_update);
		    		}
		    		curSelect.close();
		    		
		    	}
	 			DBH.DB.setTransactionSuccessful();  //commit;
           
	 		} catch (SQLiteException es) {
	 			Log.e(TAG,"INSERT INTO AZ_MODI_DATE : " + ls_insert);
				Util.f_dialog(DataGet_Detail.this,"최종수정일관리(AZ_MODI_DATE) 데이타 INSERT 중에 오류가 발생하였습니다.");
	 			return false;
	 		} finally {
	 			DBH.DB.endTransaction();
	 		}
	    	
			return true;
	
		} catch (Exception e) {
			e.printStackTrace();
 			Log.e(TAG, "fHttpGet : " + ls_select);
			Util.f_dialog(DataGet_Detail.this,"최종수정일관리(AZ_MODI_DATE) 데이타 설정중에 오류가 발생하였습니다.");
			return false;
 		} finally {
 			if ( pd_bef != null ) pd_bef.hide();
			
		}
		
	}

	
	public void fGetList() {
		try
		{
			fChk_AZ_MODI_DATE();
	
			fAddAdapter("제조사",         "LA_MAKE");
			
			fAddAdapter("공장코드",       "LA_FACT");
			fAddAdapter("제조사공장",     "LA_MAKE_FACT");
			
			fAddAdapter("세부공장코드",   "LA_FACT_SUB");
			fAddAdapter("세부공장",       "LA_FACT_DETAIL");
			
			fAddAdapter("작업라인코드",   "LA_LINE");
			fAddAdapter("작업라인",       "LA_FACT_LINE");
			
			fAddAdapter("조치사항",       "LA_CHEK");
	
			fAddAdapter("점검항목",       "LA_CASE");
			fAddAdapter("품목분류별점검항목", "LA_CASE_GRUP");
			
			fAddAdapter("메이커코드",     "LA_MAKER");
			fAddAdapter("메이커별품목규격관리", "LA_MAKER_ITEM_SPEC");
			fAddAdapter("제품구분",       "LA_BAR_LA");
			
			//fAddAdapter("시리얼번호관리", "LA_SN_NUMB");
	
			fAddAdapter("품목대분류",     "BC_ITEM_LA_H");
			fAddAdapter("품목중분류",     "BC_ITEM_MA_H");
			fAddAdapter("품목",           "BC_ITEM_INFO_H");  //
			fAddAdapter("담당자",         "BE_DEPT_EMP");
			
			//adapter.addItem(new DataGetItem("담당자",         "BE_DEPT_EMP",    ls_modi_date, "대기"));
			listView.setAdapter(adapter);
			//<<<---리스트뷰<<<---

		} finally {
 			if ( pd_bef != null ) pd_bef.hide();
			
		}
		
	}
	
	public void f_dataget() {
		
		try
		{
			int li_count = adapter.getCount();
			ipbtitle.setMax(li_count);
			
			String ls_table_code = null, ls_table_name = null, ls_next_table_name = null, ls_flag;
			int li_chk = 1, li_fail = 0, li_next;

			for (int i = 1; i <= li_count; i++){
				DataGetItem ldgi = (DataGetItem)adapter.getItem(i - 1);
				ls_table_name = ldgi.getData(0);
				ls_table_code = ldgi.getData(1);
				ls_flag = ldgi.getData(3);
				
				if (i >= li_count) {
					li_next = i - 1;
				} else {
					li_next = i;
				}
				DataGetItem ldgi2 = (DataGetItem)adapter.getItem(li_next);
				ls_next_table_name = ldgi2.getData(0);
				//Toast.makeText(getBaseContext(), i + ")" + ls_table_code, Toast.LENGTH_SHORT).show();
				
	    		//Log.i(TAG,"for" + i + ", table_code : " + ls_table_code);

	    		if (ls_flag.equals("대기") || ls_flag.equals("실패")) {
	    			//Log.i(TAG, ls_table_code + "-" + ls_table_name);
	    			if (fChk_Table(ls_table_code, ls_table_name) == false) {li_chk = 9; li_fail++;} else {li_chk = 2;}  //9.실패, 0.대기 1.진행 2.완료 3.일치
	    			
	    		} else {
	    			//Log.i(TAG, ls_table_code + "일치 : " + ls_flag);
	    			li_chk = 100;
	    		}
	    		
	    		Message msg = handler.obtainMessage();  //-------------------------------------------------------------------------------------------------------
				msg.obj = ls_next_table_name;
				msg.arg1 = li_chk;   //마지막 건 성공유무(9.실패, 0.대기 1.진행 2.완료 3.일치)
				msg.arg2 = li_fail;  //최종실패건수
				handler.sendMessage(msg);
				
			}

			
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private boolean fChk_Table(String ls_table_code, String ls_table_name) {  //품목
		String ls_select = null, ls_insert = null;
		try
		{
			if (DBH.fCreate(ls_table_code) == false) {
				Log.i(TAG, ls_table_name + "(" + ls_table_code + ")를 생성중에 오류가 발생하였습니다.");
				Util.f_dialog(DataGet_Detail.this, ls_table_name + "(" + ls_table_code + ")를 생성중에 오류가 발생하였습니다.");
				return false;
			}
			
			try {
				DBH.DB.execSQL("DELETE FROM " + ls_table_code);
			} catch (SQLiteException es) {
				Log.e(TAG, "DELETE FROM " + ls_table_code);
				Util.f_dialog(DataGet_Detail.this, ls_table_name + "(" + ls_table_code + ") 초기화 과정중에 오류가 발생하였습니다.");
				return false;
			}

			//----->>>>>실데이타를 가져온다.
			ls_select = fGetSelect(ls_table_code);
			if (Util.f_isnull(ls_select)) return false;
			
			f_web_select(ls_select);
			
	 		//Log.i(TAG, ls_table_code + "[결과 : " + gsDbRtn + " ----- 전체건수 : " + gjArr.length() + "-------------]");
	
	 		DBH.DB.beginTransaction();
	 		try {
		    	for (int i = 0; i < gjArr.length(); i++) {
		    		JSONObject jObj = gjArr.getJSONObject(i);  //추출
		    		
		    		ls_insert = fGetInsert(ls_table_code, jObj);
		    		if (Util.f_isnull(ls_insert)) return false;
		    		
	    			DBH.DB.execSQL(ls_insert);
		    		
		    	}
	 			DBH.DB.execSQL("UPDATE AZ_MODI_DATE SET MODI_DATE_M = MODI_DATE, USE_FLAG = '1' WHERE TABLE_CODE = '" + ls_table_code + "'");
		    	DBH.DB.setTransactionSuccessful();  //commit;

	 		} catch (SQLiteException es) {
	 			Log.e(TAG,"INSERT INTO " + ls_table_code + " : " + ls_insert);
				//Util.f_dialog(DataGet_Detail.this, ls_table_name + "(" + ls_table_code + ") 데이타 INSERT 중에 오류가 발생하였습니다.");
	 			return false;
	 		} finally {
	 			DBH.DB.endTransaction();
	 		}
	    	
	 		//Log.i(TAG, "gsDbRtn : " + gsDbRtn + "," + gjArr.length());
			return true;
	
		} catch (Exception e) {
			e.printStackTrace();
 			Log.e(TAG, "fHttpGet : " + ls_select);
			//Util.f_dialog(DataGet_Detail.this, ls_table_name + "(" + ls_table_code + ") 데이타 설정중에 오류가 발생하였습니다.");
			return false;
		}
		
	}
	
	private String fGetSelect(String as_table_code) {
		String ls_select = null;
		
		if (as_table_code == "LA_MAKE") {                                   //제조사
			ls_select = "SELECT MAKE_CODE, MAKE_NAME, USE_FLAG, SORT_KEY, BIGO  FROM LA_MAKE";
		} else if (as_table_code == "LA_FACT") {                            //공장코드
			ls_select = "SELECT FACT_CODE, FACT_NAME, SORT_KEY, USE_FLAG, BIGO  FROM LA_FACT";
		} else if (as_table_code == "LA_MAKE_FACT") {                       //제조사공장
			ls_select = "SELECT MAKE_FACT_SEQ, MAKE_CODE, FACT_CODE, SORT_KEY, USE_FLAG, BIGO  FROM LA_MAKE_FACT";
		} else if (as_table_code == "LA_FACT_SUB") {                        //세부공장코드
			ls_select = "SELECT FACT_SUB_CODE, FACT_SUB_NAME, SORT_KEY, USE_FLAG, BIGO  FROM LA_FACT_SUB";
		} else if (as_table_code == "LA_FACT_DETAIL") {                     //세부공장
			ls_select = "SELECT FACT_DETL_SEQ, MAKE_CODE, FACT_CODE, FACT_SUB_CODE, SORT_KEY, USE_FLAG, BIGO  FROM LA_FACT_DETAIL";
		} else if (as_table_code == "LA_LINE") {                            //작업라인코드
			ls_select = "SELECT LINE_CODE, LINE_NAME, SORT_KEY, USE_FLAG, BIGO  FROM LA_LINE";
		} else if (as_table_code == "LA_FACT_LINE") {                       //작업라인
			ls_select = "SELECT FACT_LINE_SEQ, MAKE_CODE, FACT_CODE, FACT_SUB_CODE, LINE_CODE, SORT_KEY, USE_FLAG, BIGO  FROM LA_FACT_LINE";
		} else if (as_table_code == "LA_CHEK") {                            //조치사항
			ls_select = "SELECT CHEK_CODE, CHEK_NAME, SORT_KEY, USE_FLAG, BIGO  FROM LA_CHEK";
		} else if (as_table_code == "LA_CASE") {                            //점검항목
			ls_select = "SELECT CASE_CODE, CASE_NAME, SORT_KEY, USE_FLAG, BIGO  FROM LA_CASE";
		} else if (as_table_code == "LA_CASE_GRUP") {                       //품목분류별점검항목
			ls_select = "SELECT ITEM_LA_CODE, ITEM_MA_CODE, CASE_CODE, SORT_KEY, USE_FLAG, BIGO  FROM LA_CASE_GRUP";
		} else if (as_table_code == "LA_MAKER") {                           //메이커코드
			ls_select = "SELECT MAKER_CODE, MAKER_NAME, SORT_KEY, USE_FLAG, BIGO  FROM LA_MAKER";
		} else if (as_table_code == "LA_MAKER_ITEM_SPEC") {                 //메이커별품목규격관리
			ls_select = "SELECT MAKER_CODE, ITEM_SPEC, USE_FLAG  FROM LA_MAKER_ITEM_SPEC";
		} else if (as_table_code == "LA_BAR_LA") {                          //제품구분
			ls_select = "SELECT BAR_LA_CODE, BAR_LA_NAME, SORT_KEY, USE_FLAG, BIGO  FROM LA_BAR_LA";
		//} else if (as_table_code == "LA_SN_NUMB") {                         //시리얼번호관리
		} else if (as_table_code == "BC_ITEM_LA_H") {                       //품목대분류
			ls_select = "SELECT ITEM_LA_CODE, ITEM_LA_NAME, BIGO" +
               "  FROM BC_ITEM_LA_H";
		} else if (as_table_code == "BC_ITEM_MA_H") {                       //품목중분류
			ls_select = "SELECT ITEM_LA_CODE, ITEM_MA_CODE, ITEM_MA_NAME, BIGO" +
               "  FROM BC_ITEM_MA_H";
		} else if (as_table_code == "BC_ITEM_INFO_H") {                     //품목
			ls_select = "SELECT ITEM_LA_CODE, ITEM_MA_CODE, ITEM_CODE, ITEM_NAME, ITEM_SPEC" +
	            "    FROM BC_ITEM_INFO_H" +
					"   WHERE ITEM_CODE > ' '";
			
		} else if (as_table_code == "BE_DEPT_EMP") {                        //담당자
			ls_select = "SELECT EMP.DEPT_CODE, DEPT.DEPT_NAME, EMP.EMP_CODE, EMP.EMP_NAME" +
               "  FROM BE_DEPT_EMP EMP," +
               "       BF_DEPT_INFO DEPT" +
               " WHERE EMP.DEPT_CODE = DEPT.DEPT_CODE(+)";
		}
		
		
		return ls_select;
	}

	private String fGetInsert(String as_table_code, JSONObject jObj) {
		String ls_insert = null;
		
 		try {
			if (as_table_code == "LA_MAKE") {                                  //제조사
		 		ls_insert =	"INSERT INTO LA_MAKE" +
				      "           (MAKE_CODE, MAKE_NAME, USE_FLAG, SORT_KEY, BIGO)" +
				      "    VALUES ('" + jObj.getString("MAKE_CODE") + "', '" + 
                                    jObj.getString("MAKE_NAME").replace("'", "''") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_FACT") {                            //공장코드
		 		ls_insert =	"INSERT INTO LA_FACT" +
				      "           (FACT_CODE, FACT_NAME, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("FACT_CODE") + "', '" + 
                                    jObj.getString("FACT_NAME").replace("'", "''") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_MAKE_FACT") {                       //제조사공장
		 		ls_insert =	"INSERT INTO LA_MAKE_FACT" +
				      "           (MAKE_FACT_SEQ, MAKE_CODE, FACT_CODE, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("MAKE_FACT_SEQ") + "', '" + 
                                    jObj.getString("MAKE_CODE") + "', '" + 
                                    jObj.getString("FACT_CODE") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_FACT_SUB") {                        //세부공장코드
		 		ls_insert =	"INSERT INTO LA_FACT_SUB" +
				      "           (FACT_SUB_CODE, FACT_SUB_NAME, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("FACT_SUB_CODE") + "', '" + 
                                    jObj.getString("FACT_SUB_NAME").replace("'", "''") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_FACT_DETAIL") {                     //세부공장
		 		ls_insert =	"INSERT INTO LA_FACT_DETAIL" +
				      "           (FACT_DETL_SEQ, MAKE_CODE, FACT_CODE, FACT_SUB_CODE, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("FACT_DETL_SEQ") + "', '" + 
                                    jObj.getString("MAKE_CODE") + "', '" + 
                                    jObj.getString("FACT_CODE") + "', '" + 
                                    jObj.getString("FACT_SUB_CODE") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_LINE") {                            //작업라인코드
		 		ls_insert =	"INSERT INTO LA_LINE" +
				      "           (LINE_CODE, LINE_NAME, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("LINE_CODE") + "', '" + 
                                    jObj.getString("LINE_NAME").replace("'", "''") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_FACT_LINE") {                       //작업라인
		 		ls_insert =	"INSERT INTO LA_FACT_LINE" +
				      "           (FACT_LINE_SEQ, MAKE_CODE, FACT_CODE, FACT_SUB_CODE, LINE_CODE, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("FACT_LINE_SEQ") + "', '" + 
                                    jObj.getString("MAKE_CODE") + "', '" + 
                                    jObj.getString("FACT_CODE") + "', '" + 
                                    jObj.getString("FACT_SUB_CODE") + "', '" + 
                                    jObj.getString("LINE_CODE") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_CHEK") {                            //조치사항
		 		ls_insert =	"INSERT INTO LA_CHEK" +
				      "           (CHEK_CODE, CHEK_NAME, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("CHEK_CODE") + "', '" + 
                                    jObj.getString("CHEK_NAME").replace("'", "''") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_CASE") {                            //점검항목
		 		ls_insert =	"INSERT INTO LA_CASE" +
				      "           (CASE_CODE, CASE_NAME, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("CASE_CODE") + "', '" + 
                                    jObj.getString("CASE_NAME").replace("'", "''") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_CASE_GRUP") {                       //품목분류별점검항목
		 		ls_insert =	"INSERT INTO LA_CASE_GRUP" +
				      "           (ITEM_LA_CODE, ITEM_MA_CODE, CASE_CODE, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("ITEM_LA_CODE") + "', '" + 
                                    jObj.getString("ITEM_MA_CODE") + "', '" + 
                                    jObj.getString("CASE_CODE") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_MAKER") {                           //메이커코드
		 		ls_insert =	"INSERT INTO LA_MAKER" +
				      "           (MAKER_CODE, MAKER_NAME, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("MAKER_CODE") + "', '" + 
                                    jObj.getString("MAKER_NAME").replace("'", "''") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			} else if (as_table_code == "LA_MAKER_ITEM_SPEC") {                 //메이커별품목규격관리
		 		ls_insert =	"INSERT INTO LA_MAKER_ITEM_SPEC" +
				      "           (MAKER_CODE, ITEM_SPEC, USE_FLAG)" +
				      "    VALUES ('" + jObj.getString("MAKER_CODE") + "', '" + 
                                    jObj.getString("ITEM_SPEC").replace("'", "''") + "', '" + 
                                    jObj.getString("USE_FLAG") + "')";
			} else if (as_table_code == "LA_BAR_LA") {                           //제품구분
		 		ls_insert =	"INSERT INTO LA_BAR_LA" +
				      "           (BAR_LA_CODE, BAR_LA_NAME, SORT_KEY, USE_FLAG, BIGO)" +
				      "    VALUES ('" + jObj.getString("BAR_LA_CODE") + "', '" + 
                                    jObj.getString("BAR_LA_NAME").replace("'", "''") + "', '" + 
                                    jObj.getString("SORT_KEY") + "', '" + 
                                    jObj.getString("USE_FLAG") + "', '" + 
                                    jObj.getString("BIGO").replace("'", "''") + "')";
			//} else if (as_table_code == "LA_SN_NUMB") {                         //시리얼번호관리
			} else if (as_table_code == "BC_ITEM_LA_H") {                      //품목대분류
		 		ls_insert =	"INSERT INTO BC_ITEM_LA_H" +
				      "           (ITEM_LA_CODE, ITEM_LA_NAME, BIGO)" +
				      "    VALUES ('" + jObj.getString("ITEM_LA_CODE") + "', '" + 
				                        jObj.getString("ITEM_LA_NAME").replace("'", "''") + "', '" + 
				                        jObj.getString("BIGO") + "')";
			} else if (as_table_code == "BC_ITEM_MA_H") {                      //품목중분류
		 		ls_insert =	"INSERT INTO BC_ITEM_MA_H" +
				      "           (ITEM_LA_CODE, ITEM_MA_CODE, ITEM_MA_NAME, BIGO)" +
				      "    VALUES ('" + jObj.getString("ITEM_LA_CODE") + "', '" + 
				                        jObj.getString("ITEM_MA_CODE") + "', '" + 
                                    jObj.getString("ITEM_MA_NAME").replace("'", "''") + "', '" + 
				                        jObj.getString("BIGO") + "')";
			} else if (as_table_code == "BC_ITEM_INFO_H") {
		 		ls_insert =	"INSERT INTO BC_ITEM_INFO_H" +
					      "           (ITEM_LA_CODE, ITEM_MA_CODE, ITEM_CODE, ITEM_NAME, ITEM_SPEC)" +
					      "    VALUES ('" + jObj.getString("ITEM_LA_CODE") + "', '" + 
					                        jObj.getString("ITEM_MA_CODE") + "', '" + 
					                        jObj.getString("ITEM_CODE") + "', '" + 
					                        jObj.getString("ITEM_NAME").replace("'", "''") + "', '" + 
					                        jObj.getString("ITEM_SPEC").replace("'", "''") + "')";
		 		
		 		//Log.i(TAG, "ls_insert:" + ls_insert);
			} else if (as_table_code == "BE_DEPT_EMP") {                        //담당자
		 		ls_insert =	"INSERT INTO BE_DEPT_EMP" +
					      "           (DEPT_CODE, DEPT_NAME, EMP_CODE, EMP_NAME)" +
					      "    VALUES ('" + jObj.getString("DEPT_CODE") + "', '" + 
					                        jObj.getString("DEPT_NAME").replace("'", "''") + "', '" + 
					                        jObj.getString("EMP_CODE") + "', '" + 
					                        jObj.getString("EMP_NAME").replace("'", "''") + "')";
			}

			ls_insert = ls_insert.replace(", 'null'", ", null");

		} catch (Exception e) {
			e.printStackTrace();
 			Log.e(TAG, "fGetInsert : " + ls_insert);
			//Util.f_dialog(DataGet_Detail.this, as_table_code + " 데이타 INSERT 중에 오류가 발생하였습니다.");
			return "";
 		}
		
		
		return ls_insert;
	}

	public class ProgressHandler extends Handler {
		public void handleMessage(Message msg) {
			DataGetItem selectItem = (DataGetItem)adapter.getItem(ipbtitle.getProgress());
			
			String mData[] = selectItem.getData();
			
   		//9.실패, 0.대기 1.진행 2.완료 3.일치
   		if (msg.arg1 == 0) {
   			mData[3] = "대기"; //변경된 내용만 변수에 적용
   		} else if (msg.arg1== 1) {
   			mData[3] = "진행";
   		} else if (msg.arg1== 2) {
   			mData[3] = "완료";
   		} else if (msg.arg1== 3) {
   			mData[3] = "일치";
   		} else if (msg.arg1== 9) {
   			mData[3] = "실패";
   		} else if (msg.arg1== 100) {
   			//아무것도 안함
   		} else {
   			mData[3] = "오류(" + msg.arg1 + ")";
   		}
   		selectItem.setData(mData);   //적용된 변수를 SetData
			
   		adapter.notifyDataSetChanged();  //변경사항을 refalsh
			
   		   		
			ipbtitle.incrementProgressBy(1);
			if (ipbtitle.getProgress() == ipbtitle.getMax()) {
				
				String ls_insert, ls_update, ls_datetime;
				Date ld_date = new Date(); 
				SimpleDateFormat lsd_datetime = new SimpleDateFormat("yyyyMMdd hhmmssss"); 
				ls_datetime = lsd_datetime.format(ld_date).substring(0, 17);
				
				Cursor curSelect = DBH.DB.rawQuery("SELECT COUNT(1) CNT FROM AZ_MODI_DATE WHERE TABLE_CODE = 'AZ_MODI_DATE'", null);  //최근 기초데이타 수신일 관리
				curSelect.moveToNext();
	    		if (curSelect.getInt(curSelect.getColumnIndex("CNT")) <= 0) {
			 		ls_insert =	"INSERT INTO AZ_MODI_DATE" +
    				      "           (TABLE_CODE, MODI_DATE, MODI_DATE_M, VER_M, USE_FLAG)" +
    				      "    VALUES ('AZ_MODI_DATE', '" + 
    				                        "19000101 00000000" + "', '" + 
    				                        ls_datetime + "', '" + 
    				                        DBH.VER + "', '" +
                                       '0' + "')";
			 		DBH.DB.execSQL(ls_insert);
			 		//Log.i(TAG, ls_insert);
	    		} else {
	    			ls_update = "UPDATE AZ_MODI_DATE" +
						         "   SET MODI_DATE_M = '" + ls_datetime + "'," +
							      "       USE_FLAG = '" + '0' + "'" +
 							      " WHERE TABLE_CODE = 'AZ_MODI_DATE'";
	    			DBH.DB.execSQL(ls_update);
	    			//Log.i(TAG, ls_update);
	    		}
	    		curSelect.close();
				
				
				
				if ( pd != null ) pd.hide();
				itvtitle.setText(R.string.app_name);
				if (msg.arg2 <= 0) {
					//완료후 자동으로 창 닫음
					new AlertDialog.Builder(DataGet_Detail.this)
					.setTitle(R.string.MSG_TITLE)
					.setIcon(R.drawable.title_icon)
					.setMessage("기초 데이타 수신작업을 완료했습니다.")
					.setPositiveButton("확인", mClickLeft)
					.show();	
				} else {
					Util.f_dialog(DataGet_Detail.this, "기초 데이타 수신작업을 실패했습니다.\n * 총 " + (ipbtitle.getMax()) + "건중 " + msg.arg2 + "건 실패!");
				}
			} else {
         	itvtitle.setText((String) msg.obj);
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
}
