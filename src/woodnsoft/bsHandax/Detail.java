package woodnsoft.bsHandax;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import woodnsoft.bsHandax.common.DBActivity;
import woodnsoft.bsHandax.common.Util;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Detail extends DBActivity  {
	TextView txtMsg;
	String is_code, is_name, is_date;
	private static final String TAG = "Detail"; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		txtMsg = (TextView)findViewById(R.id.txtMsg);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
         
		is_name = bundle.getString("data0");
		is_code = bundle.getString("data1");
		is_date = bundle.getString("data2");

		fChk();

        
        //txtMsg.append("\n");
		txtMsg.setOnLongClickListener(new OnLongClickListener() {
			
			public boolean onLongClick(View v) {
				txtMsg.setText("");
				return false;
			}
		});
		

		Button lbback = (Button) findViewById(R.id.bBack);
		lbback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				
				//Toast.makeText(getBaseContext(), DatabaseHelper.dbPath, Toast.LENGTH_SHORT).show();
				
				//Util.f_dialog(Detail.this,"서버에서 데이타를 가져오는중에 오류가 발생하였습니다.");
				
        		//File sdcard = Environment.getExternalStorageDirectory();   
            //    File dbpath = new File(sdcard.getAbsolutePath() + File.separator + "DBTEST");   

        		// String dbfile = dbpath.getAbsolutePath() + File.separator + "mydata_log.sql";
        		// Toast.makeText(getBaseContext(), dbfile, Toast.LENGTH_SHORT).show();
				
			}
		});
		
		Button lbOk = (Button) findViewById(R.id.bOk);
		lbOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pd = ProgressDialog.show(Detail.this, "","데이타를 가져오고 있습니다.", true);
			    v.postDelayed(new Runnable () { public void run() {
			   	 
			   	 
			   	 //dbSelect();
					
			   	
			   	 //f_BE_DEPT_EMP();
			   	 
			   	 /*
			   	 if (is_code.equals("BE_DEPT_EMP")) {
			   		 fHttpTest();
			   	 } else if (is_code.equals("AZ_MODI_DATE")) {
			   		 fAZ_MODI_DATE();
			   	 } else {
			   		 Toast.makeText(getBaseContext(), "[" + is_code + "]" + is_name + "에 대한 검증이 개발되지 않았습니다", Toast.LENGTH_LONG).show();
			   		 if ( pd != null ) pd.hide();
			   	 }
			   	  */
						
			    }}, 200);
			}
		});
		

	};

	public void fChk() {
		long ll_cnt = 0, ll_cntm = 0;
		String ls_d = "", ls_dm = "";
		
		try
		{
			try
			{
				f_web_select("SELECT COUNT(1) CNT, MAX((SELECT MODI_DATE FROM AZ_MODI_DATE WHERE TABLE_CODE = '" + is_code + "')) MODI_DATE FROM " + is_code);
				//Log.i(TAG, "[결과 : " + gsDbRtn + " ----- 전체건수 : " + gjArr.length() + "-------------]");
				txtMsg.append("★★★[" + is_code + "] " + is_name + "★★★\n\n");
				txtMsg.append("---ERP 데이타-------------------->>>>>>\n");

				if (gjArr.length() <= 0) {
					txtMsg.append("서버에서 데이타를 조회할 수 없습니다.\n");
				} else {
					JSONObject jObj = gjArr.getJSONObject(0);  //추출
					ll_cnt = jObj.getLong("CNT");
					txtMsg.append("총건수 : " + ll_cnt + "건\n");
					ls_d = jObj.getString("MODI_DATE");
					if ((Util.f_isnull(ls_d)) || ls_d.equals("null")) {
						txtMsg.append("최종수정일 : 미수신\n");
					} else {
						txtMsg.append("최종수정일 : " + String.format("%s년%s월%s일 (%s시%s분%s초)", ls_d.substring(0, 4), ls_d.substring(4, 6), ls_d.substring(6, 8), ls_d.substring(9, 11), ls_d.substring(11, 13), ls_d.substring(13, 15) ) + "\n");
					}
					
				}
				
		
			} catch (Exception e) {
				e.printStackTrace();
				txtMsg.append("서버에서 데이타를 가져오는중에 오류가 발생하였습니다.\n");
				//Util.f_dialog(Detail.this,"서버에서 데이타를 가져오는중에 오류가 발생하였습니다.");
				//return;
			}

			
			try
			{
				txtMsg.append("\n---모바일 데이타----------------->>>>>>\n");
				Cursor cur;
				cur = DBH.DB.rawQuery("SELECT COUNT(1) CNT FROM " + is_code, null);
				cur.moveToNext();
				ll_cntm = cur.getInt(cur.getColumnIndex("CNT"));
				txtMsg.append("총건수 : " + ll_cntm + "건\n");
				
				cur = DBH.selectAZ_MODI_DATE(is_code);
				if (cur.getCount() <= 0) {
					txtMsg.append("최종수정일 : 미수신\n");
				} else {
					cur.moveToNext();
					ls_dm = cur.getString(cur.getColumnIndex("MODI_DATE_M"));
					if (Util.f_isnull(ls_dm)) {
						txtMsg.append("최종수정일 : 미수신\n");
					} else {
						txtMsg.append("최종수정일 : " + String.format("%s년%s월%s일 (%s시%s분%s초)", ls_dm.substring(0, 4), ls_dm.substring(4, 6), ls_dm.substring(6, 8), ls_dm.substring(9, 11), ls_dm.substring(11, 13), ls_dm.substring(13, 15) ) + "\n");
					}
				}
				cur.close();

			} catch (Exception e) {
				e.printStackTrace();
				txtMsg.append("모바일에서 데이타를 가져오는중에 오류가 발생하였습니다.\n");
				//Util.f_dialog(Detail.this,"모바일에서 데이타를 가져오는중에 오류가 발생하였습니다.");
				//return;
			}
			
			if ((ll_cnt == ll_cntm) && (ls_d.equals(ls_dm))) {
				if (Util.f_isnull(ls_d) || Util.f_isnull(ls_dm)) {
					txtMsg.append("\n\n결과 : 미수신") ;
				} else {
					txtMsg.append("\n\n결과 : 일치");
				}
			} else {
				txtMsg.append("\n\n결과 : 불일치");
			}
			
			
		} finally {
			if ( pd != null ) pd.hide();
		}
	
		
		
	}	
	

	public void fAZ_MODI_DATE() {
		try
		{
			f_web_select("SELECT TABLE_CODE, MODI_DATE FROM AZ_MODI_DATE");
	
	    	txtMsg.append("[결과 : " + gsDbRtn + " ----- 전체건수 : " + gjArr.length() + "-------------]\n");
	    	
	    	
    		DBH.fCreate("AZ_MODI_DATE");
	    	for (int i = 0; i < gjArr.length(); i++) {
	    		JSONObject jObj = gjArr.getJSONObject(i);  //추출
	    		
	    		String resultStr = String.format("테이블코드 : %s  최종수정일 : %s\n",
	                                      jObj.getString("TABLE_CODE"), jObj.getString("MODI_DATE"));
	    		
	    		txtMsg.append(resultStr);
	    		try {
	    			DBH.DB.execSQL(
	    					"UPDATE AZ_MODI_DATE" +
	    					"   SET MODI_DATE = '" + jObj.getString("MODI_DATE") + "'" +
	    					" WHERE TABLE_CODE = '" + jObj.getString("TABLE_CODE") + "'"
	    					);
	    		} catch (SQLiteException es) {
	    			DBH.DB.execSQL(
	    					"INSERT INTO AZ_MODI_DATE" +
	    					"           (TABLE_CODE, MODI_DATE)" +
	    					"    VALUES ('" + jObj.getString("TABLE_CODE") + "', '" + jObj.getString("MODI_DATE") + "')"
	    					);
	    		}
	    	}
	
		} catch (Exception e) {
			e.printStackTrace();
			Util.f_dialog(Detail.this,"서버에서 데이타를 가져오는중에 오류가 발생하였습니다.");
		} finally {
			if ( pd != null ) pd.hide();
		}
		
	}	
	
	public void fHttpTest() {
		try
		{
			f_web_select("SELECT EMP_CODE, EMP_NAME FROM BE_DEPT_EMP");
	
	    	txtMsg.append("[결과 : " + gsDbRtn + " ----- 전체건수 : " + gjArr.length() + "-------------]\n");
	    	
	    	for (int i = 0; i < gjArr.length(); i++) {
	    		JSONObject jObj = gjArr.getJSONObject(i);  //추출
	    		
	    		String resultStr = String.format("사번 : %s  사원명 : %s\n",
	                                      jObj.getString("EMP_CODE"), jObj.getString("EMP_NAME"));
	    		
	    		txtMsg.append(resultStr);
	    	}
	
		} catch (Exception e) {
			e.printStackTrace();
			Util.f_dialog(Detail.this,"서버에서 데이타를 가져오는중에 오류가 발생하였습니다.");
			//Toast.makeText(getBaseContext(), "데이타를 가져오는 중에 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
		} finally {
			if ( pd != null ) pd.hide();
		}
		
	}	
	
	
	//우선은 사용안함
	private void dbSelect() {
		try
		{
			HttpHost targetHost = new HttpHost(WEB_HOST, 80, "http");
			HttpPost httpPost = new HttpPost("/oracle/gateway.php?p_hash=" + P_HASH);
			httpPost.addHeader("Accept", "text/xml");

			httpPost.addHeader("Content-Type", "application/xml");
		    HttpParams params = new BasicHttpParams();
		    
		    params = params.setParameter("p_hash", P_HASH);
		    httpPost.setParams(params);

			try
			{
				StringBuilder sb = new StringBuilder();
		        sb.append("{\"service\":\"SESSION\",");
		        sb.append("\"className\":\"common.Session\",");
		        sb.append("\"method\":\"f_user_select\",");			        
		        sb.append("\"argus\":{");
		        sb.append("\"p_sql\":\"" + "SELECT EMP_CODE, EMP_NAME FROM BE_DEPT_EMP ".replace("+", "┼") + "\"");
		        sb.append("}}");
				
			    StringEntity entity = new StringEntity(sb.toString(), "UTF-8");  //한글처리
			    entity.setContentType("text/xml");
			    httpPost.setEntity(entity);

				
			    HttpResponse response = httpClient.execute(targetHost, httpPost);  //통신을시작한다.
			    HttpEntity hEntity = response.getEntity();  //응답온 데이터를 얻는다.
		    	Log.v(TAG, "request ok!!");

		    	if (hEntity != null) {
			    	InputStream instream = hEntity.getContent();
			    	
			    	String lsContent = convertStreamToString(instream);

			    	
			    	instream.close();
			    	//txtMsg.append(lsContent +  "\n-------------------\n");
			    	
			    	
			    	int ll = (int) lsContent.indexOf("W&s|") + 4;
			    	lsContent = lsContent.substring(ll);
			    	

			    	String lsResult = lsContent.substring(0, 1);
			    	String lsValue = lsContent.substring(1);
			    	
			    	JSONArray jArr = new JSONArray(lsValue);
			    	
			    	txtMsg.append("[결과 : " + lsResult + " ----- 전체건수 : " + jArr.length() + "-------------]\n");
		    		
			    	for (int i = 0; i < jArr.length(); i++) {
			    		JSONObject jObj = jArr.getJSONObject(i);  //추출
			    		
			    		String resultStr = String.format("사번 : %s  사원명 : %s\n",
                                                  jObj.getString("EMP_CODE"), jObj.getString("EMP_NAME"));
			    		
			    		txtMsg.append(resultStr);
			    	}
			    	
			    	
			    }
			}  catch (Exception e2) {
				e2.printStackTrace();
		        //txtMsg.append("----에러----\n");
			} 
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} finally {
			//dismissDialog(PROGRESS2_KEY);
			if ( pd != null ) pd.hide();
		}
	}
	
	
}