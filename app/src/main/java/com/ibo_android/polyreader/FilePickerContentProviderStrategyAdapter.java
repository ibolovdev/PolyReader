


package com.ibo_android.polyreader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;



import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import android.widget.TextView;



public class FilePickerContentProviderStrategyAdapter  extends FilePickerAdapter {



    public String getRootDirectory() {
        return _RootDirectory;
    }
    private FileSystemProvider _FileSystemProvider = null;

    public void setRootDirectory(String rootDirectory) {
        _RootDirectory = rootDirectory;
        if(_act.get() != null)
            _act.get().SetRootDirectoryTextView(_RootDirectory);
    }


    public FilePickerContentProviderStrategyAdapter( String rootdir, FilePickerActivity act, ArrayList<Parcelable> selfiles_asparam)
    {

        minfl = LayoutInflater.from(act);
        dfiles = new ArrayList<DocFile>();
        selectedfiles = new ArrayList<DocFile>();

        if (!(selfiles_asparam == null ))
        {
            for (Object omf: selfiles_asparam.toArray())
            {
                DocFile typedmf =  (DocFile) omf;
                selectedfiles.add(typedmf);
            }
        }

        _rootdir=rootdir;

        _act = new WeakReference<FilePickerActivity>(act);
        _FileSystemProvider = new FileSystemProvider(act);
        getdocfiles(_rootdir);

        //Collections.sort(dfiles);

        prefs = PreferenceManager.getDefaultSharedPreferences(act);

    }//FilesAdapter



    public void getdocfiles(String rootfolder)
    {

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
                    }
                    dfiles.add(mf);

                }//fn

            }//if (f.listFiles() != null)


        } else {

            DocFile mf = new DocFile("", compnode.ID(), compnode.FullPath());
            if(compnode.IsDirectory())
            {
                mf.bIsDirectory = 1;
            }
            dfiles.add(mf);
        }

        Collections.sort(dfiles);

    }//getmusicfiles


    public void getdocfilesFromContentProvider()
    {
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



        String pdfExt = "_data LIKE '%.pdf'";
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

    private boolean CheckFile(File f)
    {

        //if (!f.isDirectory())
        //{
        FileName fn = new FileName(f.getAbsolutePath());

        //	for (String format: _allowableFormats)
        //	{
        //	if (format.toUpperCase().equals(fn.extension().toUpperCase()))
        //	{
        //		return true;
        //	}
        //	}

			/*if (_allowableFormats.contains(fn.extension()))
			{
				return true;
			}*/

        //return false;
        //}

        return true;

    }//CheckFile






    private void FilePickerLongClick(View v)
    {
        String action = "FILE_PICKER_LONG_CLICK";
        Intent in = new Intent(action);


        PickFileViewHolder vh = (PickFileViewHolder)  v.getTag();
        File fc = new File(vh.dfile.filepath);

      /*  if (!fc.isDirectory())
        {
            in.putExtra("file_path", vh.dfile.filepath);

            if(_act.get() != null)
                _act.get().sendBroadcast(in);
        }*/

        if (vh.dfile.bIsDirectory != 1) {
            in.putExtra("file_path", vh.dfile.filepath);

            if (_act.get() != null)
                _act.get().sendBroadcast(in);
        }

    }


    public void OpenFolder(View v) {


        PickFileViewHolder vh = (PickFileViewHolder) v.getTag();

        // File fc = new File(vh.mfile.filepath);
        FileNode compnode = GetCompositeByFileName(vh.dfile.filepath,MfilesComp);

        if (compnode.IsDirectory()) {
            dfiles.clear();
            getdocfiles(compnode.FullPath());
            this.notifyDataSetChanged();
        }


    }//OpenFolder

    public void OpenFolder1(View v)
    {

        //open a new pick song activity
        PickFileViewHolder vh = (PickFileViewHolder)  v.getTag();
        //MusicFile mf = vh.mfile;
        //  CheckBox cb = vh.mCheck;
        //Toast.makeText(_con, "OpenFolder", Toast.LENGTH_LONG).show();

        File fc = new File(vh.dfile.filepath);

        if (fc.isDirectory())
        {
            dfiles.clear();
            getdocfiles(fc.getAbsolutePath());
            this.notifyDataSetChanged();


			/*	Intent i = new Intent(_con, SongsPickerActivity.class);
    		 	i.putExtra("root_dir", mf.filepath);
    			i.putExtra("SELECTED_SONGS", selectedfiles);
    		 	i.putExtra("OPEN_FOLDER", true);*/

            //_act.startActivityForResult(intent, requestCode)
            // _con.startActivity(i);
            //_act.startActivityForResult(i,MainActivity.GET_SONGS_FOLDER);
            //_con.s


            //String action = "STOP_MUSIC";
            //Toast.makeText(ctx, "AUDIO_BECOMING_NOISY", Toast.LENGTH_LONG).show();

            // Intent in = new Intent("PICK_SONG_INSIDE_FOLDER");
            //in.putExtra("root_dir", mf.filepath);

            // You can also include some extra data.
            //  in.putExtra("message", "This is my message!");


            //  LocalBroadcastManager.getInstance(_con).sendBroadcast(in);

            // ctx.sendBroadcast(in);
        }


    }//OpenFolder

    private void TraverseDirectoryAdd1( File fc,DocFile mf)
    {
        if (fc.isDirectory())
        {
            selectedfiles.add(mf);
            for (File name: fc.listFiles())
            {
                DocFile mf_in = new  DocFile("",name.getName(),name.getAbsolutePath());
                //selectedfiles.add(mf_in);
                TraverseDirectoryAdd1(name,mf_in);
            }

        }
        else
        {
            selectedfiles.add(mf);
        }
    }//TraverseDirectoryAdd




    public synchronized void AddToSelected( DocFile mf)
    {
        selectedfiles.add(mf);
    }

    void TraverseDirectoryAdd( DocFile mf ) {
        // FileName fn = new FileName(fc);
        FileNode compnode = GetCompositeByFileName(mf.filepath,MfilesComp);

        if (compnode.IsDirectory()) {
            AddToSelected(mf);

            if (compnode.GetChildren() != null)
            {
                for (FileNode fn  : compnode.GetChildren())
                {
                    DocFile mf_in = new DocFile("", fn.ID(), fn.FullPath());
                    TraverseDirectoryAdd( mf_in);
                }

            }  // if (compnode.GetChildren() != null)

        }
        else
        {
            AddToSelected(mf);
        }

        notifyDataSetChanged();

    }//TraverseDirectoryAdd


    private void TraverseDirectoryRemove1( File fc,DocFile mf)
    {
        if (fc.isDirectory())
        {
            selectedfiles.remove(mf);
            for (Object omf: selectedfiles.toArray())
            {
                DocFile mf_in = (DocFile) omf;

                if(mf_in.filepath.contains(fc.getPath()))
                {
                    selectedfiles.remove(mf_in);
                }

			/*	for (File name: fc.listFiles())//takes too much time
				 {
						if (mf_in.filepath.equals(name.getAbsolutePath()))
						{
							 //selectedfiles.remove(mf_in);
							TraverseDirectoryRemove(name,mf_in);
							 continue;
						}
				}*/
            }	//omf

        }
        else
        {
            selectedfiles.remove(mf);
        }


    }


    public synchronized void RemoveFromSelected( DocFile mf)
    {
        selectedfiles.remove(mf);
    }

    private void TraverseDirectoryRemove( DocFile mf) {

        FileNode compnode = GetCompositeByFileName(mf.filepath,MfilesComp);

        if (compnode.IsDirectory()) {

            RemoveFromSelected(mf);

            for (Object omf : selectedfiles.toArray()) {
                DocFile mf_in = (DocFile) omf;

                if (mf_in.filepath.contains(compnode.FullPath())) {
                    RemoveFromSelected(mf);
                }

            }    //omf

        } else {
            RemoveFromSelected(mf);
            //selectedfiles.remove(mf);
        }

    }//TraverseDirectoryRemove



    public boolean GoUp() {

        if(MfilesComp==null)
        {
            return false;
        }


        if (!_RootDirectory.equals("/")) {

            //  File fc = new File(_RootDirectory);

            FileNode compnode = GetCompositeByFileName(_RootDirectory,MfilesComp);

            String root =  compnode.parent.FullPath();

            dfiles.clear();
            getdocfiles(root);
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

//		if (mf.title == "UP")
//		{
//			 File fc = new File(mf.filepath);
//			String root =   fc.getParent();
//			if (!root.equals(null))
//			{
//				 File froot = new File(root);
//				 mfiles.clear();
//				 getmusicfiles(root);
//				this.notifyDataSetChanged();
//			}
//			else
//			{
//				 return false;
//			}
//
//			return true;
//		}


        if (_RootDirectory != "")
        {
            File fc = new File(_RootDirectory);
            String root =   fc.getParent();
            if (root != null)
            {
                // File froot = new File(root);
                dfiles.clear();
                getdocfiles(root);
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

    public boolean GoUp(View v)
    {

        PickFileViewHolder vh = (PickFileViewHolder)  v.getTag();
        DocFile mf = vh.dfile;
        //CheckBox cb = vh.mCheck;

        if (mf.title == "UP")
        {
            File fc = new File(mf.filepath);
            String root =   fc.getParent();
            if (root != null)
            {
                //File froot = new File(root);
                dfiles.clear();
                getdocfiles(root);
                this.notifyDataSetChanged();
            }
            else
            {
                return false;
            }

            return true;
        }

        return false;

    }//GoUp

    public void ChooseSongRecursive1(View v)
    {

        PickFileViewHolder vh = (PickFileViewHolder)  ((View) v.getParent()).getTag();
        //MusicFile mf = vh.mfile;
        //CheckBox cb = vh.mCheck;

        if ( vh.dCheck.isChecked())
        {
            File fc = new File(vh.dfile.filepath);
            TraverseDirectoryAdd1(fc,vh.dfile);
        }
        else
        {
            File fu = new File(vh.dfile.filepath);
            TraverseDirectoryRemove1(fu,vh.dfile);
        }


    }//ChooseSongRecursive

    public void ChooseSongRecursive(View v) {

        PickFileViewHolder vh = (PickFileViewHolder) ((View) v.getParent()).getTag();

        if (vh.dCheck.isChecked()) {
            TraverseDirectoryAdd(vh.dfile);
        } else {
            TraverseDirectoryRemove(vh.dfile);
        }

    }//ChooseSongRecursive



    public void SelectAll1()
    {
        selectedfiles.clear();

        for (Object omf: dfiles.toArray())
        {
            DocFile mf_in = (DocFile) omf;


            //int idx = mfiles.indexOf(mf_in);
				/* if (!(  fileslist.getChildAt(idx) == null))//for the visible ones
				 {
					 View v = (View)   fileslist.getChildAt(idx);
					 PickSongViewHolder vh = (PickSongViewHolder)  v.getTag();
					 vh.mCheck.setChecked(true);
				 }*/

            File fc = new File(mf_in.filepath);
            TraverseDirectoryAdd1(fc,mf_in);

        }	//omf


        this.notifyDataSetChanged();////for the visible ones, for everything
        //Notifies the attached observers that the underlying data has been changed
        //and any View reflecting the data set should refresh itself.
        //ArrayAdapter - Control whether methods that change the list (add(T), insert(T, int), remove(T), clear()) automatically call notifyDataSetChanged().
        //this.registerDataSetObserver(observer)

    } //SelectAll

    public void SelectAll()
    {
        selectedfiles.clear();

        for (Object omf: dfiles.toArray())
        {
            DocFile mf_in = (DocFile) omf;


            //int idx = mfiles.indexOf(mf_in);
				/* if (!(  fileslist.getChildAt(idx) == null))//for the visible ones
				 {
					 View v = (View)   fileslist.getChildAt(idx);
					 PickSongViewHolder vh = (PickSongViewHolder)  v.getTag();
					 vh.mCheck.setChecked(true);
				 }*/

            // File fc = new File(mf_in.filepath);
            TraverseDirectoryAdd(mf_in);

        }	//omf


        this.notifyDataSetChanged();////for the visible ones, for everything
        //Notifies the attached observers that the underlying data has been changed
        //and any View reflecting the data set should refresh itself.
        //ArrayAdapter - Control whether methods that change the list (add(T), insert(T, int), remove(T), clear()) automatically call notifyDataSetChanged().
        //this.registerDataSetObserver(observer)

    } //SelectAll

    public void UnSelectAll()
    {
        selectedfiles.clear();
        this.notifyDataSetChanged();
    }//UnSelectAll

}

