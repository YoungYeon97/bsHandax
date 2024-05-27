package woodnsoft.bsHandax;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import woodnsoft.bsHandax2.R;

public final class Dialog_seri_appr_chk extends Dialog {
	
	//private String TAG = "Dialog_seri_appr_chk"; 
	
	EditText iet_user_emp_text;
	
	private OnDismissListener _listener;
	private OnCancelListener _cancle;

	public Dialog_seri_appr_chk(Context context) {  
      super(context);  
      // TODO Auto-generated constructor stub  
	}  	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_seri_appr_chk);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		iet_user_emp_text = (EditText) findViewById(R.id.et_user_emp_text);

		Button lb_cancel = (Button) findViewById(R.id.b_cancel);
		lb_cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if( _cancle == null ) {} else {  
					_cancle.onCancel( Dialog_seri_appr_chk.this ) ;
				}
				
				//dismiss() ;
				cancel();
			}

		});

		Button lb_ok = (Button) findViewById(R.id.b_ok);
		lb_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if( _listener == null ) {} else {  
					_listener.onDismiss( Dialog_seri_appr_chk.this ) ;  
				}
				
				dismiss() ;
			}
		});
		

	};

//	@Override
//	public void setOnCancelListener(OnCancelListener listener) {
//		// TODO Auto-generated method stub
//		super.setOnCancelListener(listener);
//	}
	@Override
	public void setOnCancelListener(OnCancelListener $listener) {
		// TODO Auto-generated method stub
		_cancle = $listener ; 
	}

	public void setOnDismissListener( OnDismissListener $listener ) {  
      _listener = $listener ; 
  }
	
   public String getName() {  
   	String ls_user_emp_text = iet_user_emp_text.getText().toString();
   	
//   	if (Util.f_isnull(ls_user_emp_text)) {
//   		Util.f_dialog(Dialog_seri_appr_chk.this, "");
//   		return 
//   	}
   	
      return ls_user_emp_text ;  
  }


}