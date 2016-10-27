package fi.hut.soberit.agilefant.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.GenericBusiness;
import fi.hut.soberit.agilefant.business.GitBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.db.BacklogDAO;
import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.db.TaskDAO;
import fi.hut.soberit.agilefant.db.history.TaskHistoryDAO;
import fi.hut.soberit.agilefant.model.AgilefantRevisionEntity;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.transfer.AgilefantHistoryEntry;
import fi.hut.soberit.agilefant.transfer.BacklogInfoTO;

@Service("gitBusiness")
@Transactional
public class GitBusinessImpl extends GenericBusinessImpl<Object> implements GitBusiness{

	public GitBusinessImpl() {
		super(Object.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	TaskBusiness taskBusiness;
	
	@Autowired
	TaskHistoryDAO taskHistoryDAO;
	
	public Object getReport(List<Integer> taskIDs) {
		
		Map<Integer, HashMap<String, String>> taskInfo = new HashMap<Integer, HashMap<String, String>>(); 
		
		for (Integer taskid : taskIDs) {
			Task task = taskBusiness.getTaskById(taskid);
			if (null == task) continue;
			
			Story story = task.getStory();
			Backlog blog = story.getBacklog();
			
			HashMap<String, String> info = new HashMap<String, String>();
			info.put("TASK_ID", Integer.toString(task.getId()));
			info.put("TASK_NAME", task.getName());
			info.put("TASK_STATE", task.getState().getName());
			info.put("STORY_ID", Integer.toString(story.getId()));
			info.put("STORY_NAME", story.getName());
			info.put("STORY_POINTS", Integer.toString(story.getStoryPoints()));
			info.put("STORY_STATE", story.getState().getName());
			info.put("BACKLOG_TYPE", blog instanceof Product ? "PRODUCT" : blog instanceof Project ? "PROJECT" : "ITERATION");
			info.put("BACKLOG_ID", Integer.toString(blog.getId()));
			info.put("BACKLOG_NAME", blog.getName());
			
			taskInfo.put(taskid, info);
		}
		
		return taskInfo;
	}
	
	public List<AgilefantHistoryEntry> dumpAllTaskRevisions() {
		
		return taskHistoryDAO.dumpAllTaskRevisions();
	}
	
}
