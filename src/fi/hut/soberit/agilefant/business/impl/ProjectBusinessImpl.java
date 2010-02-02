package fi.hut.soberit.agilefant.business.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.business.AssignmentBusiness;
import fi.hut.soberit.agilefant.business.BacklogHistoryEntryBusiness;
import fi.hut.soberit.agilefant.business.HourEntryBusiness;
import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.business.ProductBusiness;
import fi.hut.soberit.agilefant.business.ProjectBusiness;
import fi.hut.soberit.agilefant.business.RankUnderDelegate;
import fi.hut.soberit.agilefant.business.RankingBusiness;
import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.business.StoryBusiness;
import fi.hut.soberit.agilefant.business.TransferObjectBusiness;
import fi.hut.soberit.agilefant.db.BacklogDAO;
import fi.hut.soberit.agilefant.db.ProjectDAO;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.model.Assignment;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.BacklogHistoryEntry;
import fi.hut.soberit.agilefant.model.BacklogHourEntry;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.Product;
import fi.hut.soberit.agilefant.model.Project;
import fi.hut.soberit.agilefant.model.Rankable;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.transfer.IterationTO;
import fi.hut.soberit.agilefant.transfer.ProjectMetrics;
import fi.hut.soberit.agilefant.transfer.ProjectTO;
import fi.hut.soberit.agilefant.util.HourEntryHandlingChoice;
import fi.hut.soberit.agilefant.util.TaskHandlingChoice;

@Service("projectBusiness")
@Transactional
public class ProjectBusinessImpl extends GenericBusinessImpl<Project> implements
        ProjectBusiness {

    private ProjectDAO projectDAO;
    private BacklogDAO backlogDAO;
    private ProductBusiness productBusiness;
    private AssignmentBusiness assignmentBusiness;
    
    private TransferObjectBusiness transferObjectBusiness;
    private RankingBusiness rankingBusiness;
    private SettingBusiness settingBusiness;
    @Autowired
    private StoryBusiness storyBusiness;
    @Autowired
    private HourEntryBusiness hourEntryBusiness;
    @Autowired
    private IterationBusiness iterationBusiness;
    @Autowired
    private BacklogHistoryEntryBusiness historyEntryBusiness;

    public ProjectBusinessImpl() {
        super(Project.class);
    }
    
    @Autowired
    public void setProjectDAO(ProjectDAO projectDAO) {
        this.genericDAO = projectDAO;
        this.projectDAO = projectDAO;
    }

    @Autowired
    public void setBacklogDAO(BacklogDAO backlogDAO) {
        this.backlogDAO = backlogDAO;
    }    
    
    @Autowired
    public void setRankingBusiness(RankingBusiness rankingBusiness) {
        this.rankingBusiness = rankingBusiness;
    }
    
    @Autowired
    public void setSettingBusiness(SettingBusiness settingBusiness) {
        this.settingBusiness = settingBusiness;
    }
    
    @Autowired
    public void setProductBusiness(ProductBusiness productBusiness) {
        this.productBusiness = productBusiness;
    }
    
    @Autowired
    public void setTransferObjectBusiness(TransferObjectBusiness transferObjectBusiness) {
        this.transferObjectBusiness = transferObjectBusiness;
    }
    
    @Autowired
    public void setAssignmentDAO(AssignmentBusiness assignmentBusiness) {
        this.assignmentBusiness = assignmentBusiness;
    }
    
    @Transactional
    public void unrankProject(int projectId) {
        Project project = projectDAO.get(projectId);
        project.setRank(0);
    }
    
    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public ProjectMetrics getProjectMetrics(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project must be supplied");
        }
        ProjectMetrics metrics = new ProjectMetrics();
        metrics.setStoryPoints(
                backlogDAO.calculateStoryPointSumIncludeChildBacklogs(project.getId()));
        return metrics;
    }
    
    /** {@inheritDoc} */
    public Project store(int projectId,
            Integer productId, Project project, Set<Integer> assigneeIds) throws ObjectNotFoundException,
            IllegalArgumentException {

        Project persistable = new Project();
        if (projectId > 0) {
            persistable = this.retrieve(projectId);       
        } 
        validateProjectData(project, projectId, productId);
        if(productId != null ){
            Product product = this.productBusiness.retrieve(productId);
            persistable.setParent(product);
        }
        setAssignees(persistable, assigneeIds);
        
        persistable.setName(project.getName());
        persistable.setStartDate(project.getStartDate());
        persistable.setEndDate(project.getEndDate());
        persistable.setDescription(project.getDescription());
        persistable.setStatus(project.getStatus());
        persistable.setBacklogSize(project.getBacklogSize());   
        persistable.setBaselineLoad(project.getBaselineLoad());
        Project stored = persistProject(persistable);
        
        return stored;
    }
    
    private void setAssignees(Project project, Set<Integer> assigneeIds) {
        if (assigneeIds != null) {
            for(Assignment assignment : project.getAssignments()) {
                if(!assigneeIds.contains(assignment.getId())) {
                    assignmentBusiness.delete(assignment.getId());
                }
            }
            assignmentBusiness.addMultiple(project, assigneeIds);
        }
    }
    /**
     * Persists a given project.
     * <p>
     * Decides whether to use <code>store</code> or <code>create</code>.
     * @return the persisted project
     */
    private Project persistProject(Project project) {
        if (project.getId() > 0) {
            this.store(project);
            return project;
        }
        else {
            int newId = this.create(project);
            return this.retrieve(newId);
        }
    }
    
    /**
     * Validates the given project's data.
     * <p>
     * Currently checks start and end date. 
     */
    private static void validateProjectData(Project project, int projectId, Integer productId)
        throws IllegalArgumentException {
        if (project.getStartDate().isAfter(project.getEndDate())) {
            throw new IllegalArgumentException("Project start date after end date.");
        }
        if(projectId == 0 && productId == null) {
            throw new IllegalArgumentException("New project must have a parent product");
        }
    }


    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public Collection<User> getAssignedUsers(Project project) {
        return projectDAO.getAssignedUsers(project);
    }


    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public ProjectTO getProjectData(int projectId) {
        Project original = this.retrieve(projectId);
        ProjectTO project = transferObjectBusiness.constructProjectTO(original);
        project.setChildren(new HashSet<Backlog>());
        for (Backlog backlog : original.getChildren()) {
            IterationTO iter = new IterationTO((Iteration)backlog);
            iter.setScheduleStatus(transferObjectBusiness.getBacklogScheduleStatus(backlog));
            project.getChildren().add(iter);
        }
        return project;
    }

    @Transactional
    public Project rankUnderProject(int projectId, int rankUnderId) {
        Project project = projectDAO.get(projectId);
        Project rankUnder = projectDAO.get(rankUnderId);
        rankUnderProject(project, rankUnder);
        return project;
    }
    
    /** {@inheritDoc} */
    @Transactional
    public Project rankUnderProject(final Project project, Project upperProject)
            throws IllegalArgumentException {
        if (project == null) {
            throw new IllegalArgumentException("Project should be given");
        }
        
        rankingBusiness.rankUnder(project, upperProject, new RankUnderDelegate() {
            public Collection<? extends Rankable> getWithRankBetween(Integer lower, Integer upper) {
                return projectDAO.getProjectsWithRankBetween(lower, upper);
            }
        });

        return project;
    }
    
    
    @Transactional
    public Project rankOverProject(int projectId, int rankOverId) {
        Project project = projectDAO.get(projectId);
        Project rankOver = projectDAO.get(rankOverId);        
        Project rankUnder = projectDAO.getProjectWithRankLessThan(rankOver.getRank());
        if (rankUnder == null) {
            projectDAO.increaseRankedProjectRanks();
            project.setRank(1);
        } else {
            rankUnderProject(project, rankUnder);            
        }
        return project;
    }
    
    /** {@inheritDoc} */
    @Transactional
    public void moveToRanked(int projectId) {
        LocalDate startDate = new LocalDate();
        LocalDate endDate = startDate.plus(settingBusiness
                .getPortfolioTimeSpan());
        List<Project> projects = projectDAO.getRankedProjects(startDate, endDate);
        Project project = projectDAO.get(projectId);
        
        Project maxRankedProject = projectDAO.getMaxRankedProject();
        if (maxRankedProject != null) {
            project.setRank(maxRankedProject.getRank() + 1);
        }
        if( projects.isEmpty() ) {
            if(maxRankedProject == null) {
                project.setRank(1);
            } else {
                rankUnderProject(project,maxRankedProject);
            }   
        } else {
            rankUnderProject(project,projects.get(projects.size() - 1));
        }
        
    }
    
    public void deleteDeep(int id) {

        Project project = retrieve(id);
        if (project == null)
            return;
        Set<Backlog> iterations = new HashSet<Backlog>(project.getChildren());
        
        if (iterations != null) {
            for (Backlog item : iterations) {
                iterationBusiness.deleteDeep(item.getId());
            }
        }
        
        Set<Story> stories = new HashSet<Story>(project.getStories());
        TaskHandlingChoice taskHandlingChoice = TaskHandlingChoice.DELETE;
        HourEntryHandlingChoice storyHourEntryHandlingChoice = HourEntryHandlingChoice.DELETE;
        HourEntryHandlingChoice taskHourEntryHandlingChoice = HourEntryHandlingChoice.DELETE;
       
        if (stories != null) {
            for (Story item : stories) {
                storyBusiness.delete(item.getId(), taskHandlingChoice,
                        storyHourEntryHandlingChoice, taskHourEntryHandlingChoice);
            }
        }
        Set<Assignment> assignments = new HashSet<Assignment>(project.getAssignments());
        
        if (assignments != null) {
            for (Assignment item : assignments) {
                assignmentBusiness.delete(item.getId());
            }
        }
        
        Set<BacklogHourEntry> hourEntries = new HashSet<BacklogHourEntry>(project.getHourEntries());
        
        if (hourEntries != null) {
            hourEntryBusiness.deleteAll(hourEntries);
        }
        
        
        List<BacklogHistoryEntry> historyEntries = new ArrayList<BacklogHistoryEntry>(project.getBacklogHistoryEntries());
        for (BacklogHistoryEntry item : historyEntries) {
            historyEntryBusiness.delete(item.getId());
        }
        
        delete(project);

    }

    public void setAssignmentBusiness(AssignmentBusiness assignmentBusiness) {
        this.assignmentBusiness = assignmentBusiness;
    }

    public void setStoryBusiness(StoryBusiness storyBusiness) {
        this.storyBusiness = storyBusiness;
    }

    public void setHourEntryBusiness(HourEntryBusiness hourEntryBusiness) {
        this.hourEntryBusiness = hourEntryBusiness;
    }

    public void setIterationBusiness(IterationBusiness iterationBusiness) {
        this.iterationBusiness = iterationBusiness;
    }

    public void setHistoryEntryBusiness(
            BacklogHistoryEntryBusiness historyEntryBusiness) {
        this.historyEntryBusiness = historyEntryBusiness;
    }
    
}
