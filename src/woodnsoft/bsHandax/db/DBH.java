package woodnsoft.bsHandax.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBH extends SQLiteOpenHelper {
	
	private static final String TAG = "DBH";
	public static final int VER = 0; 
	public static SQLiteDatabase DB = null;
	public static String dbPath, dbFile, dbPathFile;  //DB 설치경로, DB 파일명, DB 설치경로 + DB 파일명 : dbCreate()에서 Set
	
	
   public DBH(Context context) {
   	super(context, dbPathFile, null, VER);
   }
    
   public static void openDatabase() {
        
       try {
           DB = SQLiteDatabase.openDatabase(dbPathFile, null, SQLiteDatabase.OPEN_READWRITE);
       } catch (SQLiteException ex) {
      	 Log.i(TAG,"openDatabase : " + ex.getMessage());
           //Toast.makeText(this, ex.getMessage(), 1).show();
       }
       
   }

   public static void closeDatabase() {
       try {
           // close database
      	 DB.close();
      	 DB = null;
       } catch(Exception e) {
      	 e.printStackTrace();
      	 Log.i(TAG,"closeDatabase");
       }
   }
   
   public static Cursor selectAZ_MODI_DATE(String as_table_code) {
      String aSQL = "  SELECT TABLE_CODE, MODI_DATE, MODI_DATE_M, VER_M"
                  + "    FROM AZ_MODI_DATE"
                  + "   WHERE TABLE_CODE LIKE '" + as_table_code + "'";

      Cursor outCursor = DB.rawQuery(aSQL, null);
       
      return (outCursor);
  }
 	
   public static Cursor selectBE_DEPT_EMP(String as_emp_code) {
      String aSQL = "  SELECT DEPT_CODE, DEPT_NAME, EMP_CODE, EMP_NAME"
                  + "    FROM BE_DEPT_EMP"
                  + "   WHERE EMP_CODE LIKE '" + as_emp_code + "'";

      Cursor outCursor = DB.rawQuery(aSQL, null);
       
      return (outCursor);
  }

/*
	public static Cursor selectAZ_MODI_DATE(String strDrugCode) {  //사용안함
      String aSQL = "SELECT TABLE_CODE, MODI_DATE, VER"
                  + " FROM AZ_MODI_DATE_M"
                  + " WHERE TABLE_CODE = ?";

      String[] args = {strDrugCode};

      Cursor outCursor = DB.rawQuery(aSQL, args);
       
      return (outCursor);
  }
*/
    
   public static boolean insertMasterData(String aLine) {
       // split the input line
       String[] tokens = aLine.split("\\|");
       if (tokens != null && tokens.length > 7) {
           DB.execSQL( "insert into MASTER(DRUGCODE, DRUGNAME, PRODENNM, PRODKRNM, PHRMNAME, DISTRNAME, REPDGID, REPDGNAME) values (" +
                   "'" + tokens[0] + "'," +
                   "'" + tokens[1] + "'," +
                   "'" + tokens[2] + "'," +
                   "'" + tokens[3] + "'," +
                   "'" + tokens[4] + "'," +
                   "'" + tokens[5] + "'," +
                   "'" + tokens[6] + "'," +
                   "'" + tokens[7] + "')");
           return true;
       } else {
       }
        
       return false;
   }
    
   public static Cursor queryMasterTable(String strSearchWord ) {
       String aSQL = "select DRUGCODE, DRUGNAME, PRODKRNM, DISTRNAME "
           + " from MASTER"
           + " where DRUGNAME like ?";
        
       String[] args = {strSearchWord};

       Cursor outCursor = DB.rawQuery(aSQL, args);
        
       return (outCursor);
   }
    
   public static Cursor queryDetailsTable(String strDrugCode) {
      String aSQL = "select DRUGCODE, CLASSCODE, CLASSNAME, DETAILS "
          + " from DETAILS"
          + " where DRUGCODE = ?";

      String[] args = {strDrugCode};

      Cursor outCursor = DB.rawQuery(aSQL, args);
       
      return (outCursor);
  }
  
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	
	public static boolean fCreate(String asFlag) {
		//Cursor c;
		//String lsSql;
		//lsSql = "SELECT UPPER(tbl_name) FROM sqlite_master WHERE TYPE = 'table' AND UPPER(tbl_name) LIKE UPPER('" + asFlag + "')";
		//c = DB.rawQuery(lsSql, null);
		//if (c.getCount() > 0) {
	   //	try {
	   //		DB.execSQL("DROP TABLE IF EXISTS AZ_MODI_DATE");
	   //        
	   //   } catch (SQLiteException ex) {
	   //   	Log.e(TAG,"AZ_MODI_DATE");
	   //   }
		//	
		//}
		//c.close();


		if (asFlag.equals("ALL") || asFlag.equals("LA_SETP"))                   { if (CreateTable.createLA_SETP(DB) == false) return false;            //환경설정
		} else if (asFlag.equals("ALL") || asFlag.equals("AZ_MODI_DATE"))       { if (CreateTable.createAZ_MODI_DATE(DB) == false) return false;       //최종 수정일
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_MAKE"))            { if (CreateTable.createLA_MAKE(DB) == false) return false;            //제조사코드
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_FACT"))            { if (CreateTable.createLA_FACT(DB) == false) return false;            //공장코드
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_MAKE_FACT"))       { if (CreateTable.createLA_MAKE_FACT(DB) == false) return false;       //제조사공장
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_FACT_SUB"))        { if (CreateTable.createLA_FACT_SUB(DB) == false) return false;        //세부공장코드
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_FACT_DETAIL"))     { if (CreateTable.createLA_FACT_DETAIL(DB) == false) return false;     //세부공장
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_LINE"))            { if (CreateTable.createLA_LINE(DB) == false) return false;            //작업라인코드
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_FACT_LINE"))       { if (CreateTable.createLA_FACT_LINE(DB) == false) return false;       //작업라인
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_CHEK"))            { if (CreateTable.createLA_CHEK(DB) == false) return false;            //조치사항
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_CASE"))            { if (CreateTable.createLA_CASE(DB) == false) return false;            //점검항목
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_CASE_GRUP"))       { if (CreateTable.createLA_CASE_GRUP(DB) == false) return false;       //품목분류별점검항목
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_MAKER"))           { if (CreateTable.createLA_MAKER(DB) == false) return false;           //메이커코드
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_MAKER_ITEM_SPEC")) { if (CreateTable.createLA_MAKER_ITEM_SPEC(DB) == false) return false; //메이커별품목규격관리
		} else if (asFlag.equals("ALL") || asFlag.equals("LA_BAR_LA"))          { if (CreateTable.createLA_BAR_LA(DB) == false) return false;          //제품구분
		} else if (asFlag.equals("ALL") || asFlag.equals("BC_ITEM_LA_H"))       { if (CreateTable.createBC_ITEM_LA_H(DB) == false) return false;       //품목대분류
		} else if (asFlag.equals("ALL") || asFlag.equals("BC_ITEM_MA_H"))       { if (CreateTable.createBC_ITEM_MA_H(DB) == false) return false;       //품목중분류
		} else if (asFlag.equals("ALL") || asFlag.equals("BC_ITEM_INFO_H"))     { if (CreateTable.createBC_ITEM_INFO_H(DB) == false) return false;     //품목
		} else if (asFlag.equals("ALL") || asFlag.equals("BE_DEPT_EMP"))        { if (CreateTable.createBE_DEPT_EMP(DB) == false) return false;        //담당자

		} else if (asFlag.equals("ALL") || asFlag.equals("LB_INSP"))            { if (CreateTable.createLB_INSP(DB) == false) return false;            //점검내역
		} else if (asFlag.equals("ALL") || asFlag.equals("LB_INSP_EMP"))        { if (CreateTable.createLB_INSP_EMP(DB) == false) return false;        //점검내역작업자
		} else if (asFlag.equals("ALL") || asFlag.equals("LB_INSP_MASTER"))     { if (CreateTable.createLB_INSP_MASTER(DB) == false) return false;     //점검내역마스터
		} else if (asFlag.equals("ALL") || asFlag.equals("LB_INSP_DETAIL"))     { if (CreateTable.createLB_INSP_DETAIL(DB) == false) return false;     //점검내역디테일
		} else if (asFlag.equals("ALL") || asFlag.equals("LB_INSP_CASE_CHEK"))  { if (CreateTable.createLB_INSP_CASE_CHEK(DB) == false) return false;  //점검내역별점검항목
		
		} else {
			Log.e(TAG,"Create Table(" + asFlag + ")문 점검오류");
			return false;
		}
		
		return true;
			
	}
	
}
