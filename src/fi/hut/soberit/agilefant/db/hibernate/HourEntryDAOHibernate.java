package fi.hut.soberit.agilefant.db.hibernate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import fi.hut.soberit.agilefant.db.HourEntryDAO;
import fi.hut.soberit.agilefant.model.AFTime;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.BacklogItem;
import fi.hut.soberit.agilefant.model.BacklogItemHourEntry;
import fi.hut.soberit.agilefant.model.HourEntry;
import fi.hut.soberit.agilefant.model.User;

public class HourEntryDAOHibernate extends GenericDAOHibernate<HourEntry> implements
        HourEntryDAO{

    protected HourEntryDAOHibernate() {
        super(HourEntry.class);
    }
    
    /**
     * {@inheritDoc}
     */
    public AFTime getEffortSumByUserAndTimeInterval(User user, Date start, Date end) {
        DetachedCriteria criteria = DetachedCriteria.forClass(this
                .getPersistentClass());
        
        criteria.add(Restrictions.between("date", start, end));
        criteria.add(Restrictions.eq("user", user));
        criteria.setProjection(Projections.sum("timeSpent"));
        
        return (AFTime) super.getHibernateTemplate()
                            .findByCriteria(criteria).get(0);
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<HourEntry> getHourEntriesByUser(User user) {
        DetachedCriteria criteria = DetachedCriteria.forClass(this
                .getPersistentClass());
        
        criteria.add(Restrictions.eq("user", user));
        return (List<HourEntry>)super.getHibernateTemplate()
                            .findByCriteria(criteria);
    }
    
    @SuppressWarnings("unchecked")
    public Map<BacklogItem, AFTime> getSpentEffortSumsByBacklog(Backlog backlog) {
        DetachedCriteria crit = DetachedCriteria.forClass(BacklogItemHourEntry.class);
        crit.createAlias("backlogItem", "bli");
        crit.add(Restrictions.eq("bli.backlog", backlog));
        ProjectionList proj = Projections.projectionList();
        proj.add(Projections.groupProperty("backlogItem"));
        proj.add(Projections.sum("timeSpent"));
        crit.setProjection(proj);
        Map<BacklogItem, AFTime> result = new HashMap<BacklogItem, AFTime>();
        List<Object[]> data = this.getHibernateTemplate().findByCriteria(crit);
        try {
            for(Object[] item : data) {
                result.put((BacklogItem)item[0], (AFTime)item[1]);
            }
        } catch(Exception e) { }
        return result;
    }
}
