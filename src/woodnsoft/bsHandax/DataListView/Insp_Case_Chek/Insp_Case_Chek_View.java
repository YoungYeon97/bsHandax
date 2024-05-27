package woodnsoft.bsHandax.DataListView.Insp_Case_Chek;

import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
 
@SuppressLint("ViewConstructor")
public class Insp_Case_Chek_View extends LinearLayout  {
	private TextView itv_chk, itv_reg_sseq, itv_case_code, itv_case_name, itv_chek_code, itv_chek_name;
	TableLayout itl_list_insp_case_chek;
	Button ib_sub_delete;
	
	public Insp_Case_Chek_View(Context context, Insp_Case_Chek_Item aItem) {
		super(context);
 
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_insp_case_chek, this, true);
		
		// Set Text
		itl_list_insp_case_chek = (TableLayout) findViewById(R.id.tl_list_insp_case_chek);
		itv_chk        = (TextView) findViewById(R.id.tv_chk);        itv_chk.setText(aItem.getData(0));         //0:선택유무
		itv_reg_sseq   = (TextView) findViewById(R.id.tv_reg_sseq);   itv_reg_sseq.setText(aItem.getData(1));    //1:REG_SSEQ
		itv_case_code  = (TextView) findViewById(R.id.tv_case_code);  itv_case_code.setText(aItem.getData(2));   //2:점검코드
		itv_case_name  = (TextView) findViewById(R.id.tv_case_name);  itv_case_name.setText(aItem.getData(3));   //3:점검명
		itv_chek_code  = (TextView) findViewById(R.id.tv_chek_code);  itv_chek_code.setText(aItem.getData(4));   //4:조치코드
		itv_chek_name  = (TextView) findViewById(R.id.tv_chek_name);  itv_chek_name.setText(aItem.getData(5));   //5:조치명
//		ib_sub_delete = (Button) findViewById(R.id.b_sub_delete);
		
//		final Context lcontext = context;
//		ib_sub_delete.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				ill_list_insp_case_chek.setVisibility(GONE);
//				ill_list_insp_case_chek.setVisibility(visibility)
//				
//				//Toast.makeText(lcontext, "abc:" + itv_chek_name.getText(), Toast.LENGTH_SHORT).show();
//				Log.i("insp_case_chek_view", "" + itv_chek_name.getText());
//            //Log.i("TAG", "abc:" + pos + "/" + getCount() +  "-" + mItems.get(pos).getData(4));
//            //mItems.remove(pos);
//            //itemView.notifyDataSetChanged()
//			}
//		});
		
	}
 
	public void setText(int index, String data) {
		if (index == 0) {
			itv_chk.setText(data);
			setColor_chk(data);
		} else if (index == 1) {
			itv_reg_sseq.setText(data);
		} else if (index == 2) {
			itv_case_code.setText(data);
		} else if (index == 3) {
			itv_case_name.setText(data);
		} else if (index == 4) {
			itv_chek_code.setText(data);
		} else if (index == 5) {
			itv_chek_name.setText(data);
		} else {
			throw new IllegalArgumentException();
		}
	}
    
	public void setColor_chk(String data) {
		if (data.equals("1")) {  //1.선택
			itl_list_insp_case_chek.setBackgroundColor(getResources().getColor(R.color.chk_true));
			itv_case_name.setTextColor(getResources().getColor(R.color.chk_true_text));
			itv_chek_name.setTextColor(getResources().getColor(R.color.chk_true_text));
			
		} else {  //0.미선택
			itl_list_insp_case_chek.setBackgroundColor(getResources().getColor(R.color.chk_false));
			itv_case_name.setTextColor(getResources().getColor(R.color.chk_false_text));
			itv_chek_name.setTextColor(getResources().getColor(R.color.chk_false_text));
			
		}
		
	}
	
}