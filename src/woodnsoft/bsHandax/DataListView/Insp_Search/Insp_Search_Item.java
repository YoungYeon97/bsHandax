package woodnsoft.bsHandax.DataListView.Insp_Search;

public class Insp_Search_Item  {

    private String[] mData;
    private boolean mSelectable = true;
    public Insp_Search_Item(String[] obj) {
        mData = obj;
    }
 
    public Insp_Search_Item(String obj00, String obj01, String obj02, String obj03, String obj04, String obj05, String obj06, String obj07, String obj08, String obj09, String obj10) {
   	 
   	 mData = new String[11];
   	 mData[0] = obj00;
   	 mData[1] = obj01;
   	 mData[2] = obj02;
   	 mData[3] = obj03;
   	 mData[4] = obj04;
   	 mData[5] = obj05;
   	 mData[6] = obj06;
   	 mData[7] = obj07;
   	 mData[8] = obj08;
   	 mData[9] = obj09;
   	 mData[10] = obj10;
    }
     
    public boolean isSelectable() {
        return mSelectable;
    }
 
    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }
 
    public String[] getData() {
        return mData;
    }
 
    public String getData(int index) {
        if (mData == null || index >= mData.length) {
            return null;
        }
         
        return mData[index];
    }
     
    public void setData(String[] obj) {
        mData = obj;
    }
 
    public int compareTo(Insp_Search_Item other) {
        if (mData != null) {
            String[] otherData = other.getData();
            if (mData.length == otherData.length) {
                for (int i = 0; i < mData.length; i++) {
                    if (!mData[i].equals(otherData[i])) {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        } else {
            throw new IllegalArgumentException();
        }
         
        return 0;
    }
 
	
}