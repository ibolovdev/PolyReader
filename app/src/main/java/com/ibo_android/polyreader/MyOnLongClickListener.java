package com.ibo_android.polyreader;

import android.content.ClipData;
import android.os.Build;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class MyOnLongClickListener implements OnLongClickListener
{
	
	  // Defines the one method for the interface, which is called when the View is long-clicked
    @SuppressWarnings("deprecation")
	public boolean onLongClick(View v) {

    	TextView txt  = (TextView) v; 	
    	DocFile doc;
    	
    // Create a new ClipData.
    // This is done in two steps to provide clarity. The convenience method
    // ClipData.newPlainText() can create a plain text ClipData in one step.

    // Create a new ClipData.Item from the ImageView object's tag
    	doc = (DocFile) v.getTag();
    
    //	ClipData.Item item = new ClipData.Item(doc.filepath);

    // Create a new ClipData using the tag as a label, the plain text MIME type, and
    // the already-created item. This will create a new ClipDescription object within the
    // ClipData, and set its MIME type entry to "text/plain"

      //  ClipData dragData = new ClipData(v.getTag(),ClipData.MIMETYPE_TEXT_PLAIN,item);

    	
    	ClipData dragData2 = ClipData.newPlainText(doc.filepath, doc.filepath);
    	
    	
    // Instantiates the drag shadow builder.
    View.DragShadowBuilder myShadow = new MyDragShadowBuilder(txt);

    // Starts the drag

           
            
            
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                
                v.startDragAndDrop(dragData2,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        null,      // no need to use local data
                        0          // flags (not currently used, set to 0)
                					);
                
            } 
            else
            {
            	 v.startDrag(dragData2,  // the data to be dragged
                         myShadow,  // the drag shadow builder
                         null,      // no need to use local data
                         0          // flags (not currently used, set to 0)
             );
            }
            
            
         return true   ;

    }
	

}
