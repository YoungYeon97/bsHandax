package woodnsoft.bsHandax.DataListView.Insp_Case_Chek;

public class Insp_Case_Chek_Item  {

    private String[] mData;
    private boolean mSelectable = true;
    public Insp_Case_Chek_Item(String[] obj) {
        mData = obj;
    }
 
    public Insp_Case_Chek_Item(String obj00, String obj01, String obj02, String obj03, String obj04, String obj05) {
   	 
   	 mData = new String[6];
   	 mData[0] = obj00;
   	 mData[1] = obj01;
   	 mData[2] = obj02;
   	 mData[3] = obj03;
   	 mData[4] = obj04;
   	 mData[5] = obj05;
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
 
    public int compareTo(Insp_Case_Chek_Item other) {
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