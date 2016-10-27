package fi.hut.soberit.agilefant.db;

import java.util.List;

import fi.hut.soberit.agilefant.model.ProjectPlan;
import fi.hut.soberit.agilefant.model.Story;

public interface ProjectPlanDAO extends GenericDAO<ProjectPlan> {

	List<Story> GetProjectPlanStories(ProjectPlan projectPlan);

}
