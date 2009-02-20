package fi.hut.soberit.agilefant.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork.ActionSupport;

import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.business.BacklogItemBusiness;
import fi.hut.soberit.agilefant.business.BusinessThemeBusiness;
import fi.hut.soberit.agilefant.business.HourEntryBusiness;
import fi.hut.soberit.agilefant.business.SettingBusiness;
import fi.hut.soberit.agilefant.exception.ObjectNotFoundException;
import fi.hut.soberit.agilefant.model.AFTime;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.BacklogItem;
import fi.hut.soberit.agilefant.model.BacklogItemHourEntry;
import fi.hut.soberit.agilefant.model.BusinessTheme;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.util.BacklogItemResponsibleContainer;
import fi.hut.soberit.agilefant.util.EffortSumData;
import fi.hut.soberit.agilefant.util.TodoMetrics;

/**
 * Action for listing backlogs contents.
 * 
 * Action contains caches for backlog item responsibles, themes, spent effort
 * entries and todos to ensure best performance compared to domain object lazy
 * fetching strategy.
 * 
 * ALL actions that list backlog contents should be derived from this class.
 * 
 * @author Pasi Pekkanen
 * 
 */
public class BacklogContentsAction extends ActionSupport {

    protected int backlogId;

    protected Backlog backlog;

    private List<BacklogItem> backlogItems = new ArrayList<BacklogItem>();
    
    private Map<BacklogItem, List<BacklogItemResponsibleContainer>> backlogResponsibles = new HashMap<BacklogItem, List<BacklogItemResponsibleContainer>>();

    private Map<BacklogItem, List<BusinessTheme>> backlogThemes = new HashMap<BacklogItem, List<BusinessTheme>>();

    private Map<BacklogItem, TodoMetrics> backlogTodos = new HashMap<BacklogItem, TodoMetrics>();
    
    private EffortSumData origEstSum = new EffortSumData();
    
    private EffortSumData effortLeftSum = new EffortSumData();
    
    private AFTime spentEffortSum = new AFTime(0);
    
    protected BacklogBusiness backlogBusiness;
    
    protected BacklogItemBusiness backlogItemBusiness;
    
    protected BusinessThemeBusiness businessThemeBusiness;
    
    protected HourEntryBusiness hourEntryBusiness;
    
    private SettingBusiness settingBusiness;

    protected void initializeContents(int backlogId) {
        Backlog bl;
        try {
            bl = backlogBusiness.getBacklog(backlogId);
        } catch (ObjectNotFoundException e) {
            return;
        }
        initializeContents(bl);
    }

    protected void initializeContents(Backlog backlog) {
        if (backlog == null) {
            return;
        }
        
        backlogItems = backlogItemBusiness.getBacklogItemsByBacklog(backlog);
        backlogResponsibles = backlogItemBusiness.getResponsiblesByBacklog(backlog);
        backlogTodos = backlogItemBusiness.getTasksByBacklog(backlog);
        backlogThemes = businessThemeBusiness.getBacklogItemBusinessThemesByBacklog(backlog);
        
        //calculate sum data
        if(backlogItems != null) {
            effortLeftSum = backlogBusiness.getEffortLeftSum(backlogItems);
            origEstSum = backlogBusiness.getOriginalEstimateSum(backlogItems);
            if(settingBusiness.isHourReportingEnabled()) {
                spentEffortSum = backlogBusiness.getSpentEffortSum(backlogItems);
            }
        }
        
        // TODO: themes

    }
    
    protected void initializeContents() {
        initializeContents(backlog);
    }

    public void loadContents() {
        initializeContents(backlogId);
    }

    // AUTOGENERATED

    public int getBacklogId() {
        return backlogId;
    }

    public void setBacklogId(int backlogId) {
        this.backlogId = backlogId;
    }

    public Backlog getBacklog() {
        return backlog;
    }

    public void setBacklog(Backlog backlog) {
        this.backlog = backlog;
    }

    public Map<BacklogItem, List<BacklogItemResponsibleContainer>> getBacklogResponsibles() {
        return backlogResponsibles;
    }

    public void setBacklogResponsibles(
            Map<BacklogItem, List<BacklogItemResponsibleContainer>> backlogResponsibles) {
        this.backlogResponsibles = backlogResponsibles;
    }

    public Map<BacklogItem, List<BusinessTheme>> getBacklogThemes() {
        return backlogThemes;
    }

    public void setBacklogThemes(
            Map<BacklogItem, List<BusinessTheme>> backlogThemes) {
        this.backlogThemes = backlogThemes;
    }

    public Map<BacklogItem, TodoMetrics> getBacklogTodos() {
        return backlogTodos;
    }

    public void setBacklogTodos(Map<BacklogItem, TodoMetrics> backlogTodos) {
        this.backlogTodos = backlogTodos;
    }

    public void setBacklogBusiness(BacklogBusiness backlogBusiness) {
        this.backlogBusiness = backlogBusiness;
    }

    public List<BacklogItem> getBacklogItems() {
        return backlogItems;
    }

    public void setBacklogItems(List<BacklogItem> backlogItems) {
        this.backlogItems = backlogItems;
    }

    public void setBusinessThemeBusiness(BusinessThemeBusiness businessThemeBusiness) {
        this.businessThemeBusiness = businessThemeBusiness;
    }

    public void setBacklogItemBusiness(BacklogItemBusiness backlogItemBusiness) {
        this.backlogItemBusiness = backlogItemBusiness;
    }

    public void setHourEntryBusiness(HourEntryBusiness hourEntryBusiness) {
        this.hourEntryBusiness = hourEntryBusiness;
    }

    public EffortSumData getOrigEstSum() {
        return origEstSum;
    }

    public EffortSumData getEffortLeftSum() {
        return effortLeftSum;
    }

    public AFTime getSpentEffortSum() {
        return spentEffortSum;
    }

    public void setSettingBusiness(SettingBusiness settingBusiness) {
        this.settingBusiness = settingBusiness;
    }

}
