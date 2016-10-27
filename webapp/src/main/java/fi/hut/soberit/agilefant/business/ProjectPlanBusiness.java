package fi.hut.soberit.agilefant.business;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fi.hut.soberit.agilefant.model.ProjectPlan;
import fi.hut.soberit.agilefant.model.*;

public interface ProjectPlanBusiness extends GenericBusiness<ProjectPlan> {

public List<Story> getProjectplanStories (ProjectPlan projectplan);

}
