package woodnsoft.bsHandax.DataListView.Dialog_bar_la;

import woodnsoft.bsHandax2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
 
@SuppressLint("ViewConstructor")
public class Dialog_bar_la_View extends LinearLayout  {
	private TextView itv_bar_la_name;
	
	public Dialog_bar_la_View(Context context, Dialog_bar_la_Item aItem) {
		super(context);
 
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_dialog_bar_la, this, true);
		
		// Set Text
		itv_bar_la_name        = (TextView) findViewById(R.id.tv_bar_la_name);        itv_bar_la_name.setText(aItem.getData(0));        //00:力前备盒
		                                                                            itv_bar_la_name.setText(aItem.getData(1));        //01:力前备盒疙
	}
 
	public void setText(int index, String data) {
		if (index == 0) {
			itv_bar_la_name.setTag(data);
		} else if (index == 1 ) {
			itv_bar_la_name.setText(data);
		} else {
			Log.i("View", index + "/" + data);
			throw new IllegalArgumentException();
		}
	}

    
 
}