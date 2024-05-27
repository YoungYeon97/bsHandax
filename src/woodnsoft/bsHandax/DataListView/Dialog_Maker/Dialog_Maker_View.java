package woodnsoft.bsHandax.DataListView.Dialog_Maker;

import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
 
@SuppressLint("ViewConstructor")
public class Dialog_Maker_View extends LinearLayout  {
	private TextView itv_maker_name;
	
	public Dialog_Maker_View(Context context, Dialog_Maker_Item aItem) {
		super(context);
 
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_dialog_maker, this, true);
		
		// Set Text
		itv_maker_name        = (TextView) findViewById(R.id.tv_maker_name);        itv_maker_name.setText(aItem.getData(0));        //00:메이커코드
		                                                                            itv_maker_name.setText(aItem.getData(1));        //01:메이커명
	}
 
	public void setText(int index, String data) {
		if (index == 0) {
			itv_maker_name.setTag(data);
		} else if (index == 1 ) {
			itv_maker_name.setText(data);
		} else {
			Log.i("View", index + "/" + data);
			throw new IllegalArgumentException();
		}
	}

    
 
}