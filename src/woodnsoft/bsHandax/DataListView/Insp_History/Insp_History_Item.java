package woodnsoft.bsHandax.DataListView.Insp_History;

public class Insp_History_Item  {

    private String[] mData;
    private boolean mSelectable = true;
    public Insp_History_Item(String[] obj) {
        mData = obj;
    }
 
    public Insp_History_Item(String obj00, String obj01, String obj02, String obj03) {
   	 
   	 mData = new String[4];
   	 mData[0] = obj00;
   	 mData[1] = obj01;
   	 mData[2] = obj02;
   	 mData[3] = obj03;
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
 
    public int compareTo(Insp_History_Item other) {
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