package woodnsoft.bsHandax.DataListView.Dialog_bar_la;

public class Dialog_bar_la_Item  {

    private String[] mData;
    private boolean mSelectable = true;
    public Dialog_bar_la_Item(String[] obj) {
        mData = obj;
    }
 
    public Dialog_bar_la_Item(String obj00, String obj01) {
   	 
   	 mData = new String[2];
   	 mData[0] = obj00;
   	 mData[1] = obj01;
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
 
    public int compareTo(Dialog_bar_la_Item other) {
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