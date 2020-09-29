package edu.nmsu.cs.webserver;

import java.io.BufferedInputStream;

/**
 * Web worker: an object of this class executes in its own new thread to receive and respond to a
 * single HTTP request. After the constructor the object executes on its "run" method, and leaves
 * when it is done.
 *
 * One WebWorker object is only responsible for one client connection. This code uses Java threads
 * to parallelize the handling of clients: each WebWorker runs in its own thread. This means that
 * you can essentially just think about what is happening on one client at a time, ignoring the fact
 * that the entirety of the webserver execution might be handling other clients, too.
 *
 * This WebWorker class (i.e., an object of this class) is where all the client interaction is done.
 * The "run()" method is the beginning -- think of it as the "main()" for a client interaction. It
 * does three things in a row, invoking three methods in this class: it reads the incoming HTTP
 * request; it writes out an HTTP header to begin its response, and then it writes out some HTML
 * content for the response content. HTTP requests and responses are just lines of text (in a very
 * particular format).
 * 
 * @author Jon Cook, Ph.D.
 *
 **/

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.io.File;
import java.io.FileNotFoundException; 
import java.util.Scanner;
import java.time.LocalDate;
import java.awt.Image;
import java.nio.file.Files;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class WebWorker implements Runnable
{
	private String url;
	private Socket socket;

	/**
	 * Constructor: must have a valid open socket
	 **/
	public WebWorker(Socket s)
	{
		socket = s;
	}

	/**
	 * Worker thread starting point. Each worker handles just one HTTP request and then returns, which
	 * destroys the thread. This method assumes that whoever created the worker created it with a
	 * valid open socket object.
	 **/
	public void run()
	{
		System.err.println("Handling connection...");
		try
		{
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			readHTTPRequest(is);
			
			
			writeContent(os);
			os.flush();
			socket.close();
		}
		catch (Exception e)
		{
			System.err.println("Output error: " + e);
		}
		System.err.println("Done handling connection.");
		return;
	}

	/**
	 * Read the HTTP request header.
	 **/
	private void readHTTPRequest(InputStream is)
	{
		String line;
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		while (true)
		{
			try
			{
				while (!r.ready())
					Thread.sleep(1);
				line = r.readLine();
				System.err.println("Request line: (" + line + ")");
				
				/* So I guess right here parse stuff*/
				if (line.contains("GET")) 
					url = line.substring(line.indexOf("/")+1, line.indexOf("HTTP")-1) + "\n";
					
	            
				if (line.length() == 0)
					break;
			}
			catch (Exception e)
			{
				System.err.println("Request error: " + e);
				break;
			}
		}
		return;
	}

	/**
	 * Write the HTTP header lines to the client network connection.
	 * 
	 * @param os
	 *          is the OutputStream object to write to
	 * @param contentType
	 *          is the string MIME content type (e.g. "text/html")
	 **/
	private void writeHTTPHeader(OutputStream os, String contentType) throws Exception
	{
		Date d = new Date();
		DateFormat df = DateFormat.getDateTimeInstance();
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		os.write("HTTP/1.1 200 OK\n".getBytes());
		os.write("Date: ".getBytes());
		os.write((df.format(d)).getBytes());
		os.write("\n".getBytes());
		os.write("Server: Jon's very own server\n".getBytes());
		// os.write("Last-Modified: Wed, 08 Jan 2003 23:11:55 GMT\n".getBytes());
		// os.write("Content-Length: 438\n".getBytes());
		os.write("Connection: close\n".getBytes());
		os.write("Content-Type: ".getBytes());
		os.write(contentType.getBytes());
		os.write("\n\n".getBytes()); // HTTP header ends with 2 newlines
		return;
	}

	/**
	 * Write the data content to the client network connection. This MUST be done after the HTTP
	 * header has been written out.
	 * 
	 * @param os
	 *          is the OutputStream object to write to
	 **/
	private void writeContent(OutputStream os) throws Exception
	{	
		
	/*
		os.write("<html><head></head><body>\n".getBytes());
		os.write("<h3>Zach's web server works!</h3>\n".getBytes());
		os.write("</body></html>\n".getBytes());
		*/
		
		try {
			String fileType = new String(url.substring(url.lastIndexOf(".")+1)).strip();
		//	writeHTTPHeader(os, "text/html");
			
			
			
			if (!fileType.contains("html")){
				
				File myObj = new File((System.getProperty("user.dir") + "/www/" + url).strip());
			    if (myObj.exists() == false) {
			    	writeHTTPHeader(os, "text/html");
			    	throw new FileNotFoundException("file does not exist.");
			    }
				
				writeHTTPHeader(os, "image/" + fileType);
				System.out.print(url);
				String filePath = new String((System.getProperty("user.dir") + "/www/" + url).strip());
				BufferedInputStream is = new BufferedInputStream(new FileInputStream(filePath));
				int line = is.read();
				while (line != -1) {
					os.write(line);
					line = is.read();
				
				}
				is.close();
			} 
			else {
				
				writeHTTPHeader(os, "text/" + fileType);
				
				os.write(("<link rel=\"shortcut icon\"\n" + 
						" href=\"http://localhost:8080/res/acc/favicon.png\">").getBytes());
			
		      File myObj = new File((System.getProperty("user.dir") + "/www/" + url).strip());
		      if (myObj.exists() == false) throw new FileNotFoundException("file does not exist.");
		      
		      Scanner myReader = new Scanner(myObj);
		      
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        
		        LocalDate date = LocalDate.now();
		       	data = data.replaceAll("<cs371date>", date.toString());
		       	data = data.replaceAll("<cs371server>", "Zach's Server");
		        if (data.contains("src=\"")){
		        	System.out.println((System.getProperty("user.dir") + "/" + data.indexOf("src=\"", data.indexOf("\""))).strip());
		        }
		       	
		        
		       	
		        os.write(data.getBytes());
		        
		      }
		      
		      myReader.close(); 
		      
			}
		    } 
		catch (FileNotFoundException e)
			{
		    	os.write("<html><head>\n".getBytes());
		    	os.write("<title>404 Not Found</title>\n".getBytes());
		    	os.write("</head><body>\n".getBytes());
		    	os.write("<h1>404 Not Found</h1>\n".getBytes());
		 		os.write("<p>Bad. that doesn't exist.</p>\n".getBytes()); 
		    	os.write("</body></html>".getBytes());
		    }
		
	}

} // end class