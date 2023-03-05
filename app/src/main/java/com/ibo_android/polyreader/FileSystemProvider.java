package com.ibo_android.polyreader;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FileSystemProvider {

    public WeakReference<Activity> _act;
    FileNodeComposite MfilesComp = null;

    public FileSystemProvider(Activity act)
    {
        _act = new WeakReference<Activity>(act);
    }


    public FileNodeComposite getdocfilesFromContentProvider()
    {
        if (MfilesComp!=null)
            return MfilesComp;
        //String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
      /*  Cursor cursor = _act.get().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Audio.Media.TITLE + " ASC");*/

        // String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        // String pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        // String[] selectionArgsPdf = new String[]{pdf};



        String pdfExt = "_data LIKE '%%.pdf'";
        //Uri ducumentsUri = MediaStore.Files.getContentUri("external");
        //String[] docsProjection ={MediaStore.Files.FileColumns.DATA,MediaStore.Images.Media.SIZE,MediaStore.Files.FileColumns.MIME_TYPE,};
//MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)  MediaStore.Files.getContentUri("external")
		/*Cursor cursor = _act.get().getContentResolver().query(
				MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
				null,
				pdfExt,
				null,
				null);*/
//MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
	/*	Cursor cursor = _act.get().getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				docsProjection,
				selectionMimeType,
				selectionArgsPdf,
				null);*/


        Cursor cursor = _act.get().getContentResolver().query(
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                null,
                pdfExt,
                null,
                null);

        ArrayList<String> files;
        files = new ArrayList<String>();


        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));

            //String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

            int dataColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            String filePath=cursor.getString(dataColumn);

            files.add(filePath);

            // Whatever else you need
        }


        if (files.isEmpty())
        {

            //show message
        }
        else
        {
            for (String fl : files ) {

                AddFile(fl);

            }//fl

        }//if (files.isEmpty())

        if (MfilesComp == null)
        {}

        return MfilesComp;
    }

    public void AddFile(String fl)
    {
        String[] parts = fl.split("/");

        for (String part : parts ) {

        }

        FileNode prevcomp = null;

        for (int i = 0; i < parts.length; i++)
        {

            // if ("".equals(parts[i]))
            //    continue;

            if (i== parts.length - 1)
            {
                FileNodeLeaf leaf = new FileNodeLeaf(parts[i],fl);
                prevcomp.Add(leaf);
                // leaf.parent = prevcomp;
            }
            else
            {

                if (MfilesComp == null)
                {
                    MfilesComp = new FileNodeComposite( parts[i]);
                    prevcomp = MfilesComp;
                }
                else
                {
                    FileNode fnode = GetComposite(parts[i],MfilesComp);

                    if (fnode == null)
                    {
                        FileNodeComposite comp = new FileNodeComposite(parts[i]);
                        // comp.parent = prevcomp;

                        prevcomp.Add(comp);
                        prevcomp = comp;
                    }
                    else
                    {
                        prevcomp = fnode;

                    }// if (fnode == null)

                }// if (MfilesComp == null)


            }// if (i== parts.length - 1)

        }//i

    }//AddFile


    public FileNode GetComposite(String part,FileNode fnode)
    {
        if (part.equals(fnode.ID()))
        {
            return fnode;
        }
        else
        {
            if (fnode.GetChildren() != null)
            {
                for (FileNode fn : fnode.GetChildren() )
                {
                    FileNode fnchild = GetComposite(part,fn);
                    if(fnchild != null)
                        return fnchild;

                }//fnode
            }// if (fnode.GetChildren() != null)

        }

        return null;
    }

    public FileNode GetCompositeByFileName(String part,FileNode fnode)
    {
        if (part.toLowerCase().equals(fnode.FullPath().toLowerCase()))
        {
            return fnode;
        }
        else
        {
            if(fnode.IsDirectory())
            {
                for (FileNode fn : fnode.GetChildren() )
                {
                    FileNode fnchild = GetCompositeByFileName(part,fn);
                    if(fnchild != null)
                        return fnchild;

                }//fnode

            }//  if(fnode.IsDirectory())

        }

        return null;
    }

}
