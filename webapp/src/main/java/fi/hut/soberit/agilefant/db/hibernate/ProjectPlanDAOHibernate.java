package fi.hut.soberit.agilefant.db.hibernate;
import fi.hut.soberit.agilefant.db.ProjectPlanDAO;
import fi.hut.soberit.agilefant.db.StoryDAO;
import fi.hut.soberit.agilefant.model.ProjectPlan;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.StoryState;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("projectPlanDAO")
public class ProjectPlanDAOHibernate   extends GenericDAOHibernate<ProjectPlan> implements ProjectPlanDAO {

	protected ProjectPlanDAOHibernate() {
		super(ProjectPlan.class);
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private StoryDAO storyDAO;
	
	@Override
	public List<Story> GetProjectPlanStories(ProjectPlan projectPlan) {
		// TODO Auto-generated method stub
		System.out.println("wtf");
		Story story = new Story();
		story.setName("Story Example");
		story.setState(StoryState.STARTED);
		story.setTreeRank(1);
		projectPlan.getStories().add(storyDAO.get(80));
		
		Set<Story> stories = projectPlan.getStories();
		
		for (Story st : stories)
		{
			System.out.println(st);
		}
		
/*		Criteria crit = this.createCriteria(ProjectplanStory.class);
		Criterion criterion = Restrictions.eq("projectplan_id", projectPlan.getId());
		crit.add(criterion);
		
		try {
			CriteriaImpl criteriaImpl = (CriteriaImpl)crit;
			SessionImplementor session = criteriaImpl.getSession();
			SessionFactoryImplementor factory = session.getFactory();
			CriteriaQueryTranslator translator=new CriteriaQueryTranslator(factory,criteriaImpl,criteriaImpl.getEntityOrClassName(),CriteriaQueryTranslator.ROOT_SQL_ALIAS);
			String[] implementors = factory.getImplementors( criteriaImpl.getEntityOrClassName() );

			CriteriaJoinWalker walker = new CriteriaJoinWalker((OuterJoinLoadable)factory.getEntityPersister(implementors[0]), 
			                        translator,
			                        factory, 
			                        criteriaImpl, 
			                        criteriaImpl.getEntityOrClassName(), 
			                        session.getLoadQueryInfluencers()   );

			String sql=walker.getSQLString();
			System.out.println("\n\n\n"+ sql +"\n\n\n");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return asList(crit);
	*/	
		return null;
	}

}
