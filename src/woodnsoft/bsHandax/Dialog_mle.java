package woodnsoft.bsHandax;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import woodnsoft.bsHandax2.R;

public final class Dialog_mle extends Dialog {
	
	//private String TAG = "Dialog_mle"; 
	
	EditText iet;
	TextView itv;
	
	private OnDismissListener _listener ;  

	public Dialog_mle(Context context) {  
      super(context);  
      // TODO Auto-generated constructor stub  
	}  	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);   //커스텀 타이틀
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mle);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		iet = (EditText) findViewById(R.id.et);
		itv = (TextView) findViewById(R.id.tv);

		iet.setFocusable(true);
		iet.setSelection(0, iet.getText().length() );
		iet.requestFocus();  //포커스를 준다.
		
		
		
		iet.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				iet.setText("");
				return false;
			}
		});
		
		 
		Button lb_cancel = (Button) findViewById(R.id.b_cancel);
		lb_cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				cancel();
			}

		});
		


		Button lb_ok = (Button) findViewById(R.id.b_ok);
		lb_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if( _listener == null ) {} else {  
					_listener.onDismiss( Dialog_mle.this ) ;  
				}
				
				dismiss() ;
			}
		});
		

	};


	public void setOnDismissListener( OnDismissListener $listener ) {  
      _listener = $listener ; 
  }  

   public String getName() {  
      return iet.getText().toString() ;  
  }  	
}