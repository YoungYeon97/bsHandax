package woodnsoft.bsHandax.db;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class CreateTable extends Activity {
	
	private static final String TAG = "CreateTable";

	public static boolean createLA_SETP(SQLiteDatabase DB) {  //환경설정
		try {
			DB.execSQL(
    				"CREATE TABLE IF NOT EXISTS [LA_SETP] (" +
    						"  [SETP_DATE] VARCHAR2(8) NOT NULL, " +  //설치일자(의미없음)
    						"  [MOBI_SERI] VARCHAR2(100), " +      //시리얼번호
    						"  [APPR_FLAG] VARCHAR2(1), " +        //승인유무
    						"  [USER_ID] VARCHAR2(20), " +         //아이디(최종접속아이디)
    						"  [USER_PWD] VARCHAR2(20), " +        //패스워드(최종접속패스워드)
    						"  [LOGIN_SAVE] VARCHAR2(1), " +       //로그인저장유무
    						"  [LOGIN_AUTO] VARCHAR2(1), " +       //자동로그인유무
    						"  [LAST_GET_DATE] VARCHAR2(17), " +   //최종수신일
    						"  [MAKE_CODE] VARCHAR2(3), " +        //제조사코드
    						"  [MAKE_NAME] VARCHAR2(50), " +       //제조사명
    						"  [FACT_CODE] VARCHAR2(3), " +        //공장코드
    						"  [FACT_NAME] VARCHAR2(50), " +       //공장명
    						"  [FACT_SUB_CODE] VARCHAR2(3), " +    //세부공장코드
    						"  [FACT_SUB_NAME] VARCHAR2(50), " +   //세부공장명
    						"  [INSP_REG_NO] VARCHAR2(12), " +     //점검코드(LB_INSP)
    						"  [EMP_CODE] VARCHAR2(5), " +         //작업자코드
    						"  [EMP_NAME] VARCHAR2(50), " +        //작업자명
    						"  CONSTRAINT [] PRIMARY KEY ([SETP_DATE]))"
    			);
    	} catch (SQLiteException ex) {
      	 Log.e(TAG, "AZ_MODI_DATE");
      	 return false;
       }
   	return true;
	}
    
	public static boolean createAZ_MODI_DATE(SQLiteDatabase DB) {  //최종수정일
		try {
			DB.execSQL(
    				"CREATE TABLE IF NOT EXISTS [AZ_MODI_DATE] (" +
    						"  [TABLE_CODE] VARCHAR2(20) NOT NULL, " +
    						"  [MODI_DATE] VARCHAR2(17), " +
    						"  [MODI_DATE_M] VARCHAR2(17), " +
    						"  [VER_M] VARCHAR2(20), " +
    						"  [USE_FLAG] VARCHAR2(1), " +
    						"  CONSTRAINT [] PRIMARY KEY ([TABLE_CODE]))"
    			);
    		
    	} catch (SQLiteException ex) {
      	 Log.e(TAG, "AZ_MODI_DATE");
      	 return false;
       }
   	return true;
	}
    
	public static boolean createLA_MAKE(SQLiteDatabase DB) {  //제조사코드
		try {
    		DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_MAKE](" +
    		   "  [MAKE_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [MAKE_NAME] VARCHAR2(50), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([MAKE_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_MAKE");
			return false;
		}
		return true;
	}
    
	public static boolean createLA_FACT(SQLiteDatabase DB) {  //공장코드
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_FACT](" +
    		   "  [FACT_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [FACT_NAME] VARCHAR2(50), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([FACT_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_FACT");
			return false;
		}
   	return true;
	}
    
	public static boolean createLA_MAKE_FACT(SQLiteDatabase DB) {  //제조사공장코드
		try {
    		DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_MAKE_FACT](" +
    		   "  [MAKE_FACT_SEQ] VARCHAR2(4) NOT NULL, " +
    		   "  [MAKE_CODE] VARCHAR2(3), " +
    		   "  [FACT_CODE] VARCHAR2(3), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([MAKE_FACT_SEQ]))"
    		   );
    		
    		DB.execSQL("CREATE INDEX IF NOT EXISTS [IDK1_LA_MAKE_FACT] ON [LA_MAKE_FACT] ([MAKE_CODE], [FACT_CODE])");
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_MAKE");
			return false;
		}
		return true;
	}
    
	public static boolean createLA_FACT_SUB(SQLiteDatabase DB) {  //세부공장코드
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_FACT_SUB](" +
    		   "  [FACT_SUB_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [FACT_SUB_NAME] VARCHAR2(50), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([FACT_SUB_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_FACT_SUB");
			return false;
		}
   	return true;
	}
    
	public static boolean createLA_FACT_DETAIL(SQLiteDatabase DB) {  //세부공장
		try {
    		DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_FACT_DETAIL](" +
    		   "  [FACT_DETL_SEQ] VARCHAR2(4) NOT NULL, " +
    		   "  [MAKE_CODE] VARCHAR2(3), " +
    		   "  [FACT_CODE] VARCHAR2(3), " +
    		   "  [FACT_SUB_CODE] VARCHAR2(3), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([FACT_DETL_SEQ]))"
    		   );
    		
    		DB.execSQL("CREATE INDEX IF NOT EXISTS [IDK1_LA_FACT_DETAIL] ON [LA_FACT_DETAIL] ([MAKE_CODE], [FACT_CODE], [FACT_SUB_CODE])");
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_FACT_DETAIL");
			return false;
		}
		return true;
	}
    
	public static boolean createLA_LINE(SQLiteDatabase DB) {  //작업라인코드
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_LINE](" +
    		   "  [LINE_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [LINE_NAME] VARCHAR2(50), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([LINE_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_LINE");
			return false;
		}
   	return true;
	}
    
	public static boolean createLA_FACT_LINE(SQLiteDatabase DB) {  //작업라인
		try {
    		DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_FACT_LINE](" +
    		   "  [FACT_LINE_SEQ] VARCHAR2(4) NOT NULL, " +
    		   "  [MAKE_CODE] VARCHAR2(3), " +
    		   "  [FACT_CODE] VARCHAR2(3), " +
    		   "  [FACT_SUB_CODE] VARCHAR2(3), " +
    		   "  [LINE_CODE] VARCHAR2(3), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([FACT_LINE_SEQ]))"
    		   );
    		
    		DB.execSQL("CREATE INDEX IF NOT EXISTS [IDK1_LA_FACT_LINE] ON [LA_FACT_LINE] ([MAKE_CODE], [FACT_CODE], [FACT_SUB_CODE], [LINE_CODE])");
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_FACT_DETAIL");
			return false;
		}
		return true;
	}
    
	public static boolean createLA_CHEK(SQLiteDatabase DB) {  //조치사항
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_CHEK](" +
    		   "  [CHEK_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [CHEK_NAME] VARCHAR2(50), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([CHEK_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_CHEK");
			return false;
		}
   	return true;
	}
    
	public static boolean createLA_CASE(SQLiteDatabase DB) {  //점검항목
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_CASE](" +
    		   "  [CASE_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [CASE_NAME] VARCHAR2(50), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([CASE_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_CASE");
			return false;
		}
   	return true;
	}
    
	public static boolean createLA_CASE_GRUP(SQLiteDatabase DB) {  //품목분류별점검항목
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_CASE_GRUP](" +
    		   "  [ITEM_LA_CODE] VARCHAR2(4) NOT NULL, " +
    		   "  [ITEM_MA_CODE] VARCHAR2(4) NOT NULL, " +
    		   "  [CASE_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([ITEM_LA_CODE], [ITEM_MA_CODE], [CASE_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_CASE_GRUP");
			return false;
		}
   	return true;
	}
    
	public static boolean createLA_MAKER(SQLiteDatabase DB) {  //메이커코드
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_MAKER](" +
    		   "  [MAKER_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [MAKER_NAME] VARCHAR2(50), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([MAKER_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_MAKER");
			return false;
		}
   	return true;
	}

	public static boolean createLA_MAKER_ITEM_SPEC(SQLiteDatabase DB) {  //메이커별품목규격관리
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_MAKER_ITEM_SPEC](" +
    		   "  [MAKER_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [ITEM_SPEC] VARCHAR2(50), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([MAKER_CODE], [ITEM_SPEC]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_MAKER_ITEM_SPEC");
			return false;
		}
   	return true;
	}
	
	public static boolean createLA_BAR_LA(SQLiteDatabase DB) {  //제품구분관리
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LA_BAR_LA](" +
    		   "  [BAR_LA_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [BAR_LA_NAME] VARCHAR2(50), " +
    		   "  [SORT_KEY] VARCHAR2(4), " +
    		   "  [USE_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([BAR_LA_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LA_BAR_LA");
			return false;
		}
   	return true;
	}
    
	public static boolean createBC_ITEM_LA_H(SQLiteDatabase DB) {  //품목대분류
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [BC_ITEM_LA_H](" +
    		   "  [ITEM_LA_CODE] VARCHAR2(4) NOT NULL, " +
    		   "  [ITEM_LA_NAME] VARCHAR2(50), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([ITEM_LA_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "BC_ITEM_LA_H");
			return false;
		}
		return true;
	}
    
	public static boolean createBC_ITEM_MA_H(SQLiteDatabase DB) {  //품목중분류
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [BC_ITEM_MA_H](" +
    		   "  [ITEM_LA_CODE] VARCHAR2(4) NOT NULL, " +
    		   "  [ITEM_MA_CODE] VARCHAR2(4) NOT NULL, " +
    		   "  [ITEM_MA_NAME] VARCHAR2(50), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([ITEM_LA_CODE], [ITEM_MA_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "BC_ITEM_MA_H");
			return false;
		}
		return true;
	}
    
	public static boolean createBC_ITEM_INFO_H(SQLiteDatabase DB) {  //품목*
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [BC_ITEM_INFO_H](" +
    		   "  [ITEM_LA_CODE] VARCHAR2(4), " +
    		   "  [ITEM_MA_CODE] VARCHAR2(4), " +
    		   "  [ITEM_CODE] VARCHAR2(8) NOT NULL, " +
    		   "  [ITEM_NAME] VARCHAR2(50), " +
    		   "  [ITEM_SPEC] VARCHAR2(50), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([ITEM_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "BC_ITEM_INFO_H");
			return false;
		}
		return true;
	}
	
	public static boolean createBE_DEPT_EMP(SQLiteDatabase DB) {  //담당자*
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [BE_DEPT_EMP](" +
 				"[DEPT_CODE] VARCHAR2(5), " +
 				"[DEPT_NAME] VARCHAR2(20), " +
 				"[EMP_CODE] VARCHAR2(5) NOT NULL, " +
 				"[EMP_NAME] VARCHAR2(20), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([EMP_CODE]))"
 				);
		} catch (SQLiteException ex) {
			Log.e(TAG, "BE_DEPT_EMP");
			return false;
		}
		return true;
	}


	//------------>>>>>>>>>>>[L]BS관리 [B]점검내역관리---------->>>>>>>>>>>>>
	public static boolean createLB_INSP(SQLiteDatabase DB) {  //점검내역
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LB_INSP](" +
    		   "  [REG_NO] VARCHAR2(12) NOT NULL, " +
    		   "  [REG_YYYY] VARCHAR2(4) NOT NULL, " +
    		   "  [MAKE_FACT_SEQ] VARCHAR2(4) NOT NULL, " +
    		   "  [MAKE_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [FACT_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [STRT_DATE] VARCHAR2(8), " +
    		   "  [CLOS_DATE] VARCHAR2(8), " +
    		   "  [EMP_TEXT] VARCHAR2(2000), " +
    		   "  [QTY] NUMBER(16, 4), " +
    		   "  [ING_FLAG] VARCHAR2(1), " +
    		   "  [BIGO] VARCHAR2(2000), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([REG_NO]))"
    		   );
			
    		DB.execSQL("CREATE INDEX IF NOT EXISTS [IDK1_LB_INSP] ON [LB_INSP] ([MAKE_CODE], [FACT_CODE])");
		} catch (SQLiteException ex) {
			Log.e(TAG, "LB_INSP");
			return false;
		}
		return true;
	}
    
	public static boolean createLB_INSP_EMP(SQLiteDatabase DB) {  //점검내역작업자
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LB_INSP_EMP](" +
    		   "  [REG_NO] VARCHAR2(12) NOT NULL, " +
    		   "  [EMP_CODE] VARCHAR2(5) NOT NULL, " +
    		   "  CONSTRAINT [] PRIMARY KEY ([REG_NO], [EMP_CODE]))"
    		   );
		} catch (SQLiteException ex) {
			Log.e(TAG, "LB_INSP_EMP");
			return false;
		}
		return true;
	}
    
	public static boolean createLB_INSP_MASTER(SQLiteDatabase DB) {  //점검내역마스터
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LB_INSP_MASTER](" +
    		   "  [REG_NO] VARCHAR2(12) NOT NULL, " +
    		   "  [LK_INSP_NO] VARCHAR2(12) NOT NULL, " +
    		   "  [REG_DATE] VARCHAR2(8) NOT NULL, " +
    		   "  [REG_CODE] VARCHAR2(4) NOT NULL, " +
    		   "  [MAKE_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [FACT_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [FACT_SUB_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [FACT_DETL_SEQ] VARCHAR2(4), " +
    		   "  [EMP_CODE] VARCHAR2(5), " +
    		   "  [QTY] NUMBER(16, 4), " +
    		   "  [BIGO] VARCHAR2(100), " +
    		   "  [MOBI_FLAG] VARCHAR2(1), " +
    		   "  [PUT_FLAG] VARCHAR2(1), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([REG_NO]))"
    		   );
			
    		DB.execSQL("CREATE INDEX IF NOT EXISTS [IDK1_LB_INSP_MASTER] ON [LB_INSP_MASTER] ([MAKE_CODE], [FACT_CODE], [FACT_SUB_CODE])");
		} catch (SQLiteException ex) {
			Log.e(TAG, "LB_INSP_MASTER");
			return false;
		}
		return true;
	}
    
	public static boolean createLB_INSP_DETAIL(SQLiteDatabase DB) {  //점검내역디테일
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LB_INSP_DETAIL](" +
    		   "  [REG_NO] VARCHAR2(12) NOT NULL, " +
    		   "  [REG_SEQ] VARCHAR2(4) NOT NULL, " +
    		   "  [REG_SKEY] VARCHAR2(4), " +
    		   "  [REG_DATE] VARCHAR2(8), " +
    		   "  [REG_CODE] VARCHAR2(4), " +
    		   "  [MAKE_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [FACT_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [FACT_SUB_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [LINE_CODE] VARCHAR2(3) NOT NULL, " +
    		   "  [FACT_LINE_SEQ] VARCHAR2(4) NOT NULL, " +
    		   "  [EMP_CODE] VARCHAR2(5), " +
    		   "  [OP_NO] VARCHAR2(50), " +
    		   "  [MACN_CODE] VARCHAR2(3), " +
    		   "  [MACN_NAME] VARCHAR2(50), " +
    		   "  [MACN_MODEL] VARCHAR2(50), " +
    		   "  [MACN_MAKE_CODE] VARCHAR2(3), " +
    		   "  [MACN_MAKE_NAME] VARCHAR2(50), " +
    		   "  [MAKER_CODE] VARCHAR2(3), " +
    		   "  [BAR_CODE] VARCHAR2(50), " +
    		   "  [ITEM_CODE] VARCHAR2(20), " +
    		   "  [ITEM_SPEC] VARCHAR2(50), " +
    		   "  [JATA_FLAG] VARCHAR2(1), " +
    		   "  [BAR_LA_CODE] VARCHAR2(4), " +
    		   "  [MATCH_FLAG] VARCHAR2(1), " +
    		   "  [MAKE_DATE] VARCHAR2(8), " +
    		   "  [STATE_FLAG] VARCHAR2(1), " +
    		   "  [CHEK_FLAG] VARCHAR2(1), " +
    		   "  [CASE_TEXT] VARCHAR2(500), " +
    		   "  [CHEK_TEXT] VARCHAR2(500), " +
    		   "  [QTY] NUMBER(16, 4), " +
    		   "  [BIGO] VARCHAR2(2000), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([REG_NO], [REG_SEQ]))"
    		   );
			
    		DB.execSQL("CREATE INDEX IF NOT EXISTS [IDK1_LB_INSP_DETAIL] ON [LB_INSP_DETAIL] ([MAKE_CODE], [FACT_CODE], [FACT_SUB_CODE], [LINE_CODE])");
		} catch (SQLiteException ex) {
			Log.e(TAG, "LB_INSP_DETAIL");
			return false;
		}
		return true;
	}
    
	public static boolean createLB_INSP_CASE_CHEK(SQLiteDatabase DB) {  //점검내역별점검항목
		try {
			DB.execSQL(
    			"CREATE TABLE IF NOT EXISTS [LB_INSP_CASE_CHEK](" +
    		   "  [REG_NO] VARCHAR2(12) NOT NULL, " +
    		   "  [REG_SEQ] VARCHAR2(4) NOT NULL, " +
    		   "  [REG_SSEQ] VARCHAR2(4) NOT NULL, " +
    		   "  [REG_SSKEY] VARCHAR2(4), " +
    		   "  [REG_DATE] VARCHAR2(8), " +
    		   "  [REG_CODE] VARCHAR2(4), " +
    		   "  [CASE_CODE] VARCHAR2(3), " +
    		   "  [CHEK_CODE] VARCHAR2(3), " +
    		   "  [BIGO] VARCHAR2(2000), " +
    		   "  CONSTRAINT [] PRIMARY KEY ([REG_NO], [REG_SEQ], [REG_SSEQ]))"
    		   );
			
    		DB.execSQL("CREATE INDEX IF NOT EXISTS [IDK1_LB_INSP_CASE_CHEK] ON [LB_INSP_CASE_CHEK] ([REG_DATE], [CASE_CODE], [CHEK_CODE])");
		} catch (SQLiteException ex) {
			Log.e(TAG, "LB_INSP_CASE_CHEK");
			return false;
		}
		return true;
	}
    

}
