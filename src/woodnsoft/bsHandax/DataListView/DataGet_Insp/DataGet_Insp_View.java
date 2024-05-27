package woodnsoft.bsHandax.DataListView.DataGet_Insp;

import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
 
@SuppressLint("ViewConstructor")
public class DataGet_Insp_View extends LinearLayout  {
	private TextView mText01;
   private TextView mText02;
   private TextView mText03;
   private TextView mText04;
	
	public DataGet_Insp_View(Context context, DataGet_Insp_Item aItem) {
		super(context);
 
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_dataget_insp, this, true);
		
		// Set Text
		mText01 = (TextView) findViewById(R.id.dataItem01);
      mText01.setText(aItem.getData(0));
      mText02 = (TextView) findViewById(R.id.dataItem02);
      mText02.setText(aItem.getData(1));
      mText03 = (TextView) findViewById(R.id.dataItem03);
      mText03.setText(aItem.getData(2));
      mText04 = (TextView) findViewById(R.id.dataItem04);
      mText04.setText(aItem.getData(3));
      setColor(aItem.getData(3));
	}
 
   public void setText(int index, String data) {
      if (index == 0) {
          mText01.setText(data);
      } else if (index == 1) {
          mText02.setText(data);
      } else if (index == 2) {
          mText03.setText(data);
      } else if (index == 3) {
          mText04.setText(data);
          setColor(data);
      } else {
          throw new IllegalArgumentException();
      }
  }
  
  public void setColor(String data) {
     if (data.equals("대기")) {
    	  mText04.setTextColor(getResources().getColor(R.color.lk0));  //mText04.setTextColor(Color.parseColor("#333333"));
     } else if (data.equals("진행")) {
   	  mText04.setTextColor(getResources().getColor(R.color.lk1));
     } else if (data.equals("실패")) {
   	  mText04.setTextColor(getResources().getColor(R.color.lk9));
      } else {  //완료
    	  mText04.setTextColor(getResources().getColor(R.color.lk2));
      }
		
	}
  

}