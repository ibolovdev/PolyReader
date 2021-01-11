
function showPDF(inurl, pagenumbertoshow, zoom_scale)
{
 		  url = inurl;
 		  pageNum = pagenumbertoshow;
 		  scale = zoom_scale;
 		  
 		   /**
   * Asynchronously downloads PDF.
   */
  PDFJS.getDocument(url).then(function (pdfDoc_) {
    pdfDoc = pdfDoc_;
    document.getElementById('page_count').textContent = pdfDoc.numPages;
     document.getElementById("pagenumber").max = pdfDoc.numPages ;
    //number page maximum

    // Initial/first page rendering
    renderPage(pageNum);
  });
 		  
 		  
 		  
}

  //
  // If absolute URL from the remote server is provided, configure the CORS
  // header on that server.
  //
  
 
  var url ;


  //
  // Disable workers to avoid yet another cross-origin issue (workers need
  // the URL of the script to be loaded, and dynamically loading a cross-origin
  // script does not work).
  //
  // PDFJS.disableWorker = true;

  //
  // In cases when the pdf.worker.js is located at the different folder than the
  // pdf.js's one, or the pdf.js is executed via eval(), the workerSrc property
  // shall be specified.
  //
  // PDFJS.workerSrc = '../../build/pdf.worker.js';

 PDFJS.workerSrc = 'file:///android_asset/pdfviewer/pdf.worker.js';
  var pdfDoc = null,
      pageNum = 1,
      pageRendering = false,
      pageNumPending = null,
      scale = 1,
      canvas = document.getElementById('the-canvas'),
      ctx = canvas.getContext('2d');

  /**
   * Get page info from document, resize canvas accordingly, and render page.
   * @param num Page number.
   */
  function renderPage(num) {
    pageRendering = true;
    // Using promise to fetch the page
    pdfDoc.getPage(num).then(function(page) {
      var viewport = page.getViewport(scale);
      canvas.height = viewport.height;
      canvas.width = viewport.width;

      // Render PDF page into canvas context
      var renderContext = {
        canvasContext: ctx,
        viewport: viewport
      };
      var renderTask = page.render(renderContext);

      // Wait for rendering to finish
      renderTask.promise.then(function () {
        pageRendering = false;
        if (pageNumPending !== null) {
          // New page rendering is pending
          renderPage(pageNumPending);
          pageNumPending = null;
        }
      });
    });

    // Update page counters
    document.getElementById('page_num').textContent = pageNum;
    PolyReaderApp.PageNumberChanged(pageNum,scale);
    
    
  }

  /**
   * If another page rendering in progress, waits until the rendering is
   * finised. Otherwise, executes rendering immediately.
   */
  function queueRenderPage(num) {
    if (pageRendering) {
      pageNumPending = num;
    } else {
      renderPage(num);
    }
  }

  /**
   * Displays previous page.
   */
  function onPrevPage() {
    if (pageNum <= 1) {
      return;
    }
    pageNum--;
    queueRenderPage(pageNum);
  }
  document.getElementById('prev').addEventListener('click', onPrevPage);
  
  
  var page_step = 10;
  
  function onBackwardPage() {
    if (pageNum <= 1) {
      return;
    }
    
    
    if (pageNum - page_step < 1) 
    {
        pageNum = 1;
    	queueRenderPage(pageNum);
    }
    else
    {
     	pageNum = pageNum - page_step;
    	queueRenderPage(pageNum);
    }
    
    
   
  }
  document.getElementById('backward').addEventListener('click', onBackwardPage);
  
  
  
  function onForwardPage() {
     if (pageNum == pdfDoc.numPages) {
          return;
      }
      
      if (pageNum + page_step > pdfDoc.numPages) {
           pageNum = pdfDoc.numPages;
    	   queueRenderPage(pageNum);
      }
      else
      {
       		pageNum = pageNum + page_step;
    		queueRenderPage(pageNum);
      }
      
   
  }
  document.getElementById('forward').addEventListener('click', onForwardPage);
  
  

  /**
   * Displays next page.
   */
  function onNextPage() {
    if (pageNum >= pdfDoc.numPages) {
      return;
    }
    pageNum++;
    queueRenderPage(pageNum);
  }
  document.getElementById('next').addEventListener('click', onNextPage);
  
  
   function onEndPage() {
      if (pageNum == pdfDoc.numPages) {
          return;
      }

      pageNum = pdfDoc.numPages
      queueRenderPage(pdfDoc.numPages);
  }
  document.getElementById('end').addEventListener('click', onEndPage);


  function onStartPage() {
      if (pageNum <= 1) {
          return;
      }
      pageNum = 1;
      queueRenderPage(1);
  }
  document.getElementById('start').addEventListener('click', onStartPage);


  function onZoomInPage() {

      scale = scale  + scale*0.25;
      queueRenderPage(pageNum);
  }
  document.getElementById('zoom_in').addEventListener('click', onZoomInPage);
  
  
   function onZoomOutPage() {

		if (scale == 1)
		{
			return;
		}

      scale = scale - scale*0.25;
      queueRenderPage(pageNum);
  }
  document.getElementById('zoom_out').addEventListener('click', onZoomOutPage);
  
  
   function onActualSize() { 

      scale = 1;
      queueRenderPage(pageNum);
  }
  document.getElementById('actual_size').addEventListener('click', onActualSize);
  
  
   function onPageNumberEntered() {
      
      pageNum = document.getElementById("pagenumber").value;
      
      queueRenderPage(pageNum);
  }
   document.getElementById('pagenumber').addEventListener('input', onPageNumberEntered);
   
    function onMark() 
    {
 		PolyReaderApp.MarkPage();	
  	}
  document.getElementById('mark').addEventListener('click', onMark);
  
  
    function onHideShowButtons() 
    {
    
    
 		// var mydiv = document.getElementById('buttons');
    //mydiv.style.display = 'none';
 		 
 		 
 		  var mymark = document.getElementById('mark');
 		  var hide_show_buttons_img = document.getElementById('hide_show_buttons_img');
 		  
    	//mymark.style.display = "none";
    	
    	//mymark.style.visibility="visible"
    	
    	if (mymark.style.visibility=="visible")
    	{
			mymark.style.visibility="hidden";  
			hide_show_buttons_img.src="file:///android_asset/pdfviewer/images/show_32_trans.png"; 
			
			 document.getElementById('start').style.visibility="hidden";
			  document.getElementById('backward').style.visibility="hidden";
			   document.getElementById('prev').style.visibility="hidden";
			    document.getElementById('next').style.visibility="hidden";	
			     document.getElementById('forward').style.visibility="hidden";
			      document.getElementById('end').style.visibility="hidden";
			       document.getElementById('zoom_in').style.visibility="hidden";
			        document.getElementById('zoom_out').style.visibility="hidden";
			         document.getElementById('actual_size').style.visibility="hidden";
			          // document.getElementById('page_num').style.visibility="hidden";
			           //  document.getElementById('page_count').style.visibility="hidden";
			             document.getElementById('page_title').style.visibility="hidden";
			          
    	}
    	else
    	{
    		mymark.style.visibility="visible";
    		hide_show_buttons_img.src="file:///android_asset/pdfviewer/images/hide_32_trans.png"; 
    		
    		 document.getElementById('start').style.visibility="visible";
			  document.getElementById('backward').style.visibility="visible";
			   document.getElementById('prev').style.visibility="visible";
			    document.getElementById('next').style.visibility="visible";	
			     document.getElementById('forward').style.visibility="visible";
			      document.getElementById('end').style.visibility="visible";
			       document.getElementById('zoom_in').style.visibility="visible";
			        document.getElementById('zoom_out').style.visibility="visible";
			         document.getElementById('actual_size').style.visibility="visible"; 
			          //document.getElementById('page_num').style.visibility="visible"; 
			          // document.getElementById('page_count').style.visibility="visible"; 
			             document.getElementById('page_title').style.visibility="visible";
    	} 		
 		
  	}
  document.getElementById('hide_show_buttons').addEventListener('click', onHideShowButtons);
  
   
  
  