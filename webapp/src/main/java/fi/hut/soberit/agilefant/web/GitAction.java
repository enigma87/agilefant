package fi.hut.soberit.agilefant.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.GitBusiness;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.transfer.AgilefantHistoryEntry;

@Component("gitAction")
@Scope("singleton")
public class GitAction extends ActionSupport {

	ByteArrayInputStream excelStream;
	
	public ByteArrayInputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(ByteArrayInputStream excelStream) {
		this.excelStream = excelStream;
	}


	Object exportObject;
	List<Integer> taskIDs;
	
	
	
    public Object getExportObject() {
		return exportObject;
	}

	public void setExportObject(Object exportObject) {
		this.exportObject = exportObject;
	}


	@Autowired
    private GitBusiness gitBusiness;   
   
	
	public String execute() throws NoHeadException, GitAPIException, IOException{
	   
	    try{
	    	   	FileRepositoryBuilder builder = new FileRepositoryBuilder();
			   	Repository repo = builder.setGitDir(new File("/Users/enigma/hooks"+"//.git")).setMustExist(true).build();
			   	Git git = new Git(repo);
			   	git.pull();
			
			   	HSSFWorkbook hwb=new HSSFWorkbook();
		        HSSFSheet sheet =  hwb.createSheet("commit info");

		        Iterable<RevCommit> log = git.log().call();
		        List<Integer> tasks = new ArrayList<Integer>();
		        for (Iterator<RevCommit> iterator = log.iterator(); iterator.hasNext();) {
		        	RevCommit rev = iterator.next();
		        	Pattern pattern = Pattern.compile("REF\\s+(\\d+)\\s+");
		        	Matcher matcher = pattern.matcher(rev.getFullMessage());
		        	if (matcher.find()) {
		        	
		        		Integer taskId = Integer.parseInt(matcher.group(1));
		        		tasks.add(taskId);
		        	}
		        }
			   
		        HashMap<Integer, HashMap<String, String>> dump = (HashMap<Integer, HashMap<String, String>>) gitBusiness.getReport(tasks);
		        
		        HSSFRow title = sheet.createRow(0);
		        
		        int j = 0;
		        for (String name : new String[] {"TASK_ID", "COMMIT_MSG", "COMMITTER", "COMMITTER_EMAIL", "COMMIT_TIME", "COMMITTER_TIME_ZONE","TASK_NAME", "TASK_STATE", "STORY_ID", "STORY_NAME", "STORY_POINTS", "STORY_STATE", "BACKLOG_TYPE", "BACKLOG_ID", "BACKLOG_NAME"}) {
		        
		        	title.createCell(j).setCellValue(name);
		        	j++;
	    		}
		        
        		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
        		
        		log = git.log().call();
		        int i = 1;
		        for (Iterator<RevCommit> iterator = log.iterator(); iterator.hasNext();) {
		        	RevCommit rev = iterator.next();
		        	HSSFRow row = sheet.createRow(i);
		        	Pattern pattern = Pattern.compile("REF\\s+(\\d+)\\s+");
		        	Matcher matcher = pattern.matcher(rev.getFullMessage());
		        	if (matcher.find()) {
		        		Integer taskId = Integer.parseInt(matcher.group(1));
		        		
		        				        		
		        		row.createCell(0).setCellValue(taskId);
		        		row.createCell(1).setCellValue(rev.getFullMessage());
		        		row.createCell(2).setCellValue(rev.getCommitterIdent().getName());
		        		row.createCell(3).setCellValue(rev.getCommitterIdent().getEmailAddress());
		        		row.createCell(4).setCellValue(sdf.format(rev.getCommitterIdent().getWhen().getTime()));
		        		row.createCell(5).setCellValue(rev.getCommitterIdent().getTimeZone().getDisplayName());
		        		
		        		HashMap<String, String> info = new HashMap<String, String>();
		        		info = dump.get(taskId);
		        		if (null == info) continue;
		        		
		        		int k = 6;
		        		for (String key : new String[] {"TASK_NAME", "TASK_STATE", "STORY_ID", "STORY_NAME", "STORY_POINTS", "STORY_STATE", "BACKLOG_TYPE", "BACKLOG_ID", "BACKLOG_NAME"}) {
		        			if ("STORY_POINTS".equals(key)) {
		        				row.createCell(k, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(Integer.parseInt(info.get(key)));;
		        			} else {
		        				row.createCell(k).setCellValue(info.get(key).toString());
		        			}
		        			k++;
		        		}
		        		
		        	}
		        	i++;
		        }
			   
	        ///////////////////////////////////////////////////////////////////////////////

		    List<AgilefantHistoryEntry> taskHistoryDump = gitBusiness.dumpAllTaskRevisions();    
		     
		    HSSFSheet sheet_1 =  hwb.createSheet("task history");
		    
		    i = 1;
		    int k = 0;
		    String [] cols = new String[] {"USERNAME", "TASK_ID", "TIME_STAMP", "TASK_STATE", "TASK_EFFORT_REMAIN", "TASK_ESTIMATE", "REVISION_TYPE"};
		    HSSFRow header = sheet_1.createRow(0);
		    
		    for (String key : cols) {
		    	header.createCell(k).setCellValue(key);
		    	k++;
		    }
		    
		    for (AgilefantHistoryEntry  hist : taskHistoryDump) {
		    	HSSFRow row = sheet_1.createRow(i);
		    	Task task = (Task) hist.getObject();
		    	System.err.println(hist.getObjectId());
		    	row.createCell(0).setCellValue(hist.getRevision().getUserName());
		    	if (-1 != hist.getObjectId()) row.createCell(1,HSSFCell.CELL_TYPE_NUMERIC).setCellValue(hist.getObjectId());
		    	row.createCell(2).setCellValue(new Date(hist.getRevision().getTimestamp()).toString());
		    	if (null != task.getState()) row.createCell(3).setCellValue(task.getState().toString());
		    	if (null != task.getEffortLeft()) row.createCell(4).setCellValue(task.getEffortLeft().toString());
		    	if (null != task.getOriginalEstimate()) row.createCell(5).setCellValue(task.getOriginalEstimate().toString());
		    	if (null != hist.getRevisionType()) row.createCell(6).setCellValue(hist.getRevisionType().toString());
		    		    	
		    	i++;
		    }
		    
	        ///////////////////////////////////////////////////////////////////////////////
	        //////Now you are ready with the HSSFworkbook object to be sent to client//////
	        ///////////////////////////////////////////////////////////////////////////////

	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        hwb.write(baos);
	        excelStream = new ByteArrayInputStream(baos.toByteArray());

	        ///////////////////////////////////////////////////////////////////////////////
	        ////Here HSSFWorkbook object is sent directly to client w/o saving on server///
	        ///////////////////////////////////////////////////////////////////////////////
	    
	        git.close();
	    }catch(Exception e){
	        System.out.println(e.getMessage());
	    }
	   	    
	    return SUCCESS;
	}
	
}
