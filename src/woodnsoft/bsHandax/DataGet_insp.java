package woodnsoft.bsHandax;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import woodnsoft.bsHandax.DataListView.DataGet_Insp.DataGet_Insp_Item;
import woodnsoft.bsHandax.DataListView.DataGet_Insp.DataGet_Insp_ListAdapter;
import woodnsoft.bsHandax.DataListView.DataGet_Insp.DataGet_Insp_ListView;
import woodnsoft.bsHandax.common.DBActivity;
import woodnsoft.bsHandax.common.Util;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class DataGet_insp extends DBActivity {
	String is_make_code, is_make_name, is_fact_code, is_fact_name;
	private static final String TAG = "DataGet_insp"; 
	DataGet_Insp_ListView listView;
	DataGet_Insp_ListAdapter adapter;
	
	//���α׷��� ������ ����
	ProgressHandler handler;

	//Ÿ��Ʋ ��Ʈ��
	ProgressBar ipbtitle;
	TextView    itvtitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //Ŀ���� Ÿ��Ʋ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dataget_detail);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		handler = new ProgressHandler();  //���α׷��� ������
		ipbtitle = (ProgressBar) findViewById(R.id.pbtitle);
		itvtitle = (TextView) findViewById(R.id.tvtitle);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
         
		is_make_code = bundle.getString("make_code");
		is_make_name = bundle.getString("make_name");
		is_fact_code = bundle.getString("fact_code");
		is_fact_name = bundle.getString("fact_name");

		
		//--->>>����Ʈ��--->>>
		adapter = new DataGet_Insp_ListAdapter(this);
		listView = new DataGet_Insp_ListView(this);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayoutDrugList );
		linLayout.addView(listView);
		
		
		pd_bef = ProgressDialog.show(DataGet_insp.this, "","���� BS���˳��� �۾���� �������Դϴ�...", true);		        	
		Handler v = new Handler();
		v.postDelayed(
	    		new Runnable () {
					public void run() {
						fGetList();
					}
	    		}
	    		, 1000);

		Button lbback = (Button) findViewById(R.id.bBack);  //�ڷ�
		lbback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		Button lbok = (Button) findViewById(R.id.bOk);  //����
		lbok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ipbtitle.setVisibility(View.VISIBLE);
				ipbtitle.setProgress(0);
				
				pd = ProgressDialog.show(DataGet_insp.this, "", "\"" + is_make_name + " " + is_fact_name + "\"�� \n���� BS���˳��� ����Ÿ�� �������Դϴ�...", true);		        	
			    v.postDelayed(new Runnable () {public void run() {
								//-------->>>>���α׷��� ������ ����-------->>>>>>>
								Thread thread2 = new Thread(new Runnable() {
									public void run() {
										// TODO Auto-generated method stub
										fGetData();
									}
								});
								thread2.start();
								//<<<<<<<--------���α׷��� ������ ����<<<<<--------
			    }}, 300);

			}
		});
		


	}

	public void fAddAdapter(String as_table_name, String as_table_code) {
		String ls_select = "", ls_modi_date = "0��", ls_flag;
		
		try {
			
			if (as_table_code.equals("LB_INSP_EMP")) {
				ls_select = "SELECT COUNT(1) CNT FROM LB_INSP I, LB_INSP_EMP IE" +
		            "    WHERE I.REG_NO = IE.REG_NO" +
		            "      AND I.MAKE_CODE LIKE '" + is_make_code + "' AND I.FACT_CODE LIKE '" + is_fact_code + "'";
			} else if (as_table_code.equals("LB_INSP_CASE_CHEK")) {
				ls_select = "SELECT COUNT(1) CNT FROM LB_INSP_DETAIL D, LB_INSP_CASE_CHEK C" +
		            "    WHERE D.REG_NO = C.REG_NO" +
		            "      AND D.REG_SEQ = C.REG_SEQ" +
		            "      AND D.MAKE_CODE LIKE '" + is_make_code + "' AND D.FACT_CODE LIKE '" + is_fact_code + "'";
			} else {
				ls_select = "SELECT COUNT(1) CNT FROM " + as_table_code + " WHERE MAKE_CODE LIKE '" + is_make_code + "' AND FACT_CODE LIKE '" + is_fact_code + "'";
			}
			
			
			f_web_select(ls_select);
			//Log.i(TAG,"[��� : " + gsDbRtn + " ----- ��ü�Ǽ� : " + gjArr.length() + "-------------]" + ls_select);
			JSONObject jObj = gjArr.getJSONObject(0);  //����
			ls_modi_date = "�� " + jObj.getString("CNT") + "��";
 		} catch (Exception e) {
 			ls_modi_date = "����";
 			Log.e(TAG, "fAddAdapter :" + ls_select);
 		}		
		ls_flag = "���";
		adapter.addItem(new DataGet_Insp_Item(as_table_name, as_table_code, ls_modi_date, ls_flag));

	}

	public void fGetList() {
		try
		{
			fAddAdapter("���˳���",            "LB_INSP");
			fAddAdapter("���˳����۾���",      "LB_INSP_EMP");
			
			fAddAdapter("���˳���������",      "LB_INSP_MASTER");
			fAddAdapter("���˳���������",      "LB_INSP_DETAIL");
			fAddAdapter("���˳����� �����׸�", "LB_INSP_CASE_CHEK");
			
			listView.setAdapter(adapter);
			//<<<---����Ʈ��<<<---

		} finally {
 			if ( pd_bef != null ) pd_bef.hide();
			Toast.makeText(getApplicationContext(), "������ : " + is_make_name + ", ���� : " + is_fact_name + "\n�� �����ϼ̽��ϴ�!", Toast.LENGTH_LONG).show();

		}
		
	}
	
	public void fGetData() {
		
		try
		{
			int li_count = adapter.getCount();
			ipbtitle.setMax(li_count);
			
			String ls_table_code = null, ls_table_name = null, ls_next_table_name = null, ls_flag;
			int li_chk = 1, li_fail = 0, li_next;

			for (int i = 1; i <= li_count; i++){
				DataGet_Insp_Item ldgi = (DataGet_Insp_Item)adapter.getItem(i - 1);
				ls_table_name = ldgi.getData(0);
				ls_table_code = ldgi.getData(1);
				ls_flag = ldgi.getData(3);
				
				if (i >= li_count) {	li_next = i - 1; } else { li_next = i; }
				DataGet_Insp_Item ldgi2 = (DataGet_Insp_Item)adapter.getItem(li_next);
				ls_next_table_name = ldgi2.getData(0);
				
	    		//Log.i(TAG,"for:" + i + ", " + ls_flag + ", table_code : " + ls_table_code);

	    		if (ls_flag.equals("���") || ls_flag.equals("����")) {
	    			//Log.i(TAG, ls_table_code + "-" + ls_table_name);
	    			if (fChk_Table(ls_table_code, ls_table_name) == false) {li_chk = 9; li_fail++;} else {li_chk = 2;}  //9.����, 0.��� 1.���� 2.�Ϸ� 3.��ġ
	    			
	    		} else {
	    			//Log.i(TAG, ls_table_code + "��ġ : " + ls_flag);
	    			li_chk = 100;
	    		}
	    		
	    		Message msg = handler.obtainMessage();  //-------------------------------------------------------------------------------------------------------
				msg.obj = ls_next_table_name;
				msg.arg1 = li_chk;   //������ �� ��������(9.����, 0.��� 1.���� 2.�Ϸ� 3.��ġ)
				msg.arg2 = li_fail;  //�������аǼ�
				handler.sendMessage(msg);
				
			}

			
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private boolean fChk_Table(String ls_table_code, String ls_table_name) {  //ǰ��
		String ls_select = null, ls_insert = null;
		try
		{
			if (DBH.fCreate(ls_table_code) == false) {
				Log.i(TAG, ls_table_name + "(" + ls_table_code + ")�� �����߿� ������ �߻��Ͽ����ϴ�.");
				Util.f_dialog(DataGet_insp.this, ls_table_name + "(" + ls_table_code + ")�� �����߿� ������ �߻��Ͽ����ϴ�.");
				return false;
			}
			
			try {
				DBH.DB.execSQL("DELETE FROM " + ls_table_code);
			} catch (SQLiteException es) {
				Log.e(TAG, "DELETE FROM " + ls_table_code);
				Util.f_dialog(DataGet_insp.this, ls_table_name + "(" + ls_table_code + ") �ʱ�ȭ �����߿� ������ �߻��Ͽ����ϴ�.");
				return false;
			}

			//----->>>>>�ǵ���Ÿ�� �����´�.
			ls_select = fGetSelect(ls_table_code);
			if (Util.f_isnull(ls_select)) return false;
			
		   f_web_select(ls_select);
			
	 		//Log.i(TAG, ls_table_code + "[��� : " + gsDbRtn + " ----- ��ü�Ǽ� : " + gjArr.length() + "-------------]");
	
	 		DBH.DB.beginTransaction();
	 		try {
		    	for (int i = 0; i < gjArr.length(); i++) {
		    		JSONObject jObj = gjArr.getJSONObject(i);  //����
		    		
		    		ls_insert = fGetInsert(ls_table_code, jObj);
		    		if (Util.f_isnull(ls_insert)) return false;
		    		
	    			DBH.DB.execSQL(ls_insert);
		    		
		    	}
	 			DBH.DB.execSQL("UPDATE AZ_MODI_DATE SET MODI_DATE_M = MODI_DATE, USE_FLAG = '2' WHERE TABLE_CODE = '" + ls_table_code + "'");
		    	DBH.DB.setTransactionSuccessful();  //commit;

	 		} catch (SQLiteException es) {
	 			Log.e(TAG,"INSERT INTO " + ls_table_code + " : " + ls_insert);
				//Util.f_dialog(DataGet_Detail.this, ls_table_name + "(" + ls_table_code + ") ����Ÿ INSERT �߿� ������ �߻��Ͽ����ϴ�.");
	 			return false;
	 		} finally {
	 			DBH.DB.endTransaction();
	 		}
	    	
	 		//Log.i(TAG, "gsDbRtn : " + gsDbRtn + "," + gjArr.length());
			return true;
	
		} catch (Exception e) {
			e.printStackTrace();
 			Log.e(TAG, "f_web_select : " + ls_select);
			//Util.f_dialog(DataGet_Detail.this, ls_table_name + "(" + ls_table_code + ") ����Ÿ �����߿� ������ �߻��Ͽ����ϴ�.");
			return false;
		}
		
	}
	
	private String fGetSelect(String as_table_code) {
		String ls_select = null;
		
		if (as_table_code == "LB_INSP") {                                       //���˳���
			ls_select = "  SELECT REG_NO, REG_YYYY, MAKE_FACT_SEQ, MAKE_CODE, FACT_CODE," +
	                  "         STRT_DATE, CLOS_DATE, EMP_TEXT, NVL(QTY, 0) QTY, ING_FLAG, BIGO" +
	                  "    FROM LB_INSP" +
                     "   WHERE MAKE_CODE LIKE '" + is_make_code + "' AND FACT_CODE LIKE '" + is_fact_code + "'";
		} else if (as_table_code == "LB_INSP_EMP") {                            //���˳����۾���
			ls_select = "   SELECT IE.REG_NO, IE.EMP_CODE" +
			            "     FROM LB_INSP I, LB_INSP_EMP IE" +
	                  "    WHERE I.REG_NO = IE.REG_NO" +
	                  "      AND I.MAKE_CODE LIKE '" + is_make_code + "' AND I.FACT_CODE LIKE '" + is_fact_code + "'";
		} else if (as_table_code == "LB_INSP_MASTER") {                         //���˳���������
			ls_select = "  SELECT REG_NO, LK_INSP_NO, REG_DATE, REG_CODE, MAKE_CODE, FACT_CODE, FACT_SUB_CODE," +
                     "         FACT_DETL_SEQ, EMP_CODE, NVL(QTY, 0) QTY, BIGO" +
                     "    FROM LB_INSP_MASTER" +
                     "   WHERE MAKE_CODE LIKE '" + is_make_code + "' AND FACT_CODE LIKE '" + is_fact_code + "'" +
                     "     AND REG_DATE > (SELECT CASE WHEN MAX(CNT) < 7500 THEN '00000000' ELSE MIN(REG_DATE) END REG_DATE" +
                     "                       FROM (SELECT COUNT(1) OVER () CNT, RANK() OVER (ORDER BY REG_DATE DESC) RN, REG_DATE FROM LB_INSP_DETAIL WHERE MAKE_CODE LIKE '" + is_make_code + "' AND FACT_CODE LIKE '" + is_fact_code + "')" +
                     "                      WHERE RN <= 6000)";
		} else if (as_table_code == "LB_INSP_DETAIL") {                         //���˳���������
			ls_select = "  SELECT REG_NO, REG_SEQ, REG_SKEY, REG_DATE, REG_CODE, MAKE_CODE, FACT_CODE, FACT_SUB_CODE, LINE_CODE, FACT_LINE_SEQ," +
                     "         EMP_CODE, OP_NO, MACN_CODE, MACN_NAME, MACN_MODEL, MACN_MAKE_CODE, MACN_MAKE_NAME, MAKER_CODE, BAR_CODE," +
                     "         ITEM_CODE, ITEM_SPEC, JATA_FLAG, BAR_LA_CODE, MATCH_FLAG, MAKE_DATE, STATE_FLAG, CHEK_FLAG, CASE_TEXT, CHEK_TEXT, NVL(QTY, 0) QTY, BIGO" +
                     "    FROM LB_INSP_DETAIL" +
                     "   WHERE MAKE_CODE LIKE '" + is_make_code + "' AND FACT_CODE LIKE '" + is_fact_code + "'" +
                     "     AND REG_DATE > (SELECT CASE WHEN MAX(CNT) < 7500 THEN '00000000' ELSE MIN(REG_DATE) END REG_DATE" +
                     "                       FROM (SELECT COUNT(1) OVER () CNT, RANK() OVER (ORDER BY REG_DATE DESC) RN, REG_DATE FROM LB_INSP_DETAIL WHERE MAKE_CODE LIKE '" + is_make_code + "' AND FACT_CODE LIKE '" + is_fact_code + "')" +
                     "                      WHERE RN <= 6000)";
		} else if (as_table_code == "LB_INSP_CASE_CHEK") {                      //���˳����������׸�
			ls_select = "   SELECT C.REG_NO, C.REG_SEQ, C.REG_SSEQ, C.REG_SSKEY, C.REG_DATE, C.REG_CODE, C.CASE_CODE, C.CHEK_CODE, C.BIGO" +
			            "     FROM LB_INSP_DETAIL D, LB_INSP_CASE_CHEK C" +
	                  "    WHERE D.REG_NO = C.REG_NO" +
	                  "      AND D.REG_SEQ = C.REG_SEQ" +
	                  "      AND D.MAKE_CODE LIKE '" + is_make_code + "' AND D.FACT_CODE LIKE '" + is_fact_code + "'" +
                      "      AND D.REG_DATE > (SELECT CASE WHEN MAX(CNT) < 7500 THEN '00000000' ELSE MIN(REG_DATE) END REG_DATE" +
                      "                       FROM (SELECT COUNT(1) OVER () CNT, RANK() OVER (ORDER BY REG_DATE DESC) RN, REG_DATE FROM LB_INSP_DETAIL WHERE MAKE_CODE LIKE '" + is_make_code + "' AND FACT_CODE LIKE '" + is_fact_code + "')" +
                      "                      WHERE RN <= 6000)";
		}
		
		return ls_select;
	}

	private String fGetInsert(String as_table_code, JSONObject jObj) {
		String ls_insert = null;
		
 		try {
			if (as_table_code == "LB_INSP") {                                  //���˳���
		 		ls_insert =	"INSERT INTO LB_INSP" +
				            "           (REG_NO, REG_YYYY, MAKE_FACT_SEQ, MAKE_CODE, FACT_CODE," +
	                     "            STRT_DATE, CLOS_DATE, EMP_TEXT, QTY, ING_FLAG, BIGO)" +
				            "    VALUES ('" + jObj.getString("REG_NO") + "', '" + 
                                          jObj.getString("REG_YYYY") + "', '" + 
                                          jObj.getString("MAKE_FACT_SEQ") + "', '" + 
                                          jObj.getString("MAKE_CODE") + "', '" + 
                                          jObj.getString("FACT_CODE") + "', '" + 
                                          
                                          jObj.getString("STRT_DATE") + "', '" + 
                                          jObj.getString("CLOS_DATE") + "', '" + 
                                          Util.f_replace(jObj.getString("EMP_TEXT"), "'", "''") + "', '" + 
                                          jObj.getDouble("QTY") + "', '" + 
                                          jObj.getString("ING_FLAG") + "', '" + 
                                          Util.f_replace(jObj.getString("BIGO"), "'", "''") + "')";
		 		
			} else if (as_table_code == "LB_INSP_EMP") {                            //���˳����۾���
		 		ls_insert =	"INSERT INTO LB_INSP_EMP" +
				            "           (REG_NO, EMP_CODE)" +
				            "    VALUES ('" + jObj.getString("REG_NO") + "', '" + 
                                          jObj.getString("EMP_CODE") + "')";
			} else if (as_table_code == "LB_INSP_MASTER") {                       //���˳���������
		 		ls_insert =	"INSERT INTO LB_INSP_MASTER" +
				            "           (REG_NO, LK_INSP_NO, REG_DATE, REG_CODE, MAKE_CODE, FACT_CODE, FACT_SUB_CODE," +
                        "            FACT_DETL_SEQ, EMP_CODE, QTY, BIGO, MOBI_FLAG)" +
				            "    VALUES ('" + jObj.getString("REG_NO") + "', '" + 
                                          jObj.getString("LK_INSP_NO") + "', '" + 
                                          jObj.getString("REG_DATE") + "', '" + 
                                          jObj.getString("REG_CODE") + "', '" + 
                                          jObj.getString("MAKE_CODE") + "', '" + 
                                          jObj.getString("FACT_CODE") + "', '" + 
                                          jObj.getString("FACT_SUB_CODE") + "', '" + 
                                          
                                          jObj.getString("FACT_DETL_SEQ") + "', '" + 
                                          jObj.getString("EMP_CODE") + "', '" + 
                                          jObj.getDouble("QTY") + "', '" + 
                                          Util.f_replace(jObj.getString("BIGO"), "'", "''") + "', '0')";
			} else if (as_table_code == "LB_INSP_DETAIL") {                        //���˳���������
		 		ls_insert =	"INSERT INTO LB_INSP_DETAIL" +
				            "           (REG_NO, REG_SEQ, REG_SKEY, REG_DATE, REG_CODE, MAKE_CODE, FACT_CODE, FACT_SUB_CODE, LINE_CODE, FACT_LINE_SEQ," +
                     "               EMP_CODE, OP_NO, MACN_CODE, MACN_NAME, MACN_MODEL, MACN_MAKE_CODE, MACN_MAKE_NAME, MAKER_CODE, BAR_CODE," +
                     "               ITEM_CODE, ITEM_SPEC, JATA_FLAG, BAR_LA_CODE, MATCH_FLAG, MAKE_DATE, STATE_FLAG, CHEK_FLAG, CASE_TEXT, CHEK_TEXT, QTY, BIGO)" +
				            "    VALUES ('" + jObj.getString("REG_NO") + "', '" + 
                                          jObj.getString("REG_SEQ") + "', '" + 
                                          jObj.getString("REG_SKEY") + "', '" + 
                                          jObj.getString("REG_DATE") + "', '" + 
                                          jObj.getString("REG_CODE") + "', '" + 
                                          jObj.getString("MAKE_CODE") + "', '" + 
                                          jObj.getString("FACT_CODE") + "', '" + 
                                          jObj.getString("FACT_SUB_CODE") + "', '" + 
                                          jObj.getString("LINE_CODE") + "', '" + 
                                          jObj.getString("FACT_LINE_SEQ") + "', '" +
                                          
                                          jObj.getString("EMP_CODE") + "', '" + 
                                          Util.f_replace(jObj.getString("OP_NO"), "'", "''") + "', '" + 
                                          jObj.getString("MACN_CODE") + "', '" + 
                                          Util.f_replace(jObj.getString("MACN_NAME"), "'", "''") + "', '" + 
                                          Util.f_replace(jObj.getString("MACN_MODEL"), "'", "''") + "', '" + 
                                          jObj.getString("MACN_MAKE_CODE") + "', '" + 
                                          Util.f_replace(jObj.getString("MACN_MAKE_NAME"), "'", "''") + "', '" + 
                                          jObj.getString("MAKER_CODE") + "', '" + 
                                          jObj.getString("BAR_CODE") + "', '" + 
                                          
                                          jObj.getString("ITEM_CODE") + "', '" + 
                                          Util.f_replace(jObj.getString("ITEM_SPEC"), "'", "''") + "', '" + 
                                          jObj.getString("JATA_FLAG") + "', '" + 
                                          jObj.getString("BAR_LA_CODE") + "', '" + 
                                          jObj.getString("MATCH_FLAG") + "', '" + 
                                          jObj.getString("MAKE_DATE") + "', '" + 
                                          jObj.getString("STATE_FLAG") + "', '" + 
                                          jObj.getString("CHEK_FLAG") + "', '" + 
                                          Util.f_replace(jObj.getString("CASE_TEXT"), "'", "''") + "', '" + 
                                          Util.f_replace(jObj.getString("CHEK_TEXT"), "'", "''") + "', '" + 
                                          jObj.getDouble("QTY") + "', '" + 
                                          Util.f_replace(jObj.getString("BIGO"), "'", "''") + "')";
			} else if (as_table_code == "LB_INSP_CASE_CHEK") {                     //���˳����������׸�
		 		ls_insert =	"INSERT INTO LB_INSP_CASE_CHEK" +
				            "           (REG_NO, REG_SEQ, REG_SSEQ, REG_SSKEY, REG_DATE, REG_CODE, CASE_CODE, CHEK_CODE, BIGO)" +
				            "    VALUES ('" + jObj.getString("REG_NO") + "', '" + 
                                          jObj.getString("REG_SEQ") + "', '" + 
                                          jObj.getString("REG_SSEQ") + "', '" + 
                                          jObj.getString("REG_SSKEY") + "', '" + 
                                          jObj.getString("REG_DATE") + "', '" + 
                                          jObj.getString("REG_CODE") + "', '" + 
                                          jObj.getString("CASE_CODE") + "', '" + 
                                          jObj.getString("CHEK_CODE") + "', '" + 
                                          Util.f_replace(jObj.getString("BIGO"), "'", "''") + "')";

			}

	 		ls_insert = ls_insert.replace(", 'null'", ", null");

		} catch (Exception e) {
			e.printStackTrace();
 			Log.e(TAG, "fGetInsert : " + ls_insert);
			//Util.f_dialog(DataGet_Detail.this, as_table_code + " ����Ÿ INSERT �߿� ������ �߻��Ͽ����ϴ�.");
			return "";
 		}
		
		
		return ls_insert;
	}

	public class ProgressHandler extends Handler {
		public void handleMessage(Message msg) {
			DataGet_Insp_Item selectItem = (DataGet_Insp_Item)adapter.getItem(ipbtitle.getProgress());
			
			String mData[] = selectItem.getData();
			
   		//9.����, 0.��� 1.���� 2.�Ϸ� 3.��ġ
   		if (msg.arg1 == 0) {
   			mData[3] = "���"; //����� ���븸 ������ ����
   		} else if (msg.arg1== 1) {
   			mData[3] = "����";
   		} else if (msg.arg1== 2) {
   			mData[3] = "�Ϸ�";
   		} else if (msg.arg1== 3) {
   			mData[3] = "��ġ";
   		} else if (msg.arg1== 9) {
   			mData[3] = "����";
   		} else if (msg.arg1== 100) {
   			//�ƹ��͵� ����
   		} else {
   			mData[3] = "����(" + msg.arg1 + ")";
   		}
   		selectItem.setData(mData);   //����� ������ SetData
			
   		adapter.notifyDataSetChanged();  //��������� refalsh
			
   		   		
			ipbtitle.incrementProgressBy(1);
			if (ipbtitle.getProgress() == ipbtitle.getMax()) {
				
				String ls_datetime;
				Date ld_date = new Date(); 
				SimpleDateFormat lsd_datetime = new SimpleDateFormat("yyyyMMdd hhmmssss"); 
				ls_datetime = lsd_datetime.format(ld_date).substring(0, 17);
				
				/*Cursor curSelect = DBH.DB.rawQuery("SELECT COUNT(1) CNT FROM AZ_MODI_DATE WHERE TABLE_CODE = 'INSP'", null);  //�ֱ� ����BS���˳��� ������ ����
				curSelect.moveToNext();
	    		if (curSelect.getInt(curSelect.getColumnIndex("CNT")) <= 0) {
			 		ls_insert =	"INSERT INTO AZ_MODI_DATE" +
    				            "           (TABLE_CODE, MODI_DATE, MODI_DATE_M, VER_M, USE_FLAG, BIGO1, BIGO2, BIGO3, BIGO4)" +
    				            "    VALUES ('INSP', '" + 
    				                        "19000101 00000000" + "', '" + 
    				                        ls_datetime + "', '" + 
    				                        DBH.VER + "', '" +
                                       '0' + "', '" +
    				                        is_make_code + "', '" +
    				                        is_make_name + "', '" +
    				                        is_fact_code + "', '" +
    				                        is_fact_name + "')";
			 		DBH.DB.execSQL(ls_insert);
			 		//Log.i(TAG, ls_insert);
	    		} else {
	    			ls_update = "UPDATE AZ_MODI_DATE" +
						         "   SET MODI_DATE_M = '" + ls_datetime + "'," +
							      "       USE_FLAG = '" + '0' + "'," +
							      "       BIGO1 = '" + is_make_code + "'," +
							      "       BIGO2 = '" + is_make_name + "'," +
							      "       BIGO3 = '" + is_fact_code + "'," +
							      "       BIGO4 = '" + is_fact_name + "'" +
 							      " WHERE TABLE_CODE = 'INSP'";
	    			DBH.DB.execSQL(ls_update);
	    			//Log.i(TAG, ls_update);
	    		}
	    		curSelect.close();*/
	    		
	    		DBH.DB.execSQL("UPDATE LA_SETP SET LAST_GET_DATE = '" + ls_datetime + "', MAKE_CODE = '" + is_make_code + "', MAKE_NAME = '" + is_make_name + "', FACT_CODE = '" + is_fact_code + "', FACT_NAME = '" + is_fact_name + "', " +
	    		                                  "FACT_SUB_CODE = '', FACT_SUB_NAME = '', INSP_REG_NO = '', EMP_CODE = '', EMP_NAME = ''");
	    		f_setp();  //����� ȯ�漳���� �ٽ� �о����
				
				
				if ( pd != null ) pd.hide();
				itvtitle.setText(R.string.app_name);
				if (msg.arg2 <= 0) {
					//�Ϸ��� �ڵ����� â ����
					new AlertDialog.Builder(DataGet_insp.this)
					.setTitle(R.string.MSG_TITLE)
					.setIcon(R.drawable.title_icon)
					.setMessage("���� BS���˳��� ����Ÿ �����۾��� �Ϸ��߽��ϴ�.")
					.setPositiveButton("Ȯ��", mClickLeft)
					.show();	
				} else {
					Util.f_dialog(DataGet_insp.this, "���� BS���˳��� ����Ÿ �����۾��� �����߽��ϴ�.\n * �� " + (ipbtitle.getMax()) + "���� " + msg.arg2 + "�� ����!");
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
