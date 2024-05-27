package woodnsoft.bsHandax.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import woodnsoft.bsHandax2.R;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class Util {
/**
    *<pre>
    * Null���� Null String ("") ���·� ��ȯ �մϴ�.
    *</pre>
    * @param data the String
    * @return java.lang.String
    */
    public static String fixNull(String data){
        if (data == null)
            return "";
        else
            return data;
    }
    /**
    *<pre> 
    * Null���� Null String val�� ���� �մϴ�.
    *</pre>
    * @param data the String
    * @param val the String
    * @return java.lang.String
    */
	public static String f_nvl(String data, String val){
		if (data == null || (data!=null && data.equals("")) )
			return val;
		else
			return data;
   }
   
	public static boolean f_isnull(String data){  //Null Chk
		if (data == null || data.equals("") )
			return true;
		else
			return false;
	}
   
	public static void f_dialog(Context context,String msg) {
		//Log.d(context.getString(R.string.LOG_TITLE),"2");	
        if ( !Util.fixNull(msg).equals("") ) {
            //Context con = context.getApplicationContext();
            //Context context = getApplicationContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(R.string.MSG_TITLE);
            builder.setMessage(msg);
            builder.setIcon(R.drawable.title_icon);
            builder.setCancelable(false);

            // yes
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                   public void onClick(DialogInterface dialog, int id){
                       dialog.dismiss();
                   }
            });
    /*
            // no
            alert.setNegativeButton("No", new DialogInterface.OnClickListener(){
                   public void onClick(DialogInterface dialog, int id){
                         dialog.cancel();
                   }
            });
    */
            final AlertDialog alert = builder.create();
            alert.show();
 
        }
     }
	
	public static String makeMD5(String str) {
	    MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    md.update(str.getBytes());
	    byte[] md5Code = md.digest();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < md5Code.length; i++) {
	        String md5Char = String.format("%02x", 0xff&(char)md5Code[i]);
	        sb.append(md5Char);
	    }
	    return sb.toString();
	}
	public static String getXMLValue(Node nd,String itemName) {
	    Element el = (Element) nd;
	    Element  fel = (Element) el.getElementsByTagName(itemName).item(0);
	             fel = (Element) fel.getElementsByTagName("value").item(0);
	    NodeList fnl = fel.getChildNodes();
	    Node n = (Node)fnl.item(0);
	    return n.getNodeValue();
	}
	
	public static String getFormatDate(int y,int m, int d) {
		String DATE_FORMAT = "yyyy-MM-dd";
		java.text.SimpleDateFormat sdf = 
		      new java.text.SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); 
		c1.set(y, m , d);
		return sdf.format(c1.getTime());
	}

	public static String getFormatDate(String as_date) {
		if (as_date == null || as_date.equals("")) return "";
		if (as_date.equals("null")) return "";
		return String.format("%s-%s-%s", as_date.substring(0, 4), as_date.substring(4, 6), as_date.substring(6, 8));
	}
	
	public static String getFormatTime(String as_date) {
		if (as_date == null || as_date.equals("")) return "";
		if (as_date.equals("null")) return "";
		return String.format("%s:%s", as_date.substring(0, 2), as_date.substring(2, 4));
	}
	
	public static String f_replace(String as_data, String ls_bef, String ls_aft) {
		if (as_data == null || as_data.equals("") ) return "";
		return as_data.replace(ls_bef, ls_aft);
	}
	
	public static String f_replace_sql(String as_data) {
		if (as_data == null || as_data.equals("") ) return "";
		return as_data.replace("'", "''").replace("\"", "\\\"").replace("\n", "'||chr(13)||chr(10)||'");
	}
	
	public static String getAddDate(int y,int m, int d,int addDays) {
		String DATE_FORMAT = "yyyy-MM-dd";
		java.text.SimpleDateFormat sdf = 
		      new java.text.SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); 
		c1.set(y, m , d); // 1999 jan 20
		c1.add(Calendar.DATE,addDays);
		return sdf.format(c1.getTime());
	}

	public static String unescape(String inp) {
		   String rtnStr = new String();
		   char [] arrInp = inp.toCharArray();
		   int i;
		   for(i=0;i<arrInp.length;i++) {
		       if(arrInp[i] == '%') {
		           String hex;
		           if(arrInp[i+1] == 'u') {    //유니코드.
		               hex = inp.substring(i+2, i+6);
		                i += 5;
		            } else {    //ascii
		                hex = inp.substring(i+1, i+3);
		                i += 2;
		            }
		            try{
		                rtnStr += new String(Character.toChars(Integer.parseInt(hex, 16)));
		            } catch(NumberFormatException e) {
		                rtnStr += "%";
		                i -= (hex.length()>2 ? 5 : 2);
		            }
		        } else {
		            rtnStr += arrInp[i];
		        }
		    }
		 
		    return rtnStr;
		}

}