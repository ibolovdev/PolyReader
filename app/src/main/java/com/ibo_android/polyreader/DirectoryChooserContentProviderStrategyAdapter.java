
package com.ibo_android.polyreader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import com.ibo_android.polyreader.R;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class DirectoryChooserContentProviderStrategyAdapter extends DirectoryChooserAdapter {


    private FileSystemProvider _FileSystemProvider = null;

    public DirectoryChooserContentProviderStrategyAdapter(DirectoryChooserActivity act, String rootdir )
    {

        minfl = LayoutInflater.from(act);
        dfiles = new ArrayList<DocFile>();
        _rootdir=rootdir;
        _act = new WeakReference<DirectoryChooserActivity>(act);
        //_con=con;
        prefs = PreferenceManager.getDefaultSharedPreferences(act);
        _FileSystemProvider = new FileSystemProvider(act);
        getDirectories(_rootdir);


    }//FilesAdapter


    public void setRootDirectory(String rootDirectory) {
        _rootdir = rootDirectory;
        if(_act.get() != null)
            _act.get().SetRootDirectoryTextView(_rootdir);
    }



    public void getDirectories(String rootfolder) {


        if (MfilesComp == null)
        {
            // getdocfilesFromContentProvider();
            MfilesComp = _FileSystemProvider.getdocfilesFromContentProvider();
            if(MfilesComp==null)
                return;
        }

       // FileNode compnode = GetCompositeByFileName(rootfolder,MfilesComp);
        FileNode compnode = _FileSystemProvider.GetCompositeByFileName(rootfolder,MfilesComp);

        if (compnode == null)
        {
            compnode = MfilesComp;
        }

        if (MfilesComp == null)
        {
            return;
        }

        if (compnode.IsDirectory())//first file is the root directory
        {
            setRootDirectory(compnode.FullPath());
        }

        if (compnode.IsDirectory()) {
            if (compnode.GetChildren() != null) {
                for (FileNode fn : compnode.GetChildren() )
                {
                    DocFile mf = new DocFile("", fn.ID(), fn.FullPath());

                    if(fn.IsDirectory())
                    {
                        mf.bIsDirectory = 1;
                        dfiles.add(mf);
                    }


                }//fn

            }//if (f.listFiles() != null)


        } else {

            DocFile mf = new DocFile("", compnode.ID(), compnode.FullPath());
            if(compnode.IsDirectory())
            {
                mf.bIsDirectory = 1;
                dfiles.add(mf);
            }

        }

        Collections.sort(dfiles);



    }//getmusicfiles



    public void getdocfilesFromContentProvider()
    {

        String pdfExt = "_data LIKE '%.pdf'";

        //   String[] docsProjection ={MediaStore.Files.FileColumns.DATA,MediaStore.Images.Media.SIZE,MediaStore.Files.FileColumns.MIME_TYPE,};



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


    }


    FileNodeComposite MfilesComp = null;
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


    public void ChooseDirectory(View v)
    {
        DirectoryViewHolder vh = (DirectoryViewHolder)  v.getTag();
        //String dir = vh.DirectoryName.filepath;
        //RadioButton cb = vh.mRadio;

        if (  vh.mRadio.isChecked())
            selectedDirectory = vh.DirectoryName.filepath;
        this.notifyDataSetChanged();

    }//ChooseDirectory


    public boolean ChangeDirectory(View v)
    {
        DirectoryViewHolder vh = (DirectoryViewHolder)  v.getTag();

        // File fc = new File(vh.mfile.filepath);
        FileNode compnode = GetCompositeByFileName(vh.DirectoryName.filepath,MfilesComp);

        if (compnode.IsDirectory()) {
            dfiles.clear();
            getDirectories(compnode.FullPath());
            this.notifyDataSetChanged();
        }

        return true;

    }//OpenFolder

    public boolean ChangeDirectory1(View v)
    {

        DirectoryViewHolder vh = (DirectoryViewHolder)  v.getTag();
        DocFile df = vh.DirectoryName;

        if (df.title == "UP")
        {
            File fc = new File(df.filepath);
            String root =   fc.getParent();
            if ( root!= null )
            {
                //File froot = new File(root);
                dfiles.clear();
                getDirectories(root);
                this.notifyDataSetChanged();

            }
            else
            {
                return false;
            }

            return true;
        }
        else
        {
            dfiles.clear();
            getDirectories(df.filepath);
            this.notifyDataSetChanged();
            return true;
        }

    }//ChangeDirectory




    public boolean GoUp()
    {
        if(MfilesComp==null)
        {
            return false;
        }


        if (!_rootdir.equals("/")) {

            FileNode compnode = GetCompositeByFileName(_rootdir,MfilesComp);

            String root =  compnode.parent.FullPath();

            dfiles.clear();
            getDirectories(root);
            this.notifyDataSetChanged();

            return true;
        }

        return false;

    }


    public boolean GoUp1()
    {
        //if (mfiles.size() == 0)
        //	return false;

        //MusicFile mf = mfiles.get(0);

//					if (mf.title == "UP")
//					{
//						 File fc = new File(mf.filepath);
//						String root =   fc.getParent();
//						if (!root.equals(null))
//						{
//							 File froot = new File(root);
//							 mfiles.clear();
//							 getmusicfiles(root);
//							this.notifyDataSetChanged();
//						}
//						else
//						{
//							 return false;
//						}
//
//						return true;
//					}


        if (_rootdir != "")
        {
            File fc = new File(_rootdir);
            String root =   fc.getParent();
            if (root != null)
            {
                // File froot = new File(root);
                dfiles.clear();
                getDirectories(root);
                this.notifyDataSetChanged();
            }
            else
            {
                return false;
            }

            return true;
        }

        return false;

    }

}

