package woodnsoft.bsHandax;

import java.util.Timer;
import java.util.TimerTask;

import woodnsoft.bsHandax.common.DBActivity;
import woodnsoft.bsHandax.common.Util;
import woodnsoft.bsHandax.db.DBH;
import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UnlocalizedSms")
public class BsHandaxActivity extends DBActivity {
	/** Called when the activity is first created. */
//	private static final String TAG = "DataGet";
	boolean btBackState = false; // �ڷΰ��� ��ư�� ���°��� ���� ����
	Timer timer = new Timer(); // Ÿ�̸� ����
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //Ŀ���� Ÿ��Ʋ
		setContentView(R.layout.main);
		
		g_BsHandaxActivity = BsHandaxActivity.this;
		
		f_setp();  //ȯ�漳���� �о����
		
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		
		//BS DATA ����
		findViewById(R.id.b_dataget).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
        		Intent lilogin = new Intent(getBaseContext(), DataGet.class);
        		startActivity(lilogin);
        		//finish();
        		
			}
		});

		//BS DATA ����
		findViewById(R.id.b_dataput).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Util.f_isnull(gs_make_code) || Util.f_isnull(gs_fact_code)) {
					Util.f_dialog(BsHandaxActivity.this,"������ �� ������ ���ŵ��� �ʾҽ��ϴ�.\nBS DATA�� ������ �� ��õ� �Ͻʽÿ�!");
				} else {
	        		Intent lilogin = new Intent(getBaseContext(), DataPut.class);
	        		startActivity(lilogin);
				}
			}
		});

		//BS ���˰���...
		findViewById(R.id.b_bs).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Util.f_isnull(gs_make_code) || Util.f_isnull(gs_fact_code)) {
					Util.f_dialog(BsHandaxActivity.this,"������ �� ������ ���ŵ��� �ʾҽ��ϴ�.\nBS DATA�� ������ �� ��õ� �Ͻʽÿ�!");
				} else {
					Intent linsp = new Intent(getBaseContext(), Insp.class);
					startActivity(linsp);
				}

			}
		});

		//����
		findViewById(R.id.bclose).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

	
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		TextView ltv_fact_make = (TextView) findViewById(R.id.tv_fact_make);
		if (Util.f_isnull(gs_make_code)) {
			ltv_fact_make.setText("�̼���");
		} else {
			ltv_fact_make.setText(gs_make_name + " " + gs_fact_name);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(DBH.DB != null) DBH.closeDatabase();
	}
	
	public void onBackPressed() {
		if(btBackState == false) { // �� ���� ���� ���
			timer.schedule(new TimerTask() {
				public void run() {
						btBackState = false;
				}
			}, 3000);
			btBackState = true;
			Toast.makeText(getBaseContext(), "\"�ڷ�\"��ư�� �ѹ� �� �����ø� ����˴ϴ�.", Toast.LENGTH_SHORT).show();
		} else  finish();  // 3�� ���� �� �� ���� ���
	}
	
}