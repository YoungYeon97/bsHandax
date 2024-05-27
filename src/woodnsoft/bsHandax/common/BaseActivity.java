package woodnsoft.bsHandax.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.w3c.dom.Document;

import woodnsoft.bsHandax2.R;
import woodnsoft.bsHandax.db.DBH;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	public final String gs_ver = "2.180323_2"; //"2.150618"; //"2.130514"; //"1.121030";
	public final String WEB_HOST = "58.181.26.249";  //bsHandax
	//public final String WEB_HOST = "192.168.0.33";
	//public final String WEB_HOST = "218.38.136.66";  //����(������)
   public final String gs_downfile = "http://58.181.26.249/bsHandax/upgrade/bsHandax.apk";  //�ٿ�ε� ȭ��

   public static String gs_serial_key;  //���α׷� �ø��� ��ȣ(����ں��� �ο�) BsHandaxActivary���� ����
	public final String TAG = "BaseActivity";
	public final String DATEFORMAT = "%04d-%02d-%02d";
	public Calendar cal = Calendar.getInstance( );
	public final String CUR_DATE = String.format(DATEFORMAT,cal.get(Calendar.YEAR),(cal.get(Calendar.MONTH) + 1),cal.get(Calendar.DAY_OF_MONTH));
	public final String P_HASH = Util.makeMD5("dnemdos" + CUR_DATE);
   

	//ȯ�漳����(��������, ���̵�(�������Ӿ��̵�), �н�����(���������н�����), �α�����������, �ڵ��α�������,
	//           ����BS���˳�������������, �������ڵ�, �������, �����ڵ�, �����, ���ΰ����ڵ�, ���ΰ����, �����ڵ���ǥ��ȣ, �۾����ڵ�, �۾��ڸ�)
	public static String gs_appr_flag, gs_user_id, gs_user_pwd, gs_login_save, gs_login_auto;
	public static String gs_last_get_date, gs_make_code, gs_make_name, gs_fact_code, gs_fact_name, gs_fact_sub_code, gs_fact_sub_name, gs_insp_reg_no, gs_emp_code, gs_emp_name;

	public static String gs_insp_reg_date, gs_reg_date, gs_reg_no, gs_mobi_flag;
	
	public static final int PROGRESS1_KEY = 1001;
	public static final int PROGRESS2_KEY = 1002;	
	public Context ctx = null;
	public SharedPreferences pref = null;
 
	final Calendar isC = Calendar.getInstance();
	public int curYear  = isC.get(Calendar.YEAR);
	public int curMonth = isC.get(Calendar.MONTH);
	public int curDay   = isC.get(Calendar.DAY_OF_MONTH);

	//��¥ ���̾�α׿�
	public Calendar gcal  = Calendar.getInstance();
	public Calendar gcal1 = Calendar.getInstance();
	public Calendar gcal2 = Calendar.getInstance();
    
    
	public static ProgressDialog pd = null, pd_bef = null;

	//JSON��
	public JSONArray gjArr;
	public String gsDbRtn, gsValue;  //db���ϰ�(0.���� 1.����), db��
   
	public static Activity g_BsHandaxActivity;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Log.d(R.string.MSG_TITLE,"1");
        super.onCreate(savedInstanceState);
		ctx = BaseActivity.this;
		pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference
		//loginInfo = LoginData.getInstance();  yhk�� �ּ� ó����
		
		Resources res = getResources();
    }
	   
   public String convertStreamToString(InputStream is) {

   	BufferedReader reader = new BufferedReader(new InputStreamReader(is));

   	StringBuilder sb = new StringBuilder();

   	String line = null;

   	try {
   		while ((line = reader.readLine()) != null) {
   			sb.append(line + "\n");
   		}

   	} catch (IOException e) {
   		e.printStackTrace();
   	} finally {
   		try {
   			is.close();
   		} catch (IOException e) {
   			e.printStackTrace();
   		}
   	}
   	return sb.toString();
	}
    
	public HttpPost httpPost = new HttpPost("/service/gateway.php?p_hash=" + P_HASH);
	public final HttpHost targetHost = new HttpHost(WEB_HOST, 80, "http");
	public final DefaultHttpClient httpClient = new DefaultHttpClient();
	
	// Define the actual handler for the event.
    public Document getDataXML ()
    {
		httpPost.addHeader("Accept", "text/xml");
		httpPost.addHeader("Content-Type", "application/xml");
	    HttpParams params = new BasicHttpParams();
	    params = params.setParameter("p_hash", P_HASH);
	    httpPost.setParams(params);
	    //Log.i(TAG,"executing request: " + httpPost.getRequestLine() );
	    return null;
    }
    
    // Define the actual handler for the event.
    public void draw()
    {
    	
	    // Wow!  Something really interesting must have occurred!
	    // Do something...
    	
    }
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(0,1,0,"�Ұ�");
		//menu.add(0,2,0,"���׷��̵�");
		//menu.add(0,3,0,"����");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			//if (Util.f_isnull(gs_serial_key)) gs_serial_key = Installation.id(getApplicationContext());  //���α׷� �ø��� ��ȣ(����ں��� �ο�)
			
    		new AlertDialog.Builder(this)
    		.setTitle(R.string.MSG_TITLE)
    		//.setIcon(R.drawable.title_icon)
    		.setMessage("���α׷��� : BS HANDAX\n" +
    				      "�ø����ȣ : " + gs_serial_key + "\n" +
    				      "Ver : " + gs_ver)
    		.setIcon(R.drawable.woodn_logo2)
    		.setPositiveButton("�ݱ�", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			})
    		.show();
			return true;

						
//		case 3:
//			android.os.Process.killProcess(android.os.Process.myPid());
//			
//			
//			finish();
//			System.exit(0);
//			return true;
		}
		return false;
	}    

	//���� ���� ����Ŭ ���� DB ������ ����Ÿ ������
	public void f_web_select(String asSql) {
		gjArr = null; gsDbRtn = "0"; gsValue = null;
		HttpHost targetHost = new HttpHost(WEB_HOST, 80, "http");
		HttpPost httpPost = new HttpPost("/bsHandax/oracle/gateway.php?p_hash=" + P_HASH);
		httpPost.addHeader("Accept", "text/xml");

		httpPost.addHeader("Content-Type", "application/xml");
		HttpParams params = new BasicHttpParams();
	    
		params = params.setParameter("p_hash", P_HASH);
		httpPost.setParams(params);

		try
		{
			asSql = asSql.replace("+", "��"); //post�� �ѱ涧 ����"+"�� ������ �ٸ����ڷ� ��ȯ�ؼ� �ѱ�
			asSql = asSql.replace(", 'null'", ", null");
			
			StringBuilder sb = new StringBuilder();
			sb.append("{\"service\":\"SESSION\",");
			sb.append("\"className\":\"common.Session\",");
			sb.append("\"method\":\"f_user_select\",");			        
			sb.append("\"argus\":{");
			sb.append("\"p_sql\":\"" + asSql + "\"");
			sb.append("}}");
			
			StringEntity entity = new StringEntity(sb.toString(), "UTF-8");  //�ѱ�ó��
			entity.setContentType("text/xml");
			httpPost.setEntity(entity);

			
			HttpResponse response = httpClient.execute(targetHost, httpPost);  //����������Ѵ�.
			HttpEntity hEntity = response.getEntity();  //����� �����͸� ��´�.

	    	if (hEntity !=null) {
		    	InputStream instream = hEntity.getContent();
		    	
		    	String lsContent = convertStreamToString(instream);

		    	
		    	instream.close();
		    	
		    	int ll = (int) lsContent.indexOf("W&s|") + 4;
		    	lsContent = lsContent.substring(ll);
		    	

		    	gsDbRtn = lsContent.substring(0, 1);  //1:���� 0:����
		    	gsValue = lsContent.substring(1);
		    	
		    	//Log.i(TAG, lsContent.substring(1));
		    	
		    	gjArr = new JSONArray(gsValue);
		    	
		    	
		    }
		}  catch (Exception e2) {
			Log.i(TAG, "f_web_exec:" + asSql);
			e2.printStackTrace();
		}
	}

	
	//���� ���� ����Ŭ ���� DB ������ INSERT �Ǵ� UPDATE ����
	public void f_web_exec(String asSql) {
		
		String lsContent = null;
		gjArr = null; gsDbRtn = "0"; gsValue = null;
		

		try
		{
			HttpHost targetHost = new HttpHost(WEB_HOST, 80, "http");
			HttpPost httpPost = new HttpPost("/bsHandax/oracle/gateway.php?p_hash=" + P_HASH);
			httpPost.addHeader("Accept", "text/xml");

			httpPost.addHeader("Content-Type", "application/xml");
			//HttpParams params = new BasicHttpParams();
			HttpParams params = httpClient.getParams();
 
			params = params.setParameter("p_hash", P_HASH);
			httpPost.setParams(params);

			
			//Log.i(TAG, "f_web_select2 : " + asSql);
			
			asSql = asSql.replace("+", "��"); //post�� �ѱ涧 ����"+"�� ������ �ٸ����ڷ� ��ȯ�ؼ� �ѱ�
			asSql = asSql.replace(", 'null'", ", null");
			
			StringBuilder sb = new StringBuilder();
			sb.append("{\"service\":\"SESSION\",");
			sb.append("\"className\":\"common.Session\",");
			sb.append("\"method\":\"f_user_exec\",");			        
			sb.append("\"argus\":{");
			sb.append("\"p_sql\":\"" + asSql + "\"");
			sb.append("}}");
			
			StringEntity entity = new StringEntity(sb.toString(), "UTF-8");  //�ѱ�ó��
			entity.setContentType("text/xml");
			httpPost.setEntity(entity);
			
			
			HttpResponse response = httpClient.execute(targetHost, httpPost);  //����������Ѵ�.
			
			HttpEntity hEntity = response.getEntity();  //����� �����͸� ��´�.
			
	    	if (hEntity !=null) {
		    	InputStream instream = hEntity.getContent();
		    	
		    	lsContent = convertStreamToString(instream);
				
		    	instream.close();

		    	if (lsContent.indexOf("Can not connect") >= 0) {  //��Ȯ�� ������ �𸣰����� ���� �����Ҷ��� ����
		    		Log.i(TAG, "ù��° ��õ�(can not connect) : " + asSql);
		    		HttpResponse response2 = httpClient.execute(targetHost, httpPost);  //����������Ѵ�.
		    		HttpEntity hEntity2 = response2.getEntity();  //����� �����͸� ��´�.
		    		InputStream instream2 = hEntity2.getContent();
			    	
			    	lsContent = convertStreamToString(instream2);
					
			    	instream2.close();
		    	}
		    	if (lsContent.indexOf("Can not connect") >= 0) {  //��Ȯ�� ������ �𸣰����� ���� �����Ҷ��� ����
		    		Log.i(TAG, "�ι�° ��õ�(can not connect : " + asSql);
		    		HttpResponse response3 = httpClient.execute(targetHost, httpPost);  //����������Ѵ�.
		    		HttpEntity hEntity3 = response3.getEntity();  //����� �����͸� ��´�.
		    		InputStream instream3 = hEntity3.getContent();
			    	
			    	lsContent = convertStreamToString(instream3);
					
			    	instream3.close();
		    	}
		    	
		    	int ll = (int) lsContent.indexOf("W&s|") + 4;
		    	lsContent = lsContent.substring(ll);
		    	

		    	gsDbRtn = lsContent.substring(0, 1);  //1:���� 0:����
		    	gsValue = lsContent.substring(1);
		    	
		    	//Log.i(TAG, lsContent.substring(1));
				
		    	gjArr = new JSONArray(gsValue);
				
		    }
	    	
		}  catch (Exception e2) {
			Log.i(TAG, "f_web_select : " + asSql);
			e2.printStackTrace();
			
		} finally {
			//httpClient2.getConnectionManager().shutdown();
		}
	}


	//ȯ�漳���� �о����
	public void f_setp() {
		try {
			if (DBH.fCreate("LA_SETP") == false) {
				Util.f_dialog(BaseActivity.this,"ȯ�漳��(LA_SETP) ���̺��� �����߿� ������ �߻��Ͽ����ϴ�.");
			}
			
			//Log.i(TAG, "getappli:" + getApplicationContext());
			if (Util.f_isnull(gs_serial_key)) gs_serial_key = Installation.id(getApplicationContext());  //���α׷� �ø��� ��ȣ(����ں��� �ο�)
			if (Util.f_isnull(gs_serial_key)) gs_serial_key = "none test";
			//if (Util.f_isnull(gs_serial_key)) gs_serial_key = "2635fc6b-e72d-4ed3-8980-089edda9de76"; 
	 		
			//ȯ�漳����(��������, ���̵�(�������Ӿ��̵�), �н�����(���������н�����), �α�����������, �ڵ��α�������,
			//           ����BS���˳�������������, �������ڵ�, �������, �����ڵ�, �����, ���ΰ����ڵ�, ���ΰ����, �����ڵ���ǥ��ȣ, �۾����ڵ�, �۾��ڸ�)
			Cursor curSelect = DBH.DB.rawQuery("SELECT MOBI_SERI, APPR_FLAG, USER_ID, USER_PWD, LOGIN_SAVE, LOGIN_AUTO," +
					                             " LAST_GET_DATE, MAKE_CODE, MAKE_NAME, FACT_CODE, FACT_NAME, FACT_SUB_CODE, FACT_SUB_NAME, INSP_REG_NO, EMP_CODE, EMP_NAME FROM LA_SETP", null);
			if (curSelect.getCount() <= 0) {
				DBH.DB.execSQL("INSERT INTO LA_SETP (SETP_DATE, MOBI_SERI) VALUES ('" + CUR_DATE + "', '" + gs_serial_key + "')");
			} else {
				curSelect.moveToNext();

				gs_appr_flag = curSelect.getString(curSelect.getColumnIndex("APPR_FLAG"));
				gs_user_id = curSelect.getString(curSelect.getColumnIndex("USER_ID"));
				gs_user_pwd = curSelect.getString(curSelect.getColumnIndex("USER_PWD"));
				gs_login_save = curSelect.getString(curSelect.getColumnIndex("LOGIN_SAVE"));
				gs_login_auto = curSelect.getString(curSelect.getColumnIndex("LOGIN_AUTO"));

				gs_last_get_date = curSelect.getString(curSelect.getColumnIndex("LAST_GET_DATE"));
				gs_make_code = curSelect.getString(curSelect.getColumnIndex("MAKE_CODE"));
				gs_make_name = curSelect.getString(curSelect.getColumnIndex("MAKE_NAME"));
				gs_fact_code = curSelect.getString(curSelect.getColumnIndex("FACT_CODE"));
				gs_fact_name = curSelect.getString(curSelect.getColumnIndex("FACT_NAME"));
				gs_fact_sub_code = curSelect.getString(curSelect.getColumnIndex("FACT_SUB_CODE"));
				gs_fact_sub_name = curSelect.getString(curSelect.getColumnIndex("FACT_SUB_NAME"));
				gs_insp_reg_no = curSelect.getString(curSelect.getColumnIndex("INSP_REG_NO"));
				gs_emp_code = curSelect.getString(curSelect.getColumnIndex("EMP_CODE"));
				gs_emp_name = curSelect.getString(curSelect.getColumnIndex("EMP_NAME"));

			
			}
	 		curSelect.close();
		} catch (Exception e) {
 			Log.e(TAG, "ȯ�漳�� : " + "INSERT INTO LA_SETP (SETP_DATE, MOBI_SERI) VALUES ('" + CUR_DATE + "', '" + gs_serial_key + "')");
 			Util.f_dialog(BaseActivity.this,"ȯ�漳�� ������ �������� �߿� ������ �߻��Ͽ����ϴ�.");
 		}
		
	}


}