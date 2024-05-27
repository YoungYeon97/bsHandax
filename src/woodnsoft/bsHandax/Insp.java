package woodnsoft.bsHandax;

import java.text.DateFormat;
import java.util.Calendar;

import woodnsoft.bsHandax.DataListView.DataPut.DataPut_Item;
import woodnsoft.bsHandax.DataListView.DataPut.DataPut_ListAdapter;
import woodnsoft.bsHandax.DataListView.DataPut.DataPut_ListView;
import woodnsoft.bsHandax.DataListView.DataPut.OnDataSelectionListener_DataPut;
import woodnsoft.bsHandax.common.DBActivity;
import woodnsoft.bsHandax.common.Util;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Insp extends DBActivity implements OnItemSelectedListener, OnClickListener {
	private static final String TAG = "Insp";
	DataPut_ListView listView;
	DataPut_ListAdapter adapter;
	
	Cursor curSelect;
	String[] isv_fact_sub, isv_insp;
	private EditText iet_reg_date;
	DateFormat fmtDateAndTime = DateFormat.getDateInstance();
	View flag_view;
	
	Spinner isp_fact_sub, isp_insp_reg_no, isp_emp;
	CheckBox icb_mobi_flag;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insp);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		//--->>>리스트뷰--->>>
		adapter = new DataPut_ListAdapter(this);
		listView = new DataPut_ListView(this);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.ll_list_dataput );
		linLayout.addView(listView);

		//--->>>임시--->>>
		//lbdataget.performClick();  //클릭실행
		//<<<---임시<<<---

		EditText let_make_name = (EditText) findViewById(R.id.et_make_name);
		let_make_name.setText(gs_make_code + ">. " + gs_make_name);
		EditText let_fact_name = (EditText) findViewById(R.id.et_fact_name);
		let_fact_name.setText(gs_fact_code + ">. " + gs_fact_name);

		//--->>>스피너--->>>
		isp_fact_sub = (Spinner) findViewById(R.id.sp_fact_sub);
		isp_fact_sub.setOnItemSelectedListener(this);
		isp_insp_reg_no = (Spinner) findViewById(R.id.sp_insp_reg_no);
		isp_emp = (Spinner) findViewById(R.id.sp_emp);
		//<<<---스피너<<<---
		icb_mobi_flag = (CheckBox) findViewById(R.id.cb_mobi_flag);
		
		
		
		isp_insp_reg_no.setOnItemSelectedListener(this);
		isp_emp.setOnItemSelectedListener(this);
		icb_mobi_flag.setOnClickListener(this);
		
		
		findViewById(R.id.bBack).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
	    		Toast.makeText(getBaseContext(), "뒤로", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		
		//실행
		findViewById(R.id.bOk).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pd = ProgressDialog.show(Insp.this, "","점검내역을 불러오고 있습니다.\n잠시만 기다려 주십시오!", true);		        	
				Handler v1 = new Handler();
				v1.postDelayed(
			    		new Runnable () {
							public void run() {
								//finish();
								f_open_insp_search(0);
							}
			    		}
			    		, 300);
			}
		});

		iet_reg_date = (EditText) findViewById(R.id.et_reg_date);
		iet_reg_date.setText(CUR_DATE);  //초기값으로 오늘로 셋팅
		iet_reg_date.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
        		flag_view = v;
        		new DatePickerDialog(Insp.this, dateListener, gcal1.get(Calendar.YEAR), gcal1.get(Calendar.MONTH), gcal1.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		int li_count;
		try {
			curSelect = DBH.DB.rawQuery(" SELECT FD.FACT_SUB_CODE, FS.FACT_SUB_NAME" +
	                                  "   FROM LA_FACT_DETAIL FD, LA_FACT_SUB FS" +
	                                  "  WHERE FD.FACT_SUB_CODE = FS.FACT_SUB_CODE" +
	                                  "    AND FD.MAKE_CODE = '" + gs_make_code + "' AND FD.FACT_CODE = '" + gs_fact_code + "'" +
	                                  " ORDER BY FS.SORT_KEY, FD.FACT_SUB_CODE", null);  //세부공장리스트
			
			
			li_count = curSelect.getCount();
			isv_fact_sub = new String[li_count];
			String ls_fact_sub_code, ls_fact_sub_name;
			
			if (li_count <= 0) {
				isv_fact_sub = new String[1];
				isv_fact_sub[0] = "미존재";
				isp_insp_reg_no.setEnabled(false);
				
			} else {
				isv_fact_sub = new String[li_count + 1];
				isv_fact_sub[0] = "선택하세요!";
				isp_insp_reg_no.setEnabled(true);
				
				for (int i=1;i<li_count + 1;i++){
					curSelect.moveToNext();
					ls_fact_sub_code = curSelect.getString(curSelect.getColumnIndex("FACT_SUB_CODE"));
					ls_fact_sub_name = curSelect.getString(curSelect.getColumnIndex("FACT_SUB_NAME"));
					isv_fact_sub[i] = ls_fact_sub_code + ">. " + ls_fact_sub_name;
				}
			}
			curSelect.close();

			//--->>>스피너--->>>
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, isv_fact_sub);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			isp_fact_sub.setAdapter(adapter);
			//<<<---스피너<<<---

		} catch (Exception e) {
				Log.e(TAG, "최종 데이타 수신일 GET");
				Util.f_dialog(Insp.this,"기초 데이타 최종 수신일 조회중에 오류가 발생하였습니다.");
		}

		String ls_reg_no, ls_strt_date, ls_clos_date;
	
		try {
			curSelect = DBH.DB.rawQuery(
					"   SELECT REG_NO, STRT_DATE, CLOS_DATE" +
					"     FROM LB_INSP" +
					"    WHERE ING_FLAG LIKE '1'" +  //진행구분(1.진행 2.완료)
					"      AND MAKE_CODE = '" + gs_make_code + "'" +
					"      AND FACT_CODE = '" + gs_fact_code + "'" +
					" ORDER BY REG_NO", null);  //점검내역
	
			li_count = curSelect.getCount();
			//isv_insp = new String[li_count];
			
			if (li_count <= 0) {
				isv_insp = new String[1];
				isv_insp[0] = "미존재";
			} else {
				isv_insp = new String[li_count + 1];
				isv_insp[0] = "전표번호를 선택하세요!";
				
				for (int i=1;i<li_count + 1;i++){
					curSelect.moveToNext();
					ls_reg_no = curSelect.getString(curSelect.getColumnIndex("REG_NO"));
					ls_strt_date = curSelect.getString(curSelect.getColumnIndex("STRT_DATE")).substring(4);
					ls_clos_date = curSelect.getString(curSelect.getColumnIndex("CLOS_DATE")).substring(4);
					
					isv_insp[i] = String.format("%s-%s", ls_reg_no.substring(0, 4), ls_reg_no.substring(4)) + 
							        " (" + String.format("%s.%s", ls_strt_date.substring(0, 2), ls_strt_date.substring(2)) +
							        " ~ "  + String.format("%s.%s", ls_clos_date.substring(0, 2), ls_clos_date.substring(2)) + ")";
				}
			}
			curSelect.close();
			
			//--->>>스피너--->>>
			//sp_make.setOnItemSelectedListener(this);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, isv_insp);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			isp_insp_reg_no.setAdapter(adapter);
			//<<<---스피너<<<---
			
		} catch (Exception e) {
				Log.e(TAG, "제조사공장 SELECT");
	//			Util.f_dialog(DataGet.this,"제조사공장 조회중에 오류가 발생하였습니다.");
		}
	
		
		listView.setOnDataSelectionListener( new OnDataSelectionListener_DataPut () {
         
   		public void onDataSelected(AdapterView<?> parent, View v, final int position, long id) {
   			pd = ProgressDialog.show(Insp.this, "","점검내역을 불러오고 있습니다.\n잠시만 기다려 주십시오!", true);		        	
				Handler v1 = new Handler();
				v1.postDelayed(
			    		new Runnable () {
							public void run() {
								//finish();
								f_open_insp_search(position);
							}
			    		}
			    		, 300);

                
   		}
		});        		
		
		
		try {
			f_setp();  //환경설정값 다시 읽어들임
			
			if (Util.f_isnull(gs_fact_sub_code)) return;
			
			int li_cnt, li_selection;
	
			li_cnt = isp_fact_sub.getCount();
			li_selection = 0;
			for(int i =0;i<li_cnt;i++) {
				if (gs_fact_sub_code.equals(isp_fact_sub.getItemAtPosition(i).toString().substring(0, 3))) {
					li_selection = i;
					break;
				}
			}
			if (li_selection > 0) isp_fact_sub.setSelection(li_selection, true);
			
			li_cnt = isp_insp_reg_no.getCount();
			li_selection = 0;
			
			if (isp_insp_reg_no.getItemAtPosition(0).toString().equals("미존재") == false) {
				String lb_insp_reg_no = gs_insp_reg_no.substring(0, 4) + "-" + gs_insp_reg_no.substring(4); 
				//----------
				//String ls_insp_reg_no = isp_insp_reg_no.getItemAtPosition(0).toString();
				
					for(int i =0;i<li_cnt;i++) {
						if (lb_insp_reg_no.equals(isp_insp_reg_no.getItemAtPosition(i).toString().substring(0, 9))) {
							li_selection = i;
							break;
						}
					}
				
					if (li_selection > 0) isp_insp_reg_no.setSelection(li_selection, true);
			}
			
			//onItemSelected(sp_insp_reg_no, sp_insp_reg_no, li_selection, 0);
			
			Spinner sp_emp = (Spinner) findViewById(R.id.sp_emp);
			li_cnt = sp_emp.getCount();
			li_selection = 0;
			for(int i =0;i<li_cnt;i++) {
				li_selection = i;
				if (gs_emp_code.equals(sp_emp.getItemAtPosition(i).toString().substring(0, 5))) {
					break;
				}
			}
			
			if (li_selection > 0) sp_emp.setSelection(li_selection, true);
			
		} catch (Exception e) {
			Log.e(TAG, "세부공장/점검코드/작업자 로딩오류");
//			Util.f_dialog(DataGet.this,"제조사공장 조회중에 오류가 발생하였습니다.");
		}		
		
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();


		
	}
	
	public void f_open_insp_search(int ai_select) {
		try {
			String ls_fact_sub, ls_fact_sub_code, ls_fact_sub_name;
			String ls_insp, ls_insp_reg_no;
			String ls_emp, ls_emp_code, ls_emp_name;
	
			if (icb_mobi_flag.isChecked() == false) {  //과거점검내역 미체크 - 입력모드로 들어감
				gs_mobi_flag = "1";  //모바일에서 작업유무
				
				ls_fact_sub = isp_fact_sub.getSelectedItem().toString();
				if (ls_fact_sub.indexOf(">. ") <= 0) {
					Util.f_dialog(Insp.this,"세부공장을 선택하세요!");
					return;
				}
				ls_fact_sub_code =  ls_fact_sub.substring(0, 3);
				ls_fact_sub_name =  ls_fact_sub.substring(6);
				
				ls_insp = isp_insp_reg_no.getSelectedItem().toString();
				if ((ls_insp.equals("미존재")) || (ls_insp.equals("전표번호를 선택하세요!"))) {
					Util.f_dialog(Insp.this,"점검코드의 전표번호를 선택하세요!");
					return;
				}
				
				ls_insp_reg_no = ls_insp.substring(0, 4) + ls_insp.substring(5, 9);
				
				
				ls_emp = isp_emp.getSelectedItem().toString();
				if (ls_emp.indexOf(">. ") <= 0) {
					Util.f_dialog(Insp.this,"작업자를 선택하세요!");
					return;
				}
				ls_emp_code =  ls_emp.substring(0, 5);
				ls_emp_name =  ls_emp.substring(8);
		
				if (!((ls_fact_sub_code.equals(gs_fact_sub_code)) && (ls_insp_reg_no.equals(gs_insp_reg_no)) && (ls_emp_code.equals(gs_emp_code)))) {
			 		DBH.DB.execSQL("UPDATE LA_SETP SET FACT_SUB_CODE = '" + ls_fact_sub_code + "', FACT_SUB_NAME = '" + ls_fact_sub_name + "', INSP_REG_NO = '" + ls_insp_reg_no + "', EMP_CODE = '" + ls_emp_code + "', EMP_NAME = '" + ls_emp_name + "'");
			 		f_setp();  //변경된 환경설정값 다시 읽어들임
				}
	
	//			if (icb_mobi_flag.isChecked() == true) {
	//				String ls_reg_date = isp_reg_date.getSelectedItem().toString();
	//				if ((ls_reg_date.equals("미존재")) || (ls_reg_date.equals("선택하세요!"))) {
	//					Util.f_dialog(Insp.this,"과거점검내역의 작업일자를 선택하세요!");
	//					return;
	//				} else {
	//					ls_reg_date = ls_reg_date.replace("-", "");
	//				}
	//				
	//				gs_reg_date = ls_reg_date;
	//				gs_mobi_flag = "0";  //모바일에서 작업유무
	//			} else {
					gs_reg_date = iet_reg_date.getText().toString().replace("-", "");
					
	//			}
				if (f_save_insp_master() != 0) return;  //점검내역 마스터 체크
				
				Intent linsp_search = new Intent(getBaseContext(), Insp_Search.class);
				startActivity(linsp_search);
			} else {  //과거점검내역 미체크 - 조회모드로 들어감
				//실행될일 없음 : listView.setOnDataSelectionListener에서 실행됨
				
				
				 
				//0:전표번호 1:세부공장코드 2:세부공장명 3:점검코드 4:점검코드텍스트 5:작업일자 6:작업일자텍스트 7:작업자코드 8:작업자명 9:점검건수 10:전송여부
				DataPut_Item selectItem = (DataPut_Item)adapter.getItem(ai_select);
				
				gs_fact_sub_code = selectItem.getData(1);
				gs_insp_reg_no = selectItem.getData(3);
				gs_emp_code = selectItem.getData(7);
				gs_reg_date = selectItem.getData(5);
				
				//PUT_FLAG 구분(selectItem.getData(10))
	         //완료 : 수정전용      : 전송완료된 데이타
				//진행 : 입력/수정모드 : PUT_FLAG가 전송대기이고 모바일(MOBI_FLAG)에서 수정된 데이타
				//실패 : 수정전용      : PUT_FLAG가 전송대기이나 모바일(MOBI_FLAG)에서 수정되지 않은 데이타
				if (selectItem.getData(10).equals("진행")) {
					gs_mobi_flag = "1";  //모바일에서 작업유무
				} else {
					gs_mobi_flag = "0";  //모바일에서 작업유무
				}
				
				gs_reg_no = selectItem.getData(0);
				
				//Toast.makeText(getBaseContext(), "result:" + gs_fact_sub_code + "/" + gs_insp_reg_no + "/" + gs_emp_code + "/" + gs_reg_date + "/" + gs_reg_no, Toast.LENGTH_SHORT).show();
				Intent linsp_search = new Intent(getBaseContext(), Insp_Search.class);
				startActivity(linsp_search);
			}
		} catch (Exception e) {
			Log.e(TAG, "f_open_insp_search");
			
			
		} finally {
 			if ( pd != null ) pd.hide();
			
		}
			
		
	}
	
	
	
	//점검내역 마스터 체크
	//마스터가 존재하면 gs_mobi_flag에 "0"을 설정하고 gs_reg_no를 읽어오고
	//         존재하지 않으면 gs_mobi_flag에 "1"을 설정하고 Insert후에 생성된 gs_reg_no를 읽어온다.
	//점검코드, 제조사, 공장, 세부공장, 작업자, 작업일자
	public int f_save_insp_master() {
		
		try {
			//MAX를 사용한 이유는 규칙에 벗어나서 점검코드, 제조사, 공장, 세부공장, 작업일자, 작업자가 2개이상 등록되어 있는 경우가 발생할수도 있음
			//2개인상 존재하는 경우에는 나중에 등록된 걸로 가져옴
			
			String ls_put_flag = "0";  //0:대기 1:전송
			curSelect = DBH.DB.rawQuery(
			      "   SELECT MAX(REG_NO) REG_NO" +
	            "     FROM LB_INSP_MASTER" +
	            "    WHERE MOBI_FLAG LIKE '" + gs_mobi_flag + "'" +  //모바일에서 작업중인 데이타 유무
	            "      AND PUT_FLAG LIKE '" + ls_put_flag + "'" +    //모바일에서 작업중인 데이타 전송 유무
	            "      AND LK_INSP_NO = '" + gs_insp_reg_no + "'" +
	            "      AND MAKE_CODE = '" + gs_make_code + "'" +
	            "      AND FACT_CODE = '" + gs_fact_code + "'" +
	            "      AND FACT_SUB_CODE = '" + gs_fact_sub_code + "'" +
	            "      AND EMP_CODE = '" + gs_emp_code + "'" +
	            "      AND REG_DATE = '" + gs_reg_date + "'" +
	            " ORDER BY REG_DATE DESC, REG_NO DESC", null);
			curSelect.moveToNext();
			String ls_reg_no = curSelect.getString(curSelect.getColumnIndex("REG_NO"));
			curSelect.close();
			
			if (Util.f_isnull(ls_reg_no)) {  //최종 수정하던 점검내역이 존재하지 않으면..
				//insert문
				String ls_max_reg_no = "", ls_max_reg_code = "0001";
				
				ls_max_reg_no = f_make_reg_no(gs_reg_date);
				if (Util.f_isnull(ls_max_reg_no)) return -1;
				
				String ls_insert =	
		 		      "INSERT INTO LB_INSP_MASTER" +
		            "           (REG_NO, LK_INSP_NO, REG_DATE, REG_CODE, MAKE_CODE, FACT_CODE, FACT_SUB_CODE," +
                  "            FACT_DETL_SEQ, EMP_CODE, QTY, BIGO, MOBI_FLAG, PUT_FLAG)" +
		            "    VALUES ('" + ls_max_reg_no + "', '" + 
                                    gs_insp_reg_no + "', '" + 
                                    gs_reg_date + "', '" + 
                                    ls_max_reg_code + "', '" + 
                                    gs_make_code + "', '" + 
                                    gs_fact_code + "', '" + 
                                    gs_fact_sub_code + "', " + 
                                    "null" + ", '" + 
                                    gs_emp_code + "', " + 
                                    "0" + ", " + 
                                    "null" + ", '1', '0')";
				
				DBH.DB.execSQL(ls_insert);
				gs_reg_no = ls_max_reg_no;
			} else {  //최종 수정하던 점검내역이 존재시
				gs_reg_no = ls_reg_no;
			}
			
			return 0;
		} catch (Exception e) {
 			Log.e(TAG, "제조사공장 SELECT");
 			Util.f_dialog(Insp.this,"점검내역(마스터)를 생성중에 오류가 발생하였습니다.");
			return 100;
		}
            
	
	
	}
	
	public String f_make_reg_no(String as_reg_date) {
		try {
			//MAX를 사용한 이유는 규칙에 벗어나서 점검코드, 제조사, 공장, 세부공장, 작업일자, 작업자가 2개이상 등록되어 있는 경우가 발생할수도 있음
			//2개인상 존재하는 경우에는 나중에 등록된 걸로 가져옴
			curSelect = DBH.DB.rawQuery("SELECT SUBSTR(MAX(REG_NO), 9) REG_CODE FROM LB_INSP_MASTER WHERE REG_NO LIKE '" + gs_reg_date + "%'", null);
			curSelect.moveToNext();
			String ls_reg_code = curSelect.getString(curSelect.getColumnIndex("REG_CODE"));
			curSelect.close();

			if (Util.f_isnull(ls_reg_code)) {
				ls_reg_code = "0001";
			} else {
				ls_reg_code = String.format("%04d", Integer.parseInt(ls_reg_code) + 1);
			}
			
			return gs_reg_date + ls_reg_code;
		} catch (Exception e) {
 			Log.e(TAG, "점검내역(마스터) SELECT");
 			Util.f_dialog(Insp.this,"점검내역(마스터)를 조회중에 오류가 발생하였습니다.");
			return "";
		}
 		
	}
	
	public void onClick(View v) {
		if(v.equals(findViewById(R.id.cb_mobi_flag))) {
			TextView ltv_et_reg_date = (TextView) findViewById(R.id.tv_et_reg_date);
			TextView ltv_insp_reg_no = (TextView) findViewById(R.id.tv_insp_reg_no);
			TextView ltv_emp         = (TextView) findViewById(R.id.tv_emp);
			Button   lbok            = (Button) findViewById(R.id.bOk);
			
			final LinearLayout lll_list_dataput = (LinearLayout) findViewById(R.id.ll_list_dataput);
			if (icb_mobi_flag.isChecked() == true) {
				ltv_et_reg_date.setVisibility(View.GONE);       iet_reg_date.setVisibility(View.GONE);  //공간차지하지 않고 없애기
				ltv_insp_reg_no.setVisibility(View.GONE);       isp_insp_reg_no.setVisibility(View.GONE);
				ltv_emp.setVisibility(View.GONE);               isp_emp.setVisibility(View.GONE);
				lbok.setVisibility(View.GONE);
				
				String ls_update = null, ls_delete = null;
				try {
					ls_update =
							"UPDATE LB_INSP_MASTER " +
							"   SET QTY = (SELECT COUNT(1) CNT FROM LB_INSP_DETAIL D WHERE LB_INSP_MASTER.REG_NO = D.REG_NO)" +
							" WHERE MOBI_FLAG = '1'";
			 		DBH.DB.execSQL(ls_update);
			 		
				} catch (Exception e) {
		 			Log.e(TAG, "점검내역(마스터) 수정 : " + ls_update);
		 			Util.f_dialog(Insp.this,"점검내역(마스터)을 수정중에 오류가 발생하였습니다.");
				}

				try {
					ls_delete =
							"DELETE FROM LB_INSP_MASTER WHERE MOBI_FLAG = '1' AND IFNULL(QTY, 0) = 0";
			 		DBH.DB.execSQL(ls_delete);
			 		
				} catch (Exception e) {
		 			Log.e(TAG, "점검내역(마스터) 삭제 : " + ls_delete);
		 			Util.f_dialog(Insp.this,"점검내역(마스터)을 삭제중에 오류가 발생하였습니다.");
				}

				pd_bef = ProgressDialog.show(Insp.this, "","전송 데이타 작업목록을 수신중입니다...", true);		        	
				Handler v1 = new Handler();
				v1.postDelayed(
			    		new Runnable () {
							public void run() {
								fGetList();
								lll_list_dataput.setVisibility(View.VISIBLE);
							}
			    		}
			    		, 1000);
		
			} else {
				lll_list_dataput.setVisibility(View.GONE);
				
				ltv_et_reg_date.setVisibility(View.VISIBLE);    iet_reg_date.setVisibility(View.VISIBLE);
				ltv_insp_reg_no.setVisibility(View.VISIBLE);    isp_insp_reg_no.setVisibility(View.VISIBLE);
				ltv_emp.setVisibility(View.VISIBLE);            isp_emp.setVisibility(View.VISIBLE);
				lbok.setVisibility(View.VISIBLE);
				
			}
			
		}
	}

	public void fGetList() {
		try
		{
			String ls_reg_no, ls_fact_sub_code, ls_fact_sub_name, ls_insp_reg_no, ls_insp_reg_no_text, ls_reg_date, ls_reg_date_text, ls_emp_code, ls_emp_name, ls_qty, ls_put_flag;
			
			String ls_fact_sub, ls_fact_sub_code_search;
			
			ls_fact_sub = isp_fact_sub.getSelectedItem().toString();
			if (ls_fact_sub.indexOf(">. ") <= 0) {  
				ls_fact_sub_code_search = "%";
				
			} else {
				ls_fact_sub_code_search =  ls_fact_sub.substring(0, 3);
			}
			
			
			adapter.clear();
			Cursor curSelect;
			//PUT_FLAG 구분
         //완료 : 수정전용      : 전송완료된 데이타
			//진행 : 입력/수정모드 : PUT_FLAG가 전송대기이고 모바일(MOBI_FLAG)에서 수정된 데이타
			//실패 : 수정전용      : PUT_FLAG가 전송대기이나 모바일(MOBI_FLAG)에서 수정되지 않은 데이타
			curSelect = DBH.DB.rawQuery(
			      "   SELECT M.REG_NO, M.FACT_SUB_CODE, FS.FACT_SUB_NAME, M.LK_INSP_NO, M.REG_DATE, M.QTY, M.EMP_CODE, EMP.EMP_NAME," +
			      "          CASE IFNULL(M.PUT_FLAG, '1') WHEN '0' THEN (CASE IFNULL(M.MOBI_FLAG, '0') WHEN '1' THEN '진행' WHEN '0' THEN '실패' END)" +
			      "                                       WHEN '1' THEN '완료' END PUT_FLAG" +
	            "     FROM ((" +
	            "          LB_INSP_MASTER M LEFT OUTER JOIN LA_FACT_SUB FS ON M.FACT_SUB_CODE = FS.FACT_SUB_CODE)" +
	            "                           LEFT OUTER JOIN BE_DEPT_EMP EMP ON M.EMP_CODE = EMP.EMP_CODE)" +
	            "    WHERE M.MAKE_CODE = '" + gs_make_code + "'" +
	            "      AND M.FACT_CODE = '" + gs_fact_code + "'" +
	            "      AND M.FACT_SUB_CODE LIKE '" + ls_fact_sub_code_search + "'" +
	            "      AND IFNULL(M.PUT_FLAG, '1') LIKE '%'" +
	            " ORDER BY M.REG_DATE DESC, M.REG_NO DESC", null);
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
				Util.f_dialog(Insp.this,"과거 수신내역이 존재하지 않습니다.");
			} else {
				Toast.makeText(getBaseContext(), "총 " + String.format("%,d", li_count) + "건 검색되었습니다.", Toast.LENGTH_SHORT).show();
			}
			
			listView.setAdapter(adapter);
			
//			adapter.notifyDataSetChanged();  //변경사항을 refalsh
			
			//<<<---리스트뷰<<<---

		} finally {
 			if ( pd_bef != null ) pd_bef.hide();
		}
		
	}	
	
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {

		String ls_reg_no, ls_emp_code, ls_emp_name;
		String[] lsv_emp;
		int li_count;

		String ls_insp = isp_insp_reg_no.getSelectedItem().toString();
		
		
		switch(parent.getId()){
			case R.id.sp_insp_reg_no:
				if ((ls_insp.equals("미존재")) || (ls_insp.equals("전표번호를 선택하세요!"))) {
					ls_reg_no = "";
				} else {
					ls_reg_no = ls_insp.substring(0, 4) + ls_insp.substring(5, 9);
				}

				try {
					curSelect = DBH.DB.rawQuery(
							"   SELECT IE.EMP_CODE, EMP.EMP_NAME" +
							"     FROM LB_INSP_EMP IE, BE_DEPT_EMP EMP" +
							"    WHERE IE.EMP_CODE = EMP.EMP_CODE" +
							"      AND IE.REG_NO = '" + ls_reg_no + "'" +
							" ORDER BY IE.EMP_CODE", null);  //최근 기초데이타 유무
		
					li_count = curSelect.getCount();
					lsv_emp = new String[li_count];
					
					if (li_count <= 0) {
						lsv_emp = new String[1];
						lsv_emp[0] = "미존재";
					} else {
						lsv_emp = new String[li_count + 1];
						lsv_emp[0] = "선택하세요!";
						
						for (int i=1;i<li_count + 1;i++){
							curSelect.moveToNext();
							ls_emp_code = curSelect.getString(curSelect.getColumnIndex("EMP_CODE"));
							ls_emp_name = curSelect.getString(curSelect.getColumnIndex("EMP_NAME"));
							lsv_emp[i] = ls_emp_code + ">. " + ls_emp_name;
						}
					}
					curSelect.close();
					
					//--->>>스피너--->>>
					//sp_make.setOnItemSelectedListener(this);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lsv_emp);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					isp_emp.setAdapter(adapter);
					//<<<---스피너<<<---
			
				} catch (Exception e) {
		 			Log.e(TAG, "제조사공장 SELECT");
		 			Util.f_dialog(Insp.this,"제조사공장 조회중에 오류가 발생하였습니다.");
				}
			case R.id.sp_emp:
//				ls_emp = isp_emp.getSelectedItem().toString();
//				if (ls_emp.indexOf(">. ") <= 0) {
//					icb_mobi_flag.setEnabled(false);
//				} else {
//					icb_mobi_flag.setEnabled(true);
//				}
		}
		
		icb_mobi_flag.setChecked(false);
		onClick(icb_mobi_flag);
	}	
	
	
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
      
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
			
			switch(flag_view.getId()){
			case R.id.et_reg_date:
				gcal1.set(year, monthOfYear, dayOfMonth);
				iet_reg_date.setText(String.format(DATEFORMAT,gcal1.get(Calendar.YEAR),(gcal1.get(Calendar.MONTH) + 1),gcal1.get(Calendar.DAY_OF_MONTH)));
				break;
			
			}
			
			
		}


};		
	
}