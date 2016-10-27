package fi.hut.soberit.agilefant.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.ProjectPlanBusiness;
import fi.hut.soberit.agilefant.model.ProjectPlan;
import fi.hut.soberit.agilefant.model.Story;


@Component("projectplanAction")
@Scope("prototype")
public class ProjectplanAction extends ActionSupport implements CRUDAction{

	private int projectPlanId = 1;
	
	@Autowired
	ProjectPlanBusiness projectPlanBusiness;
	
	@Override
	public String create() {
		// TODO Auto-generated method stub
		
		return Action.SUCCESS;
	}

	@Override
	public String delete() {
		// TODO Auto-generated method stub
		return Action.SUCCESS;
	}

	@Override
	public String store() {
		// TODO Auto-generated method stub
		return Action.SUCCESS;
	}

	@Override
	public String retrieve() {
		// TODO Auto-generated method stub
		return Action.SUCCESS;
	}

	public List<Story> GetProjectplanStories()
	{
		ProjectPlan projectPlan = projectPlanBusiness.retrieve(projectPlanId);
		return projectPlanBusiness.getProjectplanStories(projectPlan);
	}

}
