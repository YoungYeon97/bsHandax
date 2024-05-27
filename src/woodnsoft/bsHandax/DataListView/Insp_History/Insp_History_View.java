package woodnsoft.bsHandax.DataListView.Insp_History;

import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
 
@SuppressLint("ViewConstructor")
public class Insp_History_View extends LinearLayout  {
	private TextView itv_reg_no, itv_reg_seq;
	private TextView itv_reg_date, itv_chek_text;
	LinearLayout ill_list_insp_history;
	
	public Insp_History_View(Context context, Insp_History_Item aItem) {
		super(context);
 
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_insp_history, this, true);
		// Set Text
		
		ill_list_insp_history = (LinearLayout) findViewById(R.id.ll_list_insp_history);
		itv_reg_no    = (TextView) findViewById(R.id.tv_reg_no);    itv_reg_no.setText(aItem.getData(0));      //0:전표번호
		itv_reg_seq   = (TextView) findViewById(R.id.tv_reg_seq) ;  itv_reg_seq.setText(aItem.getData(1));     //1:전표SEQ
		itv_reg_date  = (TextView) findViewById(R.id.tv_reg_date);  itv_reg_date.setText(aItem.getData(2));    //2:전표일자
		itv_chek_text = (TextView) findViewById(R.id.tv_chek_text); itv_chek_text.setText(aItem.getData(3));   //3:조치사항

	}
 
	public void setText(int index, String data) {
		if (index == 0) {
			itv_reg_no.setText(data);
		} else if (index == 1) {
			itv_reg_seq.setText(data);
		} else if (index == 2) {
			itv_reg_date.setText(data);
		} else if (index == 3) {
			itv_chek_text.setText(data);
		} else {
			throw new IllegalArgumentException();
		}
	}
    
    
 
}