package woodnsoft.bsHandax.common;

import java.io.File;

import woodnsoft.bsHandax.db.DBH;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DBActivity extends BaseActivity implements DatabaseAdapterEvent {
	private static final String TAG = "DBActivity"; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Log.d(TAG,"1");
		super.onCreate(savedInstanceState);

    	File fsdcard = Environment.getExternalStorageDirectory();  //SD CARD�� �������
    	File fdbpath = new File(fsdcard.getAbsolutePath() + File.separator + "WoodnSoft" + File.separator + "bsHandax");   
    	if (!fdbpath.exists()){
    		Log.i(TAG,"Create Db directory : " + fdbpath.getAbsolutePath());
    		
    		if(fdbpath.mkdirs()){
    			Log.i(TAG, "OK");
    		}else{
    			Toast.makeText(getBaseContext(), "���� ���� ����!!!", Toast.LENGTH_SHORT).show();
    			Log.e(TAG, "NO");
    		}
    	}
         
    	DBH.dbPath = fdbpath.getAbsolutePath();              //DB ��ġ ���
    	DBH.dbFile = "bsHandax2.db";                         //DB ���ϸ�
    	DBH.dbPathFile = DBH.dbPath + File.separator + DBH.dbFile;  //DB ��ġ��� + DB ���ϸ�

    	DBH.DB = openOrCreateDatabase(DBH.dbPathFile, MODE_WORLD_WRITEABLE, null);    	
    	
    	if(DBH.DB == null) DBH.openDatabase();
    	
	}

}