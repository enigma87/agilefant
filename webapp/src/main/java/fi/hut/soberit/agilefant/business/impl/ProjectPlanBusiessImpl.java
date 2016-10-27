package fi.hut.soberit.agilefant.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.ProjectPlanBusiness;
import fi.hut.soberit.agilefant.db.ProjectPlanDAO;
import fi.hut.soberit.agilefant.model.ProjectPlan;
import fi.hut.soberit.agilefant.model.Story;

@Service("projectPlanBusiness")
@Transactional
public class ProjectPlanBusiessImpl extends GenericBusinessImpl<ProjectPlan> implements ProjectPlanBusiness{

	public ProjectPlanBusiessImpl() {
		super(ProjectPlan.class);
	}
	
	private ProjectPlanDAO projectPlanDAO;
	
	@Autowired
	public void setProjectPlanDAO(ProjectPlanDAO projectPlanDAO) {
		this.projectPlanDAO = projectPlanDAO;
		this.genericDAO = projectPlanDAO;
	}

	

	@Override
	public List<Story> getProjectplanStories(ProjectPlan projectPlan)
	{
		if (null == projectPlan) return null;
		
		int projectId = projectPlan.getId();
		if (projectId > 0)
		{
			return projectPlanDAO.GetProjectPlanStories(projectPlan);
		}
		 return null;
	}

	
}
