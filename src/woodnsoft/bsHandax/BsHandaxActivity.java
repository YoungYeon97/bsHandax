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
	boolean btBackState = false; // 뒤로가기 버튼의 상태값을 갖는 변수
	Timer timer = new Timer(); // 타이머 변수
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		setContentView(R.layout.main);
		
		g_BsHandaxActivity = BsHandaxActivity.this;
		
		f_setp();  //환경설정값 읽어들임
		
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		
		//BS DATA 수신
		findViewById(R.id.b_dataget).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
        		Intent lilogin = new Intent(getBaseContext(), DataGet.class);
        		startActivity(lilogin);
        		//finish();
        		
			}
		});

		//BS DATA 전송
		findViewById(R.id.b_dataput).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Util.f_isnull(gs_make_code) || Util.f_isnull(gs_fact_code)) {
					Util.f_dialog(BsHandaxActivity.this,"제조사 및 공장이 수신되지 않았습니다.\nBS DATA를 수신한 후 재시도 하십시오!");
				} else {
	        		Intent lilogin = new Intent(getBaseContext(), DataPut.class);
	        		startActivity(lilogin);
				}
			}
		});

		//BS 점검관리...
		findViewById(R.id.b_bs).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Util.f_isnull(gs_make_code) || Util.f_isnull(gs_fact_code)) {
					Util.f_dialog(BsHandaxActivity.this,"제조사 및 공장이 수신되지 않았습니다.\nBS DATA를 수신한 후 재시도 하십시오!");
				} else {
					Intent linsp = new Intent(getBaseContext(), Insp.class);
					startActivity(linsp);
				}

			}
		});

		//종료
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
			ltv_fact_make.setText("미수신");
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
		if(btBackState == false) { // 한 번만 누른 경우
			timer.schedule(new TimerTask() {
				public void run() {
						btBackState = false;
				}
			}, 3000);
			btBackState = true;
			Toast.makeText(getBaseContext(), "\"뒤로\"버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
		} else  finish();  // 3초 내에 두 번 누른 경우
	}
	
}