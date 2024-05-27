package woodnsoft.bsHandax.DataListView.Insp_Search;

import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
 
@SuppressLint("ViewConstructor")
public class Insp_Search_View extends LinearLayout  {
	private TextView itv_mobi_flag, itv_reg_no, itv_reg_seq;
	private TextView itv_bar_code, itv_item_spec, itv_jata_flag, itv_line_name, itv_macn_name, itv_state_flag, itv_bigo, itv_op_no;
	LinearLayout ill_list_insp;
	
	public Insp_Search_View(Context context, Insp_Search_Item aItem) {
		super(context);
 
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_insp_search, this, true);
		// Set Text
		
		ill_list_insp = (LinearLayout) findViewById(R.id.ll_list_insp);
		itv_mobi_flag  = (TextView) findViewById(R.id.tv_mobi_flag);  itv_mobi_flag.setText(aItem.getData(0));   //0:모바일수정유무
		itv_reg_no     = (TextView) findViewById(R.id.tv_reg_no);     itv_reg_no.setText(aItem.getData(1));      //1:전표번호
		itv_reg_seq    = (TextView) findViewById(R.id.tv_reg_seq) ;   itv_reg_seq.setText(aItem.getData(2));     //2:전표SEQ
		itv_bar_code   = (TextView) findViewById(R.id.tv_bar_code);   itv_bar_code.setText(aItem.getData(3));    //3:시리얼번호
		itv_item_spec  = (TextView) findViewById(R.id.tv_item_spec);  itv_item_spec.setText(aItem.getData(4));   //4:규격
		itv_jata_flag  = (TextView) findViewById(R.id.tv_jata_flag);  itv_jata_flag.setText(aItem.getData(5));   //5:자타구분(1.자사, 0.타사)
		itv_line_name  = (TextView) findViewById(R.id.tv_line_name);  itv_line_name.setText(aItem.getData(6));   //6:작업라인
		itv_macn_name  = (TextView) findViewById(R.id.tv_macn_name);  itv_macn_name.setText(aItem.getData(7));   //7:머신명
		itv_state_flag = (TextView) findViewById(R.id.tv_state_flag); itv_state_flag.setText(aItem.getData(8));  //8:상태(0.X 1.O)
		itv_bigo       = (TextView) findViewById(R.id.tv_bigo);       itv_bigo.setText(aItem.getData(9));        //9:비고
		itv_op_no      = (TextView) findViewById(R.id.tv_op_no);      itv_op_no.setText(aItem.getData(10));      //10:OP/NO

		setColor_mobi_flag(aItem.getData(0));
		setColor_jata_flag(aItem.getData(5));
		setColor_state_flag(aItem.getData(8));
	}
 
	public void setText(int index, String data) {
		String ls_data;
   	 
		if (index == 0) {
			if (data.equals("1")) { ls_data = "1"; } else { ls_data = "0"; }   itv_mobi_flag.setText(ls_data);
			setColor_mobi_flag(data);
		} else if (index == 1) {
			itv_reg_no.setText(data);
		} else if (index == 2) {
			itv_reg_seq.setText(data);
		} else if (index == 3) {
			itv_bar_code.setText(data);
		} else if (index == 4) {
			itv_item_spec.setText(data);
		} else if (index == 5) {
			itv_jata_flag.setText(data);
			setColor_jata_flag(data);
		} else if (index == 6) {
			itv_line_name.setText(data);
		} else if (index == 7) {
			itv_macn_name.setText(data);
		} else if (index == 8) {
			itv_state_flag.setText(data);
			setColor_state_flag(data);
		} else if (index == 9) {
			itv_bigo.setText(data);
		} else if (index == 10) {
			itv_op_no.setText(data);
		} else {
			throw new IllegalArgumentException();
		}
	}
    
 	public void setColor_mobi_flag(String data) {
		if (data.equals("1")) {  //1.모바일에서 입력
			ill_list_insp.setBackgroundColor(getResources().getColor(R.color.mobi_flag_1));
		} else {  //0.Import받은 데이타
			ill_list_insp.setBackgroundColor(getResources().getColor(R.color.mobi_flag_0));
		}
		
	}
    
 	public void setColor_jata_flag(String data) {
		if (data.equals("자사")) {  //1.자사
			itv_jata_flag.setTextColor(getResources().getColor(R.color.jata_flag_1));
		} else {  //0.타사
			itv_jata_flag.setTextColor(getResources().getColor(R.color.jata_flag_0));
		}
		
	}
    
	public void setColor_state_flag(String data) {
		if (data.equals("X")) {  //0.X
			itv_state_flag.setTextColor(getResources().getColor(R.color.state_flag_0));
		} else {  //1.O
			itv_state_flag.setTextColor(getResources().getColor(R.color.state_flag_1));
		}
		
	}
    
 
}