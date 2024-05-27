package woodnsoft.bsHandax.DataListView.Dialog_item_spec;

import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
 
@SuppressLint("ViewConstructor")
public class Dialog_item_spec_View extends LinearLayout  {
	private TextView itv_maker_code, itv_item_spec;
	
	public Dialog_item_spec_View(Context context, Dialog_item_spec_Item aItem) {
		super(context);
 
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_dialog_item_spec, this, true);
		
		// Set Text
		itv_maker_code      = (TextView) findViewById(R.id.tv_maker_code);        itv_maker_code.setText(aItem.getData(0));       //00:메이커코드
		itv_item_spec       = (TextView) findViewById(R.id.tv_item_spec);         itv_item_spec.setText(aItem.getData(1));        //01:규격
	}
 
	public void setText(int index, String data) {
		if (index == 0) {
			itv_maker_code.setTag(data);
		} else if (index == 1 ) {
			itv_item_spec.setText(data);
		} else {
			Log.i("View", index + "/" + data);
			throw new IllegalArgumentException();
		}
	}

    
 
}