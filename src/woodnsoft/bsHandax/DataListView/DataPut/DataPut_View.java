package woodnsoft.bsHandax.DataListView.DataPut;

import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
 
@SuppressLint("ViewConstructor")
public class DataPut_View extends LinearLayout  {
	private TextView itv_reg_no, itv_fact_sub_name, itv_lk_insp_no, itv_reg_date, itv_emp_name, itv_qty, itv_put_flag;
	
	public DataPut_View(Context context, DataPut_Item aItem) {
		super(context);
 
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_dataput, this, true);
		
		// Set Text
		itv_reg_no        = (TextView) findViewById(R.id.tv_reg_no);        itv_reg_no.setText(aItem.getData(0));        //00:전표번호
		                                                                                                                 //01:세부공장코드
		itv_fact_sub_name = (TextView) findViewById(R.id.tv_fact_sub_name); itv_fact_sub_name.setText(aItem.getData(2)); //02:세부공장명
		                                                                                                                 //03:점검코드
		itv_lk_insp_no    = (TextView) findViewById(R.id.tv_lk_insp_no);    itv_lk_insp_no.setText(aItem.getData(4));    //04:점검코드텍스트
		                                                                                                                 //05:작업일자
		itv_reg_date      = (TextView) findViewById(R.id.tv_reg_date);      itv_reg_date.setText(aItem.getData(6));      //06:작업일자텍스트
		                                                                                                                 //07:작업자코드
		itv_emp_name      = (TextView) findViewById(R.id.tv_emp_name);      itv_emp_name.setText(aItem.getData(8));      //08:작업자명
		itv_qty           = (TextView) findViewById(R.id.tv_qty);           itv_qty.setText(aItem.getData(9));           //09:점검건수
		itv_put_flag      = (TextView) findViewById(R.id.tv_put_flag);      itv_put_flag.setText(aItem.getData(10));     //10:전송여부

		setColor_put_flag(aItem.getData(10));
	}
 
	public void setText(int index, String data) {
		if (index == 0) {
			itv_reg_no.setText(data);
		} else if (index == 1 ) {
		} else if (index == 2 ) {
			itv_fact_sub_name.setText(data);
		} else if (index == 3 ) {
		} else if (index == 4) {
			itv_lk_insp_no.setText(data);
		} else if (index == 5 ) {
		} else if (index == 6) {
			itv_reg_date.setText(data);
		} else if (index == 7 ) {
		} else if (index == 8) {
			itv_emp_name.setText(data);
		} else if (index == 9) {
			itv_qty.setText(data);
		} else if (index == 10) {
			itv_put_flag.setText(data);
			setColor_put_flag(data);
		} else {
			Log.i("View", index + "/" + data);
			throw new IllegalArgumentException();
		}
	}

   public void setColor_put_flag(String data) {
      if (data.equals("대기")) {
      	itv_put_flag.setTextColor(getResources().getColor(R.color.lk0));
      } else if (data.equals("진행")) {
      	itv_put_flag.setTextColor(getResources().getColor(R.color.lk1));
      } else if (data.equals("실패")) {
      	itv_put_flag.setTextColor(getResources().getColor(R.color.lk9));
      } else {  //완료
      	itv_put_flag.setTextColor(getResources().getColor(R.color.lk2));
      }
		
	}
    
 
}