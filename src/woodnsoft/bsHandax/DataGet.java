package woodnsoft.bsHandax;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import woodnsoft.bsHandax.common.DBActivity;
import woodnsoft.bsHandax.common.Util;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("HandlerLeak")
public class DataGet extends DBActivity implements OnItemSelectedListener, OnClickListener, OnDismissListener, OnCancelListener  {
	private static final String TAG = "DataGet";

	String[] isv_make, isv_make_fact;
	Cursor curSelect;
	
	String is_date_time, is_date, is_time;
	
	//프로그래스 쓰레드 관련
	//ProgressHandler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dataget);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		if (checkNetwokState() == false){
			new AlertDialog.Builder(DataGet.this)
			.setTitle(R.string.MSG_TITLE)
			.setIcon(R.drawable.title_icon)
			.setMessage("인터넷에 연결할 수 없습니다.\nBS DATA 수신은 인터넷 연결이 선행되야 합니다.")
			.setPositiveButton("확인", mClickLeft)
			.show();
		} else {
					        	
			if (f_seri_appr_chk() == true) 
				if (f_ver_chk() == true) { //버젼체크
					handler.postDelayed(new Runnable () {public void run() {
						pd_bef = ProgressDialog.show(DataGet.this, "","기초 데이타 최신 유무를 확인중입니다...", true);
						Thread thread2 = new Thread(new Runnable() {
							public void run() {
								// TODO Auto-generated method stub
								fChk_AZ_MODI_DATE();  //최종수정일관리
							}
						});
						thread2.start();
						
				    }}, 300);					
				}
		}

		//--->>>스피너--->>>
		Spinner sp_make = (Spinner) findViewById(R.id.sp_make);
		sp_make.setOnItemSelectedListener(this);
		//<<<---스피너<<<---
		
		//기초 데이타 수신...
		Button lbdataget = (Button) findViewById(R.id.bDataGet);
		lbdataget.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
        		Intent liDataGet_Detail = new Intent(getBaseContext(), DataGet_Detail.class);
        		startActivity(liDataGet_Detail);

			}
		});

		//--->>>임시--->>>
		//lbdataget.performClick();  //클릭실행
		//<<<---임시<<<---

		
		//과거 BS점검내역 수신...
		Button lb_insp = (Button) findViewById(R.id.b_insp);
		lb_insp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Spinner sp_make = (Spinner) findViewById(R.id.sp_make);
				Spinner sp_make_fact = (Spinner) findViewById(R.id.sp_make_fact);
				String ls_make_code, ls_fact_code;
				
				ls_make_code = sp_make.getSelectedItem().toString();       //제조사코드
				ls_fact_code = sp_make_fact.getSelectedItem().toString();  //공장코드
				
				if ((ls_make_code.indexOf(">. ") <= 0) || (ls_fact_code.indexOf(">. ") <= 0)) {
					Util.f_dialog(DataGet.this,"제조사, 공장을 먼저 선택하십시오!");
					return;
				}
				
				Toast.makeText(getApplicationContext(), "제조사 : " + ls_make_code.substring(6) + ", 공장 : " + ls_fact_code.substring(6) + "\n을 선택하셨습니다!", Toast.LENGTH_LONG).show();

		   	Bundle bundle = new Bundle();
   			bundle.putString("make_code", ls_make_code.substring(0, 3));
   			bundle.putString("make_name", ls_make_code.substring(6));
   			bundle.putString("fact_code", ls_fact_code.substring(0, 3));
   			bundle.putString("fact_name", ls_fact_code.substring(6));
				
				
				Intent liDataGet_insp = new Intent(getBaseContext(), DataGet_insp.class);
				liDataGet_insp.putExtras(bundle);
   			startActivity(liDataGet_insp);
        		
			}
		});
		
		
		Button lbback = (Button) findViewById(R.id.bBack);  		lbback.setOnClickListener(this);	
		Button lb_dialog_seri_appr_chk = (Button) findViewById(R.id.b_dialog_seri_appr_chk);  		lb_dialog_seri_appr_chk.setOnClickListener(this);	
	}
	
	private boolean fChk_AZ_MODI_DATE() {  //최종수정일관리
		String ls_select = "", ls_update = "", ls_insert = "";
		
		try
		{
			if (DBH.fCreate("AZ_MODI_DATE") == false) {
				Util.f_dialog(DataGet.this,"최종수정일관리(AZ_MODI_DATE)를 생성중에 오류가 발생하였습니다.");
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
				Util.f_dialog(DataGet.this,"최종수정일관리(AZ_MODI_DATE) 데이타 INSERT 중에 오류가 발생하였습니다.");
	 			return false;
	 		} finally {
	 			DBH.DB.endTransaction();
	 		}
	    	
			return true;
	
		} catch (Exception e) {
			Log.e(TAG, "fHttpGet : " + ls_select);
			e.printStackTrace();
 			Util.f_dialog(DataGet.this,"최종수정일관리(AZ_MODI_DATE) 데이타 설정중에 오류가 발생하였습니다.");
			return false;
 		} finally {
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);

		}
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

 		
		try {
			if (DBH.fCreate("AZ_MODI_DATE") == false) {
				Util.f_dialog(DataGet.this,"최종수정일관리(AZ_MODI_DATE)를 생성중에 오류가 발생하였습니다.");
			}

			TextView ltvAZ_MODI_DATE = (TextView) findViewById(R.id.tv_az_modi_date);
			curSelect = DBH.DB.rawQuery("SELECT COUNT(1) CNT," +
					                      "       SUM(CASE WHEN MODI_DATE = MODI_DATE_M THEN 0 ELSE 1 END) DIFF" +
					                      "  FROM AZ_MODI_DATE" +
					                      " WHERE USE_FLAG = '1'", null);  //최근 기초데이타 유무

			if (curSelect.getCount() <= 0) {
				ltvAZ_MODI_DATE.setText("* 기초 데이타가 수신되지 않았습니다.");			
			} else {
				curSelect.moveToNext();
				long ll_cnt = curSelect.getLong(curSelect.getColumnIndex("CNT"));    //전체건수(기초데이타 총건수 : 17) - 사용안함
				long ll_diff = curSelect.getLong(curSelect.getColumnIndex("DIFF"));  //Oracle최종 수신일과 모바일 최종수신일이 다른 건수
				
				
				TextView ltvDataGet = (TextView) findViewById(R.id.tvDataGet);
				if ((ll_diff == 0) && (ll_cnt > 0)) {
					ltvDataGet.setText("* 최신 데이타입니다.");
					f_insp(true); //최신데이타이므로 과거 BS점검내역을 수신할수 있다.
				} else {
					ltvDataGet.setText("* 기초 데이타를 수신하십시오!");
					f_insp(false); //최신데이타가 아니므로 과거 BS점검내역을 수신할수 없다.
				}
			}
			curSelect.close();
		} catch (Exception e) {
 			Log.e(TAG, "최종 데이타 수신일 GET");
 			Util.f_dialog(DataGet.this,"기초 데이타 최신유무를 조회중에 오류가 발생하였습니다.");
 		}
		
		try {
			TextView ltvAZ_MODI_DATE = (TextView) findViewById(R.id.tv_az_modi_date);
			curSelect = DBH.DB.rawQuery("SELECT MODI_DATE_M FROM AZ_MODI_DATE WHERE TABLE_CODE = 'AZ_MODI_DATE'", null);  //최근 기초데이타 수신일 관리
			if (curSelect.getCount() <= 0) {
				ltvAZ_MODI_DATE.setText("* 최종 수신일시 : 미수신");			
			} else {
				curSelect.moveToNext();
				String ls_d = curSelect.getString(curSelect.getColumnIndex("MODI_DATE_M"));
				
				ltvAZ_MODI_DATE.setText("* 최종 수신일시 : " + String.format("%s년%s월%s일 (%s시%s분)", ls_d.substring(0, 4), ls_d.substring(4, 6), ls_d.substring(6, 8), ls_d.substring(9, 11), ls_d.substring(11, 13) ));
			}
			curSelect.close();
		} catch (Exception e) {
 			Log.e(TAG, "최종 데이타 수신일 GET");
 			Util.f_dialog(DataGet.this,"기초 데이타 최종 수신일 조회중에 오류가 발생하였습니다.");
 		}

		TextView ltvAZ_MODI_DATE2 = (TextView) findViewById(R.id.tv_az_modi_date2);
		TextView ltv_fact_name = (TextView) findViewById(R.id.tv_fact_name);
		if (Util.f_isnull(gs_last_get_date)) {
			ltvAZ_MODI_DATE2.setText("* 최종 수신일시 : 미수신");			
			ltv_fact_name.setText("* 최종 수신공장 : 미수신");			
		} else {
			ltvAZ_MODI_DATE2.setText("* 최종 수신일시 : " + String.format("%s년%s월%s일 (%s시%s분)", gs_last_get_date.substring(0, 4), gs_last_get_date.substring(4, 6), gs_last_get_date.substring(6, 8), gs_last_get_date.substring(9, 11), gs_last_get_date.substring(11, 13) ));
			ltv_fact_name.setText("* 최종 수신공장 : " + gs_make_name + " " + gs_fact_name);			
		}

	
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {    
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	         onClick(findViewById(R.id.bBack));       
	    }
	    return false;    
	}

	public boolean f_ver_chk() {  //버젼체크
		try
		{
			f_web_select("SELECT COMM VER FROM JC_SETUP_H WHERE GUBUN = '12' AND SEQ = '1'");
			
			if (gjArr.length() <= 0) {
				new AlertDialog.Builder(DataGet.this)
				.setTitle(R.string.MSG_TITLE)
				.setIcon(R.drawable.title_icon)
				.setMessage("서버에서 데이타를 조회할 수 없습니다.")
				.setPositiveButton("확인", mClickLeft)
				.show();
				
				return false;
			} else {
				JSONObject jObj = gjArr.getJSONObject(0);  //추출
				String ls_ver = jObj.getString("VER");
				
				return f_upgrade(ls_ver);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Util.f_dialog(DataGet.this,"서버에서 데이타를 가져오는중에 오류가 발생하였습니다.");
			return false;
		}
	}
	
	public boolean f_seri_appr_chk() {  //시리얼번호에 대한 승인유무 체크
		try
		{
			f_web_select("SELECT APPR_FLAG, USER_EMP_TEXT, USER_DATE, USER_TIME, APPR_DATE, APPR_TIME FROM LA_MOBI_APPR WHERE MOBI_SERI = '" + gs_serial_key + "'");
			
			if (gjArr.length() <= 0) {
				new AlertDialog.Builder(DataGet.this)
				.setTitle(R.string.MSG_TITLE)
				.setIcon(R.drawable.title_icon)
				.setMessage("해당 프로그램은 관리자의 승인 후 사용 하실 수 있습니다.\n\n" + 
						      "해당 기기에 대한 사용자 등록을 요청하시겠습니까?")
				.setPositiveButton("요청"    , new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int whichButton) {
						
						DataGet.this.onClick(findViewById(R.id.b_dialog_seri_appr_chk));					
						
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText(getBaseContext(), "요청 취소", Toast.LENGTH_SHORT).show();
						finish();
					}
				})
				.show();
				
				return false;
			} else {
				JSONObject jObj = gjArr.getJSONObject(0);  //추출
				
				String ls_appr_flag = jObj.getString("APPR_FLAG");
				String ls_user_emp_text = jObj.getString("USER_EMP_TEXT");
				String ls_user_date = jObj.getString("USER_DATE");
				String ls_user_time = jObj.getString("USER_TIME");
				String ls_appr_date = jObj.getString("APPR_DATE");
				String ls_appr_time = jObj.getString("APPR_TIME");
				
				if (ls_appr_flag.equals("2")) {  //1.요청 2.승인, 3.해제
					return true; //승인됨
				} else if (ls_appr_flag.equals("1")) {
					new AlertDialog.Builder(DataGet.this)
					.setTitle(R.string.MSG_TITLE)
					.setIcon(R.drawable.title_icon)
					.setMessage("사용자 등록을 관리자에게 요청한 상태입니다.\n" +
                           "해당 프로그램은 관리자의 승인 후 사용하실 수 있습니다.\n\n" +
                           "* 요청한 일시 : " + Util.getFormatDate(ls_user_date) + " " + Util.getFormatTime(ls_user_time) + "\n" +
                           "* 요청자 이름 : " + ls_user_emp_text)
					.setPositiveButton("확인"    , new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int whichButton) {
							//Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(gs_downfile)); 
							//startActivity(i);						
							finish();
							//BsHandaxActivity.g_BsHandaxActivity.finish();
						}
					}).show();
					
//					Util.f_dialog(DataGet.this, "사용자 등록을 관리자에게 요청한 상태입니다.\n" +
//							                      "해당 프로그램은 관리자의 승인 후 사용하실 수 있습니다.\n\n" +
//                                           "* 요청한 일시 : " + Util.getFormatDate(ls_user_date) + " " + Util.getFormatTime(ls_user_time) + "\n" +
//							                      "* 요청자 이름 : " + ls_user_emp_text);
					
					return false;
				} else if (ls_appr_flag.equals("3")) {
					
					new AlertDialog.Builder(DataGet.this)
					.setTitle(R.string.MSG_TITLE)
					.setIcon(R.drawable.title_icon)
					.setMessage("사용자 등록이 해제되어 프로그램을 사용하실 수 없습니다.\n" +
                           "관리자에게 문의하십시오!\n\n" +
                           "* 해제 일시 : " + Util.getFormatDate(ls_appr_date) + " " + Util.getFormatTime(ls_appr_time))
					.setPositiveButton("확인"    , new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int whichButton) {
							//Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(gs_downfile)); 
							//startActivity(i);						
							finish();
							//BsHandaxActivity.g_BsHandaxActivity.finish();
						}
					}).show();
					
//					Util.f_dialog(DataGet.this, "사용자 등록이 해제되어 프로그램을 사용하실 수 없습니다.\n" +
//                                           "관리자에게 문의하십시오!\n\n" +
//                                           "* 해제 일시 : " + Util.getFormatDate(ls_appr_date) + " " + Util.getFormatTime(ls_appr_time));
					return false;
				} else {
					new AlertDialog.Builder(DataGet.this)
					.setTitle(R.string.MSG_TITLE)
					.setIcon(R.drawable.title_icon)
					.setMessage("사용자 등록의 값을 가져오는 도중 알수 없는 오류가 발생하였습니다.\n" +
                           "* 알수없는 값 : " + ls_appr_flag)
					.setPositiveButton("확인"    , new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int whichButton) {
							//Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(gs_downfile)); 
							//startActivity(i);						
							finish();
							//BsHandaxActivity.g_BsHandaxActivity.finish();
						}
					}).show();
					
//					Util.f_dialog(DataGet.this, "사용자 등록의 값을 가져오는 도중 알수 없는 오류가 발생하였습니다.\n" +
//                     "* 알수없는 값 : " + ls_appr_flag);
					return false;
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			new AlertDialog.Builder(DataGet.this)
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
	
	public void onDismiss(DialogInterface ad_dialog) {
		// TODO Auto-generated method stub  
   		String ls_user_emp_text;
   		
			Dialog_seri_appr_chk dialog = (Dialog_seri_appr_chk) ad_dialog ;  
			ls_user_emp_text = dialog.getName().trim();
   		
			
			if (ls_user_emp_text.equals("WOODNSOFT.COM")) {  //관리자용으로 관리자 승인을 거치지 않음
				gs_serial_key = "none";
				return;
			}
			
			
			if (Util.f_isnull(ls_user_emp_text)) {
				new AlertDialog.Builder(DataGet.this)
				.setTitle(R.string.MSG_TITLE)
				.setIcon(R.drawable.title_icon)
				.setMessage("요청자 이름을 입력하십시오!")
				.setPositiveButton("확인"    , new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int whichButton) {
						DataGet.this.onClick(findViewById(R.id.b_dialog_seri_appr_chk));
						
					}
				}).show();
				
				return;
			}
			
			
			String ls_insert = null;
			try {  //순번 1
				ls_insert =
						"INSERT INTO LA_MOBI_APPR" +
						"           (REG_NO, MOBI_SERI, USER_EMP_TEXT, USER_DATE, USER_TIME, APPR_FLAG, VER, LAST_DATE, LAST_TIME)" +
						"    VALUES (FN_REG_NO_LA_MOBI_APPR('" + is_date + "'), "+
						            "'" + gs_serial_key + "', " +
						            "'" + Util.f_replace(ls_user_emp_text, "'", "''") + "', " +
						            "'" + is_date + "', " +
						            "'" + is_time + "', " +
						            "'1', " +
						            "'" + gs_ver + "', " +
						            "'" + is_date + "', " +
						            "'" + is_time + "')";
				
				f_web_exec(ls_insert);
				if (gsDbRtn.equals("1")) {  //1:성공 0:실패
					new AlertDialog.Builder(DataGet.this)
					.setTitle(R.string.MSG_TITLE)
					.setIcon(R.drawable.title_icon)
					.setMessage("관리자에게 사용자 등록을 요청하였습니다.\n" +
							      "관리자의 \"승인\"후 프로그램을 사용하실 수 있습니다.\n\n" +
							      "요청자 이름 : " + ls_user_emp_text)
					.setPositiveButton("확인"    , new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int whichButton) {
							//Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(gs_downfile)); 
							//startActivity(i);						
							finish();
							//BsHandaxActivity.g_BsHandaxActivity.finish();
						}
					}).show();

				} else {
					Log.e(TAG, "ls_insert(LA_MOBI_APPR):" + ls_insert);
					new AlertDialog.Builder(DataGet.this)
					.setTitle(R.string.MSG_TITLE)
					.setIcon(R.drawable.title_icon)
					.setMessage("관리자에게 사용자 등록을 요청 도중 오류가 발생하였습니다." +
							      "요청 작업을 다시 시도하십시오!")
					.setPositiveButton("확인"    , new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int whichButton) {
							//Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(gs_downfile)); 
							//startActivity(i);						
							finish();
							//BsHandaxActivity.g_BsHandaxActivity.finish();
						}
					}).show();
				}
					
			} catch (Exception e) {
	 			Log.e(TAG, "f_dataget_run- web_master_insert:" + ls_insert);
	 			
	 		}
			

   }
	
	public void onCancel(DialogInterface dialog) {
	     // TODO Auto-generated method stub
	     finish();
	}
	
	public boolean f_upgrade(String as_ver) {
		try {
			if ( gs_ver.equals(as_ver)) {
				//Util.f_dialog(DataGet.this, "현재 사용하시는 프로그램이 최신입니다.\n프로그램 버젼 : " + as_ver);
				return true;
			} else {
				new AlertDialog.Builder(DataGet.this)
				.setTitle("WoodNsoft")
				//.setIcon(R.drawable.woodn_logo1)
				.setMessage("업그레이드 버전(VER : " + as_ver + ")이 존재합니다.\n" +
						      "다운로드 완료후 프로그램을 재설치해주세요.\n\n" +
						      "VER : " + gs_ver)
				.setPositiveButton("다운로드"    , new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(gs_downfile)); 
						startActivity(i);						
						finish();
						BsHandaxActivity.g_BsHandaxActivity.finish();
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText(getBaseContext(), "업그레이드 취소", Toast.LENGTH_SHORT).show();
						finish();
					}
				})
				//.setNegativeButton("아니오", mClickLeft)
				.show();
				return false;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}			
	}
	
	public void f_insp(Boolean lbl_chk) {  //과거 BS점5검내역 수신 관련 결정(아규먼트 : lbl_chk.최신데이타 유무)
		Spinner sp_make = (Spinner) findViewById(R.id.sp_make);
		Spinner sp_make_fact = (Spinner) findViewById(R.id.sp_make_fact);
		Button  b_insp = (Button) findViewById(R.id.b_insp);

		sp_make.setEnabled(lbl_chk);
		sp_make_fact.setEnabled(lbl_chk);
		b_insp.setEnabled(lbl_chk);
		
		
		int li_count;
		String ls_make_code, ls_make_name;
		
		if (lbl_chk == true) {
			try {
				curSelect = DBH.DB.rawQuery(
						"   SELECT MAKE_CODE, MAKE_NAME" +
						"     FROM LA_MAKE" +
						"    WHERE USE_FLAG = '1'" +
						" ORDER BY SORT_KEY, MAKE_CODE", null);  //최근 기초데이타 유무
	
				li_count = curSelect.getCount();
				
				if (li_count <= 0) {
					isv_make = new String[1];
					isv_make[0] = "미존재";
				} else {
					isv_make = new String[li_count + 1];
					isv_make[0] = "선택하세요!";
					
					for (int i=1;i<li_count + 1;i++){
						curSelect.moveToNext();
						ls_make_code = curSelect.getString(curSelect.getColumnIndex("MAKE_CODE"));
						ls_make_name = curSelect.getString(curSelect.getColumnIndex("MAKE_NAME"));
						isv_make[i] = ls_make_code + ">. " + ls_make_name;
					}

				}
				curSelect.close();
				
				//--->>>스피너--->>>
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, isv_make);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp_make.setAdapter(adapter);
				//<<<---스피너<<<---
				
			} catch (Exception e) {
	 			Log.e(TAG, "제조사 SELECT");
	 			Util.f_dialog(DataGet.this,"제조사 조회중에 오류가 발생하였습니다.");
			}
			
		} else {
			
		}
		
		
	}
	
	public void fDataGet() {
		try
		{
			Toast.makeText(getBaseContext(), "fLogin", Toast.LENGTH_SHORT).show();
		}
		catch (Exception ex)
		{
		        ex.printStackTrace();
		} finally {
			//dismissDialog(PROGRESS2_KEY);
			if ( pd != null ) pd.hide();
		}
	}
	
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		
		Spinner sp_make_fact = (Spinner) findViewById(R.id.sp_make_fact);
		String ls_select = null;
		String ls_make_code, ls_fact_code, ls_fact_name;
		int li_count;

		ls_make_code = isv_make[position].substring(0, 3);

		try {
			ls_select = 
					"   SELECT MAKE_FACT_SEQ, MF.FACT_CODE, FACT.FACT_NAME" +
					"     FROM LA_MAKE_FACT MF," +
					"          LA_FACT FACT                 " +
					"    WHERE MF.FACT_CODE = FACT.FACT_CODE" +
					"      AND MF.USE_FLAG = '1'      " +
					"      AND MF.MAKE_CODE = '" + ls_make_code + "'" +
					" ORDER BY MF.SORT_KEY, MF.MAKE_CODE";
			
			curSelect = DBH.DB.rawQuery(ls_select, null);  //최근 기초데이타 유무
			
			li_count = curSelect.getCount();
			isv_make_fact = new String[li_count];
			
			if (li_count <= 0) {
				isv_make_fact = new String[1];
				isv_make_fact[0] = "미존재";
			} else {
				isv_make_fact = new String[li_count + 1];
				isv_make_fact[0] = "선택하세요!";
				
				for (int i=1;i<li_count + 1;i++){
					curSelect.moveToNext();
					ls_fact_code = curSelect.getString(curSelect.getColumnIndex("FACT_CODE"));
					ls_fact_name = curSelect.getString(curSelect.getColumnIndex("FACT_NAME"));
					isv_make_fact[i] = ls_fact_code + ">. " + ls_fact_name;
				}
			}
			curSelect.close();
			//--->>>스피너--->>>
			//sp_make.setOnItemSelectedListener(this);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, isv_make_fact);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_make_fact.setAdapter(adapter);
			//<<<---스피너<<<---
		} catch (Exception e) {
			e.printStackTrace();
 			Log.e(TAG, "제조사공장 SELECT:" + ls_select);
 			Util.f_dialog(DataGet.this,"제조사공장 조회중에 오류가 발생하였습니다.");
		}
		

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}


	//인터넷 연결 유무 체크
	public boolean checkNetwokState() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	
    	if (cm.getActiveNetworkInfo() != null) {
	        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	        switch (activeNetwork.getType()) {
		        case ConnectivityManager.TYPE_WIMAX: // 4g 망 체크
		        	return true;
		        case ConnectivityManager.TYPE_WIFI: // wifi망 체크
		        	return true;
		        case ConnectivityManager.TYPE_MOBILE: // 3g 망 체크
			        return true;
		        }
        }else {
        	return false;
            //Toast.makeText(this,"네트워크가 연결되어 있지 않습니다. 확인해주세요").show();
        	
        }
        
        return true;
    }
	
	public void onClick(View v) {
		
		if(v.equals(findViewById(R.id.bBack))) {  //뒤로
				finish();
		} else if(v.equals(findViewById(R.id.b_dialog_seri_appr_chk))) {  //뒤로

				Dialog_seri_appr_chk dialog1 = new Dialog_seri_appr_chk( this ) ;
				dialog1.setOnDismissListener( this ) ;
				dialog1.setOnCancelListener( this ) ;
				dialog1.show();
				
				TextView ltv_serial_key = (TextView) dialog1.findViewById(R.id.tv_serial_key);
				TextView ltv_user_date = (TextView) dialog1.findViewById(R.id.tv_user_date);
				TextView ltv_user_time = (TextView) dialog1.findViewById(R.id.tv_user_time);
				TextView ltv_user_date_time = (TextView) dialog1.findViewById(R.id.tv_user_date_time);
				//EditText let_user_emp_text = (EditText) dialog1.findViewById(R.id.et_user_emp_text);
				
				
				
				Date ld_date = new Date();
				SimpleDateFormat lsd_datetime = new SimpleDateFormat("yyyyMMdd HHmm");
				is_date_time = lsd_datetime.format(ld_date).substring(0, 13);
				is_date = is_date_time.substring(0, 8);
				is_time = is_date_time.substring(9);
				
				
				//is_item_spec = iet_item_spec.getText().toString();
				ltv_serial_key.setText(gs_serial_key);
				ltv_user_date.setText(is_date);
				ltv_user_time.setText(is_time);
				ltv_user_date_time.setText(Util.getFormatDate(is_date) + " " + Util.getFormatTime(is_time));
							
			
		}
	}
	

	DialogInterface.OnClickListener mClickLeft = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			if (whichButton == DialogInterface.BUTTON1) {
				finish();
				
			}
		}
	};
	
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			onStart();
				if ( pd_bef != null ) pd_bef.hide();
		}
	};	
}