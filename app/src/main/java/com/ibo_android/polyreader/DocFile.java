package com.ibo_android.polyreader;

import java.io.File;
import java.util.ArrayList;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class DocFile  implements Parcelable, Comparable<DocFile>
{

	String filepath;
	String code;  
	String title;
	Integer pagenumber = 1;
	float page_scale =  1;
	Integer cpage_height = 600;
	public byte bIsDirectory = 0;


	public DocFile ( String pCode, String ptitle, String pfpath, Integer ppagenumber, float ppagescale , Integer pcpage_height )
	{	 
		code = pCode; 
		filepath = pfpath;
		title = ptitle;
		pagenumber = ppagenumber;
		page_scale = ppagescale;
		cpage_height = pcpage_height;
	}
	
	public DocFile ( String pCode, String ptitle, String pfpath  )
	{	 
		code = pCode; 
		filepath = pfpath;
		title = ptitle;
		 
	}
	
	public DocFile (Parcel in) {
        readFromParcel(in);
    }
	
	  public static final Parcelable.Creator<DocFile> CREATOR
		      = new Parcelable.Creator<DocFile>() {
		  public DocFile createFromParcel(Parcel in) {
		      return new DocFile(in);
		  }
		
		  public DocFile[] newArray(int size) {
		      return new DocFile[size];
		  }
		};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dst, int flags) {
		 dst.writeString(code);
		 dst.writeString(title);
		 dst.writeString(filepath);
		 dst.writeInt(pagenumber);
		 dst.writeFloat(page_scale);
		 dst.writeInt(cpage_height);
		 dst.writeByte(bIsDirectory);
		
	}
	
	 private void readFromParcel(Parcel in) {
		  code = in.readString();
		  title = in.readString();
		  filepath = in.readString();
		  pagenumber = in.readInt();
		  page_scale= in.readFloat();
		  cpage_height = in.readInt();
		  bIsDirectory = in.readByte();
 }


	//@Override
	/*public int compareTo(DocFile another) {
		File f_this = new File(this.filepath);
		File f_another = new File(another.filepath);
		
		if (f_this.isDirectory() && !f_another.isDirectory())
			return -1;
				
		if (!f_this.isDirectory() && f_another.isDirectory())
			return 1;
			
		return this.filepath.compareTo(another.filepath);
	}*/

	public int compareTo(DocFile another) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
		{
			if ((this.bIsDirectory == 1) && !(another.bIsDirectory == 1) )
				return -1;

			if (!(this.bIsDirectory == 1) && (another.bIsDirectory == 1))
				return 1;

		}
		else
		{
			File f_this = new File(this.filepath);
			File f_another = new File(another.filepath);

			if (f_this.isDirectory() && !f_another.isDirectory())
				return -1;

			if (!f_this.isDirectory() && f_another.isDirectory())
				return 1;

		}



		/*File f_this = new File(this.filepath);
		File f_another = new File(another.filepath);

		if (isDirectory(f_this) && !isDirectory(f_another))
			return -1;

		if (!isDirectory(f_this) && isDirectory(f_another))
			return 1;*/

		return this.filepath.compareTo(another.filepath);

	}
	/* public static boolean Contains(ArrayList<DocFile> al, DocFile mf)
		{		
			for (DocFile dfile : al ) {
				 
				if(dfile.compareTo(dfile) == 0)
				{
					return true;				
				}
				
			}	
			
			return false;
		}
		
		public static DocFile Search(ArrayList<DocFile> al, DocFile mf)
		{		
			for (DocFile dfile : al ) {
				 
				if(dfile.compareTo(mf) == 0)
				{
					return dfile;				
				}
				
			}	
			
			return null;
		}*/
	 
	 
	 
}
