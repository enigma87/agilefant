package fi.hut.soberit.agilefant.web;

import java.util.ArrayList;
import java.util.Collection;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionSupport;

import fi.hut.soberit.agilefant.db.BacklogDAO;
import fi.hut.soberit.agilefant.db.BacklogItemDAO;
import fi.hut.soberit.agilefant.db.GenericDAO;
import fi.hut.soberit.agilefant.db.IterationGoalDAO;
import fi.hut.soberit.agilefant.db.UserDAO;
import fi.hut.soberit.agilefant.model.ActivityType;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.BacklogItem;
import fi.hut.soberit.agilefant.model.IterationGoal;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;

public class BacklogItemAction extends ActionSupport implements CRUDAction {
	

	private static final long serialVersionUID = -4289013472775815522L;
	private BacklogDAO backlogDAO;
	private BacklogItemDAO backlogItemDAO;
	private int backlogId;
	private int backlogItemId;
	private BacklogItem backlogItem;
	private Backlog backlog;
	private Collection<BacklogItem> backlogItems = new ArrayList<BacklogItem>();
	private UserDAO userDAO;
	private boolean watch = false;
	private IterationGoalDAO iterationGoalDAO;
	private int iterationGoalId;
	private int assigneeId;

	public String create() {
		backlogItemId = 0;
		backlogItem = new BacklogItem();
		backlog = backlogDAO.get(backlogId);
		//backlogId = backlog.getId();
		return Action.SUCCESS;
	}

	public String delete() {
		backlogItem = backlogItemDAO.get(backlogItemId);
		if(backlogItem == null){
			super.addActionError(super.getText("backlogItem.notFound"));
			return Action.ERROR;
		}
		if(backlogItem.getTasks().size() > 0) {
			super.addActionError(super.getText("backlogItem.notEmptyWhenDeleting"));
			return Action.ERROR;						
		}
		// backlogId = backlogItem.getId();//?? removed when testing with jUnit
		backlogItemDAO.remove(backlogItemId);
		return Action.SUCCESS;
	}

	public String edit() {
		backlogItem = backlogItemDAO.get(backlogItemId);
		if (backlogItem == null){
			super.addActionError(super.getText("backlogItem.notFound"));
			return Action.ERROR;
		}
		backlog = backlogItem.getBacklog();
		backlogId = backlog.getId();
		return Action.SUCCESS;
	}

	public String store() {
		BacklogItem storable = new BacklogItem();
		if (backlogItemId > 0){
			storable = backlogItemDAO.get(backlogItemId);
			if (storable == null){
				super.addActionError(super.getText("backlogItem.notFound"));
				return Action.ERROR;
			}
		}
		this.fillStorable(storable);
		if (super.hasActionErrors()){
			return Action.ERROR;
		}
		backlogItemDAO.store(storable);
		return Action.SUCCESS;
	}
	
	protected void fillStorable(BacklogItem storable){
		User oldAssignee = storable.getAssignee();
		User newAssignee = null;

		if ((oldAssignee == null && assigneeId > 0) || 
			 (oldAssignee != null && oldAssignee.getId() != assigneeId)) {
			if (assigneeId > 0) {
				newAssignee = userDAO.get(assigneeId);
			}
			storable.setAssignee(newAssignee);
		}
				
		if (this.backlogItem.getIterationGoal() != null){
			IterationGoal goal = iterationGoalDAO.get(this.backlogItem.getIterationGoal().getId());
			//IterationGoal goal = iterationGoalDAO.get(iterationGoalId);
			storable.setIterationGoal(goal);
		}
		if(this.backlogItem.getName().equals("")) {
			super.addActionError(super.getText("backlogitem.missingName"));
			return;
		}
		storable.setName(this.backlogItem.getName());
		storable.setDescription(this.backlogItem.getDescription());
		storable.setAllocatedEffort(this.backlogItem.getAllocatedEffort());
		storable.setPriority(this.backlogItem.getPriority());
		storable.setStatus(this.backlogItem.getStatus()); // added after failed jUnit test 
		
		backlog = backlogDAO.get(backlogId);
		if (backlog == null){
			super.addActionError(super.getText("backlog.notFound"));
		}
		if (storable.getId() > 0){
			storable.getBacklog().getBacklogItems().remove(storable);
			backlog.getBacklogItems().add(storable);
		}
		storable.setBacklog(backlog);
		
		if (watch) {
			User user = SecurityUtil.getLoggedUser();
			storable.getWatchers().put(user.getId(), user);
			user.getWatchedBacklogItems().add(storable);		
		}
		else {
			User user = SecurityUtil.getLoggedUser();
			storable.getWatchers().remove(user.getId());
		}
			
	}

	public Backlog getBacklog() {
		return backlog;
	}

	public void setBacklog(Backlog backlog) {
		this.backlog = backlog;
	}

	public int getBacklogId() {
		return backlogId;
	}

	public void setBacklogId(int backlogId) {
		this.backlogId = backlogId;
	}

	public BacklogItem getBacklogItem() {
		return backlogItem;
	}

	public void setBacklogItem(BacklogItem backlogItem) {
		this.backlogItem = backlogItem;
	}

	public int getBacklogItemId() {
		return backlogItemId;
	}

	public void setBacklogItemId(int backlogItemId) {
		this.backlogItemId = backlogItemId;
	}

	public Collection<BacklogItem> getBacklogItems() {
		return backlogItems;
	}

	public void setBacklogItems(Collection<BacklogItem> backlogItems) {
		this.backlogItems = backlogItems;
	}

	public void setBacklogDAO(BacklogDAO backlogDAO) {
		this.backlogDAO = backlogDAO;
	}

	public void setBacklogItemDAO(BacklogItemDAO backlogItemDAO) {
		this.backlogItemDAO = backlogItemDAO;
	}
	
/*	protected BacklogItemDAO getBacklogItemDAO() {
		return this.backlogItemDAO;
	}*/

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	public void setWatch(boolean watch) {
		this.watch = watch;
	}

	public void setIterationGoalDAO(IterationGoalDAO iterationGoalDAO) {
		this.iterationGoalDAO = iterationGoalDAO;
	}

	public void setAssigneeId(int assigneeId) {
		this.assigneeId = assigneeId;
	}

	/**
	 * Setter for Spring IoC
	 * 
	 * @param iterationGoalId iteration goal id to be set
	 */
	public void setIterationGoalId(int iterationGoalId){
		this.iterationGoalId= iterationGoalId;
	}
	
	/**
	 * Getter for Spring IoC
	 * 
	 * @return iteration goal id
	 */
	public int getIterationGoalId(){
		return iterationGoalId;
	}
}
