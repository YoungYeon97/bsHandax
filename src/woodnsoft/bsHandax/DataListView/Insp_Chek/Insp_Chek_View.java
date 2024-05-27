package woodnsoft.bsHandax.DataListView.Insp_Chek;

import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
@SuppressLint("ViewConstructor")
public class Insp_Chek_View extends LinearLayout  {
	private TextView itv_chk, itv_chek_code, itv_chek_name;
	
	LinearLayout ill_list_insp_chek;
	RelativeLayout irl_list_insp_chek;

	public Insp_Chek_View(Context context, Insp_Chek_Item aItem) {
		super(context);
 
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_insp_chek, this, true);
		
		// Set Text
		ill_list_insp_chek = (LinearLayout) findViewById(R.id.ll_list_insp_chek);
		irl_list_insp_chek = (RelativeLayout) findViewById(R.id.rl_list_insp_chek);
		itv_chk        = (TextView) findViewById(R.id.tv_chk);        itv_chk.setText(aItem.getData(0));         //0:선택유무
		itv_chek_code  = (TextView) findViewById(R.id.tv_chek_code);  itv_chek_code.setText(aItem.getData(1));   //1:점검코드
		itv_chek_name  = (TextView) findViewById(R.id.tv_chek_name);  itv_chek_name.setText(aItem.getData(2));   //2:점검명

		setColor_chk(aItem.getData(0));
	}
 
	public void setText(int index, String data) {
		if (index == 0) {
			itv_chk.setText(data);
			setColor_chk(data);
		} else if (index == 1) {
			itv_chek_code.setText(data);
		} else if (index == 2) {
			itv_chek_name.setText(data);
		} else {
			throw new IllegalArgumentException();
		}
	}
    
	public void setColor_chk(String data) {
		if (data.equals("1")) {  //1.모바일에서 입력
			ill_list_insp_chek.setBackgroundColor(getResources().getColor(R.color.chk_true));
			irl_list_insp_chek.setBackgroundColor(getResources().getColor(R.color.chk_true));
			itv_chek_name.setTextColor(getResources().getColor(R.color.chk_true_text));
			
		} else {  //0.Import받은 데이타
			ill_list_insp_chek.setBackgroundColor(getResources().getColor(R.color.chk_false));
			irl_list_insp_chek.setBackgroundColor(getResources().getColor(R.color.chk_false));
			itv_chek_name.setTextColor(getResources().getColor(R.color.chk_false_text));
			
		}
		
	}

}