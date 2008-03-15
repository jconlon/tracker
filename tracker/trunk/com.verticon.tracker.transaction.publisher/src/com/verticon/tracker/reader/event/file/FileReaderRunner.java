/**
 * 
 */
package com.verticon.tracker.reader.event.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.resources.IFile;

import com.verticon.tracker.reader.eventadmin.ITagIdPublisher;
import com.verticon.tracker.util.TrackerLog;

/**
 * Runnable that scans a file for Long numbers and sends them
 * to a ITagIdPublisher.
 * 
 * @see FileReader
 * @author jconlon
 *
 */
public class FileReaderRunner implements Runnable {

	private final ITagIdPublisher tagIdPublisher ;
	private final File file ;
	private final Set<Long> tagNumbersToSend = new HashSet<Long>();
	private final Set<Long> tagNumbersSent = new HashSet<Long>();
	
	
	/**
	 * 
	 * Primary Constructor
	 * 
	 * @param transactionProcessor to send Long numbers to.
	 * @param file to Scan
	 */
	public FileReaderRunner(ITagIdPublisher transactionProcessor, IFile file) {
		this.tagIdPublisher=transactionProcessor;
		this.file=new File(file.getLocationURI());
		TrackerLog.logInfo("Created "+this.getClass().getSimpleName());
	}

	
	/**
	 * Scans the file and adds new tags to the Set of 
	 * tagNumbersToSend.
	 */
	public void run() {
		tagNumbersToSend.clear();
		Scanner sc = null;
		Long tag = null;
		try {
			sc = new Scanner(file);
			
			while (sc.hasNextLong()) {
				tag = sc.nextLong();
				if(!tagNumbersSent.contains(tag)){
					tagNumbersToSend.add(tag);
				}
			}

		} catch (FileNotFoundException e) {
			TrackerLog.logError("Could not find the tags file.", e);


		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		sendTags();

	}

	private void sendTags(){
		for (Long tag : tagNumbersToSend) {
			tagIdPublisher.publish(tag);
			tagNumbersSent.add(tag);
		}
		
	}
}
