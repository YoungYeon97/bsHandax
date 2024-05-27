package woodnsoft.bsHandax.DataListView.Dialog_Jata;

import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
 
@SuppressLint("ViewConstructor")
public class Dialog_Jata_View extends LinearLayout  {
	private TextView itv_jata_flag_name;
	
	public Dialog_Jata_View(Context context, Dialog_Jata_Item aItem) {
		super(context);
 
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_dialog_jata, this, true);
		
		// Set Text
		itv_jata_flag_name        = (TextView) findViewById(R.id.tv_jata_flag_name);        itv_jata_flag_name.setText(aItem.getData(0));        //00:자타구분코드
		                                                                                    itv_jata_flag_name.setText(aItem.getData(1));        //01:자타구분명
	}
 
	public void setText(int index, String data) {
		if (index == 0) {
			itv_jata_flag_name.setTag(data);
		} else if (index == 1 ) {
			itv_jata_flag_name.setText(data);
		} else {
			Log.i("View", index + "/" + data);
			throw new IllegalArgumentException();
		}
	}

    
 
}