package woodnsoft.bsHandax;

import woodnsoft.bsHandax.DataListView.Insp_Case.Insp_Case_Item;
import woodnsoft.bsHandax.DataListView.Insp_Case.Insp_Case_LIstView;
import woodnsoft.bsHandax.DataListView.Insp_Case.Insp_Case_ListAdapter;
import woodnsoft.bsHandax.DataListView.Insp_Case.OnDataSelectionListener_Case;
import woodnsoft.bsHandax.DataListView.Insp_Chek.Insp_Chek_Item;
import woodnsoft.bsHandax.DataListView.Insp_Chek.Insp_Chek_LIstView;
import woodnsoft.bsHandax.DataListView.Insp_Chek.Insp_Chek_ListAdapter;
import woodnsoft.bsHandax.DataListView.Insp_Chek.OnDataSelectionListener_Chek;
import woodnsoft.bsHandax.DataListView.Insp_Case_Chek.Insp_Case_Chek_Item;
import woodnsoft.bsHandax.DataListView.Insp_Case_Chek.Insp_Case_Chek_LIstView;
import woodnsoft.bsHandax.DataListView.Insp_Case_Chek.Insp_Case_Chek_ListAdapter;
import woodnsoft.bsHandax.DataListView.Insp_Case_Chek.OnDataSelectionListener_Case_Chek;
import woodnsoft.bsHandax.common.DBActivity;
import woodnsoft.bsHandax.common.Util;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("HandlerLeak")
public class Insp_Case_Chek extends DBActivity {
	
	private static final String TAG = "Insp_Case_Chek"; 
	Insp_Case_LIstView lv_case;
	Insp_Case_ListAdapter adapter_case;
	Insp_Chek_LIstView lv_chek;
	Insp_Chek_ListAdapter adapter_chek;
	Insp_Case_Chek_LIstView lv_case_chek;
	Insp_Case_Chek_ListAdapter adapter_case_chek;
	
	//Ÿ��Ʋ ��Ʈ��
	ProgressBar ipbtitle;
	TextView    itvtitle;
	int ii_bef_case, ii_bef_chek, ii_bef_case_chek;
	Boolean ibl_case = false, ibl_chek = false;
	
	String is_reg_no, is_reg_seq, is_reg_date, is_reg_code;
	String is_case_text = null, is_chek_text = null, is_state_flag = "1";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //Ŀ���� Ÿ��Ʋ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insp_case_chek);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		ipbtitle = (ProgressBar) findViewById(R.id.pbtitle);
		itvtitle = (TextView) findViewById(R.id.tvtitle);

		itvtitle.setText("BS ��ġ����, �����׸� ����");

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
         
		is_reg_no = bundle.getString("reg_no");
		is_reg_seq = bundle.getString("reg_seq");
		is_reg_date = bundle.getString("reg_date");
		is_reg_code = bundle.getString("reg_code");
		
		//--->>>�����׸� ����Ʈ��--->>>
		adapter_case = new Insp_Case_ListAdapter(this);
		lv_case = new Insp_Case_LIstView(this);
		LinearLayout lin_case = (LinearLayout)findViewById(R.id.linCase );
		lin_case.addView(lv_case);
		
		//--->>>��ġ�׸� ����Ʈ��--->>>
		adapter_chek = new Insp_Chek_ListAdapter(this);
		lv_chek = new Insp_Chek_LIstView(this);
		LinearLayout lin_chek = (LinearLayout)findViewById(R.id.linChek );
		lin_chek.addView(lv_chek);
		
		//--->>>����,��ġ�׸� ����Ʈ��--->>>
		adapter_case_chek = new Insp_Case_Chek_ListAdapter(this);
		lv_case_chek = new Insp_Case_Chek_LIstView(this);
		LinearLayout lin_case_chek = (LinearLayout)findViewById(R.id.linCase_Chek );
		lin_case_chek.addView(lv_case_chek);
	

		findViewById(R.id.b_ok).setOnClickListener(new OnClickListener() {  //Ȯ��
			public void onClick(View v) {
				f_save_insp_case_chek();

			}
		});
		
		findViewById(R.id.b_cancel).setOnClickListener(new OnClickListener() {  //���
			public void onClick(View v) {
   			finish();
			}
		});
		
		
		f_retrieve_case();
		f_retrieve_chek();
		f_retrieve_case_chek();  //��ϵ� ����,��ġ�׸� ��ȸ
		
		//�����׸� ����
		lv_case.setOnDataSelectionListener( new OnDataSelectionListener_Case () {
         
   		public void onDataSelected(AdapterView<?> parent, View v, int position, long id) {
   			Insp_Case_Item case_selectItem;
   			String mData[];
   			
   			case_selectItem = (Insp_Case_Item)adapter_case.getItem(ii_bef_case);
   			mData = case_selectItem.getData();
   			mData[0] = "0";  //���þ���
   			case_selectItem.setData(mData);   //����� ������ SetData

      		ii_bef_case = position;
   			
      		case_selectItem = (Insp_Case_Item)adapter_case.getItem(position);
   			mData = case_selectItem.getData();
   			mData[0] = "1";  //������
   			case_selectItem.setData(mData);   //����� ������ SetData
      		adapter_case.notifyDataSetChanged();  //��������� refalsh

	   		ibl_case = true;
      		f_add_case_chek();  //�����׸�, ��ġ���� ��� ���ý� �ڵ� �߰�

   		}
		});       		
		
		//��ġ�׸� ����
		lv_chek.setOnDataSelectionListener( new OnDataSelectionListener_Chek () {
	      
			public void onDataSelected(AdapterView<?> parent, View v, int position, long id) {
				Insp_Chek_Item chek_selectItem;
				String mData[];
				
				chek_selectItem = (Insp_Chek_Item)adapter_chek.getItem(ii_bef_chek);
				mData = chek_selectItem.getData();
				mData[0] = "0";  //���þ���
				chek_selectItem.setData(mData);   //����� ������ SetData
	
	   		ii_bef_chek = position;
				
	   		chek_selectItem = (Insp_Chek_Item)adapter_chek.getItem(position);
				mData = chek_selectItem.getData();
				mData[0] = "1";  //������
				chek_selectItem.setData(mData);   //����� ������ SetData
	   		adapter_chek.notifyDataSetChanged();  //��������� refalsh
	   		
	   		ibl_chek = true;
	   		f_add_case_chek();  //�����׸�, ��ġ���� ��� ���ý� �ڵ� �߰�
			}
		});

		//����,��ġ�׸� ����
		lv_case_chek.setOnDataSelectionListener( new OnDataSelectionListener_Case_Chek () {
         
   		public void onDataSelected(AdapterView<?> parent, View v, int position, long id) {
   			Insp_Case_Chek_Item selectItem;
				String mData[];

				if (ii_bef_case_chek >= 0) {
					selectItem = (Insp_Case_Chek_Item)adapter_case_chek.getItem(ii_bef_case_chek);
					mData = selectItem.getData();
					mData[0] = "0";  //���þ���
		   		selectItem.setData(mData);   //����� ������ SetData
				}
	
	   		ii_bef_case_chek = position;
				
	   		selectItem = (Insp_Case_Chek_Item)adapter_case_chek.getItem(position);
				mData = selectItem.getData();
				mData[0] = "1";  //������
	   		selectItem.setData(mData);   //����� ������ SetData
	   		adapter_case_chek.notifyDataSetChanged();  //��������� refalsh
			}
		});

		
		findViewById(R.id.b_delete).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (adapter_case_chek.getCount() <= 0) return;
				
				adapter_case_chek.remove(ii_bef_case_chek);

				if (ii_bef_case_chek > adapter_case_chek.getCount() - 1)	ii_bef_case_chek = ii_bef_case_chek - 1;
				//ii_bef_case_chek = 0;
				
				if (adapter_case_chek.getCount() > 0) {
					Insp_Case_Chek_Item selectItem;
					String mData[];
					selectItem = (Insp_Case_Chek_Item)adapter_case_chek.getItem(ii_bef_case_chek);
					mData = selectItem.getData();
					mData[0] = "1";  //������
		   		selectItem.setData(mData);   //����� ������ SetData
				}
				adapter_case_chek.notifyDataSetChanged();  //��������� refalsh
//				//findViewById(R.id.b_sub_delete)
			}
		});
		
	}
	

	public void f_add_case_chek() {
		if ((ibl_chek == false) || (ibl_case == false)) return;

		//�ణ�� �ð������� �����̸� �༭ ����ڰ� ������ ������ ��� �Ѵ�.
		new Handler().postDelayed(	new Runnable () {
					public void run() {
						f_add_case_chek_delayed();
					}
		}, 200);

	}
	
	public void f_add_case_chek_delayed() {  //�����׸�, ��ġ���� ��� ���ý� �ڵ� �߰�
		String ls_case_code, ls_case_name, ls_chek_code, ls_chek_name;
		
		ibl_chek = false; ibl_case = false;

		Insp_Case_Item case_selectItem;
		String mData[];
		
		case_selectItem = (Insp_Case_Item)adapter_case.getItem(ii_bef_case);
		mData = case_selectItem.getData();
		
		ls_case_code = case_selectItem.getData(1);
		ls_case_name = case_selectItem.getData(2);
		
		mData[0] = "0";  //���þ���
		case_selectItem.setData(mData);   //����� ������ SetData
		adapter_case.notifyDataSetChanged();  //��������� refalsh
		
		
		//��ġ�׸�-------->>>>>>
		Insp_Chek_Item chek_selectItem;
		
		chek_selectItem = (Insp_Chek_Item)adapter_chek.getItem(ii_bef_chek);
		mData = chek_selectItem.getData();

		ls_chek_code = chek_selectItem.getData(1);
		ls_chek_name = chek_selectItem.getData(2);
		
		mData[0] = "0";  //���þ���
		chek_selectItem.setData(mData);   //����� ������ SetData
		adapter_chek.notifyDataSetChanged();  //��������� refalsh
		
		adapter_case_chek.addItem(new Insp_Case_Chek_Item("0", "0000", ls_case_code, ls_case_name, ls_chek_code, ls_chek_name));
		adapter_case_chek.notifyDataSetChanged();  //��������� refalsh
		
		Toast.makeText(getBaseContext(), ls_case_name + ", " + ls_chek_name + "�� �߰��Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();

	}
	
	public void f_retrieve_case() {  //�����׸� ��ȸ
		try
		{
			String ls_case_code, ls_case_name;
			
			adapter_case.clear();
			Cursor curSelect;
			curSelect = DBH.DB.rawQuery(
			      "   SELECT CASE_CODE, CASE_NAME" +
	            "     FROM LA_CASE" +
	            "    WHERE USE_FLAG = '1'" +
	            " ORDER BY SORT_KEY, CASE_CODE", null);
			int li_count = curSelect.getCount();
			
			for (int i=0;i<li_count;i++){
				curSelect.moveToNext();
				ls_case_code = curSelect.getString(curSelect.getColumnIndex("CASE_CODE"));
				ls_case_name = curSelect.getString(curSelect.getColumnIndex("CASE_NAME"));
				
				adapter_case.addItem(new Insp_Case_Item("0", ls_case_code, ls_case_name));
			}
			curSelect.close();
			
			lv_case.setAdapter(adapter_case);
			adapter_case.notifyDataSetChanged();  //��������� refalsh
			
			//<<<---����Ʈ��<<<---

		} catch (Exception e) {
			Log.e(TAG, "f_retrieve_case");
			Util.f_dialog(Insp_Case_Chek.this,"�����׸��� �ҷ������߿� ������ �߻��Ͽ����ϴ�.");
		}
		
	}
	
	public void f_retrieve_chek() {  //��ġ�׸� ��ȸ
		try
		{
			String ls_chek_code, ls_chek_name;
			
			adapter_chek.clear();
			Cursor curSelect;
			curSelect = DBH.DB.rawQuery(
			      "   SELECT CHEK_CODE, CHEK_NAME" +
	            "     FROM LA_CHEK" +
	            "    WHERE USE_FLAG = '1'" +
	            " ORDER BY SORT_KEY, CHEK_CODE", null);
			int li_count = curSelect.getCount();
			
			for (int i=0;i<li_count;i++){
				curSelect.moveToNext();
				ls_chek_code = curSelect.getString(curSelect.getColumnIndex("CHEK_CODE"));
				ls_chek_name = curSelect.getString(curSelect.getColumnIndex("CHEK_NAME"));
				
				adapter_chek.addItem(new Insp_Chek_Item("0", ls_chek_code, ls_chek_name));
			}
			curSelect.close();
			
			lv_chek.setAdapter(adapter_chek);
			adapter_chek.notifyDataSetChanged();  //��������� refalsh
			
			//<<<---����Ʈ��<<<---

		} catch (Exception e) {
			Log.e(TAG, "f_retrieve_chek");
			Util.f_dialog(Insp_Case_Chek.this,"�����׸��� �ҷ������߿� ������ �߻��Ͽ����ϴ�.");
		}
		
	}

	public void f_retrieve_case_chek() {  //��ϵ� ����,��ġ�׸� ��ȸ
		try
		{
			String ls_reg_sseq, ls_case_code, ls_case_name, ls_chek_code, ls_chek_name;
			adapter_case_chek.clear();
			Cursor curSelect;
			curSelect = DBH.DB.rawQuery(
			      "   SELECT I.REG_SSEQ, I.CASE_CODE, LA_CASE.CASE_NAME, I.CHEK_CODE, LA_CHEK.CHEK_NAME" +
	            "     FROM LB_INSP_CASE_CHEK I, LA_CASE, LA_CHEK" +
	            "    WHERE I.REG_NO = '" + is_reg_no + "'" +
	            "      AND I.REG_SEQ = '" + is_reg_seq + "'" +
	            "      AND I.CASE_CODE = LA_CASE.CASE_CODE" +
	            "      AND I.CHEK_CODE = LA_CHEK.CHEK_CODE" +
	            " ORDER BY I.REG_SSKEY, I.REG_SSEQ", null);
			int li_count = curSelect.getCount();
			
			for (int i=0;i<li_count;i++){
				curSelect.moveToNext();
				ls_reg_sseq  = curSelect.getString(curSelect.getColumnIndex("REG_SSEQ"));
				ls_case_code = curSelect.getString(curSelect.getColumnIndex("CASE_CODE"));
				ls_case_name = curSelect.getString(curSelect.getColumnIndex("CASE_NAME"));
				ls_chek_code = curSelect.getString(curSelect.getColumnIndex("CHEK_CODE"));
				ls_chek_name = curSelect.getString(curSelect.getColumnIndex("CHEK_NAME"));
				
				adapter_case_chek.addItem(new Insp_Case_Chek_Item("0", ls_reg_sseq, ls_case_code, ls_case_name, ls_chek_code, ls_chek_name));
			}
			curSelect.close();
			
			lv_case_chek.setAdapter(adapter_case_chek);
			adapter_case_chek.notifyDataSetChanged();  //��������� refalsh
			
			//<<<---����Ʈ��<<<---

		} catch (Exception e) {
			Log.e(TAG, "f_retrieve_case_chek");
			Util.f_dialog(Insp_Case_Chek.this,"�����׸��� �ҷ������߿� ������ �߻��Ͽ����ϴ�.");
		}
		
	}

	public void f_save_insp_case_chek() {
		pd = ProgressDialog.show(Insp_Case_Chek.this, "","�����׸��� �����ϰ� �ֽ��ϴ�.\n��ø� ��ٷ� �ֽʽÿ�!", true);
		handler.postDelayed(new Runnable () {public void run() {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					f_save_insp_case_chek_run();
									
					Message msg = handler.obtainMessage();
					msg.obj = "save";
					handler.sendMessage(msg);
				}
			});
			thread.start();
		}}, 300);
	}
	
	public int f_save_insp_case_chek_run() {
		String ls_delete = null, ls_insert = null;
		is_case_text = null; is_chek_text = null; is_state_flag = "1";
		
			if ((Util.f_isnull(is_reg_no)) == true) return 100;
	
			try {
				ls_delete =	"DELETE FROM LB_INSP_CASE_CHEK WHERE REG_NO = '" + is_reg_no + "' AND REG_SEQ = '" + is_reg_seq + "'";
		 		DBH.DB.execSQL(ls_delete);
		 		
		 		
			} catch (Exception e) {
	 			Log.e(TAG, "���˳���(�����׸�) ���� : " + ls_delete);
	 			Util.f_dialog(Insp_Case_Chek.this,"�����׸�(������)�� �����߿� ������ �߻��Ͽ����ϴ�.");
				return -1;
			}

			
			
			try {
				String ls_max_reg_sseq = "", ls_case_code = "", ls_chek_code = "", ls_bigo = "";
				
				String mData[];
				
				DBH.DB.beginTransaction();
				for (int i=0;i<adapter_case_chek.getCount();i++){
					Insp_Case_Chek_Item selectItem;
					selectItem = (Insp_Case_Chek_Item)adapter_case_chek.getItem(i);
					mData = selectItem.getData();  //0:�������� 1:REG_SSEQ 2:�����ڵ� 3:���˸� 4:��ġ�ڵ� 5:��ġ��
					ls_case_code = mData[2];
					ls_chek_code = mData[4];
					
					if (Util.f_isnull(is_case_text)) is_case_text = mData[3]; else is_case_text = is_case_text + ", " + mData[3];
					if (Util.f_isnull(is_chek_text)) is_chek_text = mData[5]; else is_chek_text = is_chek_text + ", " + mData[5];
					is_state_flag = "0";  //0.X 1.O
					
					ls_max_reg_sseq = String.format("%04d", i + 1);
				
					ls_insert = 
							"INSERT INTO LB_INSP_CASE_CHEK" +
					      "           (REG_NO,    REG_SEQ,   REG_SSEQ, REG_SSKEY, REG_DATE, REG_CODE," +
					      "            CASE_CODE, CHEK_CODE, BIGO)" +
	                  "    VALUES ('" + is_reg_no + "', " + 
	                              "'" + is_reg_seq + "', " + 
	                              "'" + ls_max_reg_sseq + "', " + 
	                              "'" + ls_max_reg_sseq + "', " + 
	                              "'" + is_reg_date + "', " + 
	                              "'" + "0001" + "', " + 
	                              
	                              "'" + ls_case_code + "', " +
	                              "'" + ls_chek_code + "', " +
	                              "'" + ls_bigo + "')";
					DBH.DB.execSQL(ls_insert);
				}
				DBH.DB.setTransactionSuccessful();  //commit;
					

				
			} catch (Exception e) {
	 			Log.e(TAG, "���˳���(�����׸�) ���� : " + ls_delete);
	 			Util.f_dialog(Insp_Case_Chek.this,"���˳���(�����׸�)�� �����߿� ������ �߻��Ͽ����ϴ�.");
				return -1;
	 		} finally {
	 			DBH.DB.endTransaction();
			}

			return 0;
	}

	
	public Handler handler = new Handler() {
      public void handleMessage(Message msg) {

			if (msg.obj.equals("save")) {
				Intent intent = getIntent(); // �� ��Ƽ��Ƽ�� �����ϰ� �� ����Ʈ�� ȣ��
				intent.putExtra("case_text", is_case_text);
				Log.i(TAG, "ls_case_text:" + is_case_text);
				intent.putExtra("chek_text", is_chek_text);
				intent.putExtra("state_flag", is_state_flag);
				
				setResult(RESULT_OK,intent);
				finish();
      	}

      	if ( pd != null ) pd.hide();
      }
	};


	
}
