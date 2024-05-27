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

    	File fsdcard = Environment.getExternalStorageDirectory();  //SD CARD의 폴더경로
    	File fdbpath = new File(fsdcard.getAbsolutePath() + File.separator + "WoodnSoft" + File.separator + "bsHandax");   
    	if (!fdbpath.exists()){
    		Log.i(TAG,"Create Db directory : " + fdbpath.getAbsolutePath());
    		
    		if(fdbpath.mkdirs()){
    			Log.i(TAG, "OK");
    		}else{
    			Toast.makeText(getBaseContext(), "폴더 생성 실패!!!", Toast.LENGTH_SHORT).show();
    			Log.e(TAG, "NO");
    		}
    	}
         
    	DBH.dbPath = fdbpath.getAbsolutePath();              //DB 설치 경로
    	DBH.dbFile = "bsHandax2.db";                         //DB 파일명
    	DBH.dbPathFile = DBH.dbPath + File.separator + DBH.dbFile;  //DB 설치경로 + DB 파일명

    	DBH.DB = openOrCreateDatabase(DBH.dbPathFile, MODE_WORLD_WRITEABLE, null);    	
    	
    	if(DBH.DB == null) DBH.openDatabase();
    	
	}

}