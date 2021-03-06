package fi.hut.soberit.agilefant.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import fi.hut.soberit.agilefant.model.PortfolioType;
import fi.hut.soberit.agilefant.model.ProductFeature;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.test.AbstractHibernateTests;
import fi.hut.soberit.agilefant.util.StoryMetrics;
import junit.framework.Assert;

@ContextConfiguration
@Transactional
public class StoryDAOTest extends AbstractHibernateTests {

    @Autowired
    private StoryDAO storyDAO;
    
    
    @Test
    public void testGetStoryPointSumByIteration_secondBacklog() {
        executeClassSql();
        assertEquals(33, storyDAO.getStoryPointSumByIteration(3));
    }
    
    @Test
    public void testRemoveStoryWithStoryHourEntries() {
        executeClassSql();
        try {
            storyDAO.remove(2);
            forceFlush();
            fail("Exception not thrown.");
        } catch (ConstraintViolationException cve) {}
    }
    
    @Test
    public void testRemoveStoryWithTaskHourEntries() {
        executeClassSql();
        try {
            storyDAO.remove(3);
            forceFlush();
            fail("Exception not thrown.");
        } catch (ConstraintViolationException cve) {}
    }
    
    @Test
    public void testGetNumberOfResponsiblesByTask() {
        executeClassSql();
        Set<Integer> storyIds = new HashSet<Integer>(Arrays.asList(1, 2, 3));
        Map<Integer, Integer> actual = this.storyDAO
                .getNumOfResponsiblesByStory(storyIds);
        assertEquals(1, actual.size());
        assertEquals(1, (int) actual.get(1));
    }
    
    @Test
    public void testGetNumberOfResponsiblesByStory_noStory() {
        executeClassSql();
        Map<Integer, Integer> actual = this.storyDAO.getNumOfResponsiblesByStory(null);
        assertEquals(0, actual.size());
        actual = this.storyDAO.getNumOfResponsiblesByStory(new HashSet<Integer>());
        assertEquals(0, actual.size());
    }


   @Test
   public void testGetAllIterationStoriesByResponsibleAndInterval() {
       executeClassSql();
       DateTime start = new DateTime(2009,6,10,1,0,0,0);
       Interval interval = new Interval(start, start.plusDays(5));
       User user = new User();
       user.setId(1);
       Collection<Story> actual = this.storyDAO.getAllIterationStoriesByResponsibleAndInterval(user, interval);
      
       HashSet<Integer> actualIds = new HashSet<Integer>();
       for (Story t: actual) {
           actualIds.add(t.getId()); 
       }
       
       HashSet<Integer> expectedIds = new HashSet<Integer>(
           Arrays.asList(6,7)
       );        
       assertEquals(expectedIds, actualIds);
   }
       
   @Test
   public void testGetAllIterationStoriesByResponsibleAndInterval_user_hasNoAssigned() {
       executeClassSql();
       DateTime start = new DateTime(2009,6,10,1,0,0,0);
       Interval interval = new Interval(start, start.plusDays(5));
       User user = new User();
       user.setId(3);
      
       Collection<Story> actual = this.storyDAO.getAllIterationStoriesByResponsibleAndInterval(user, interval);
       assertEquals(0, actual.size());
   }

   @Test
   public void testGetAllIterationStoriesByResponsibleAndInterval_standaloneIteration() {
       executeClassSql();
       DateTime start = new DateTime(2009,6,10,1,0,0,0);
       Interval interval = new Interval(start, start.plusDays(5));
       User user = new User();
       user.setId(2);
       Collection<Story> actual = this.storyDAO.getAllIterationStoriesByResponsibleAndInterval(user, interval);
      
       HashSet<Integer> actualIds = new HashSet<Integer>();
       for (Story t: actual) {
           actualIds.add(t.getId()); 
       }
       
       HashSet<Integer> expectedIds = new HashSet<Integer>(Arrays.asList(6,10));        
       assertEquals(expectedIds, actualIds);
   }
   @Test
   public void testRetrieveActiveIterationStoriesWithUserResponsible() {
       executeSql("classpath:fi/hut/soberit/agilefant/db/StoryDAOTest-assignments-data.sql");
       List<Story> stories = storyDAO.retrieveActiveIterationStoriesWithUserResponsible(1);
       assertEquals(1, stories.size());
       assertEquals(1, stories.get(0).getId());
   }

   @Test
   public void testSearchByName() {
       String search  = "9";
       executeClassSql();
       List<Story> stories = storyDAO.searchByName(search);
       assertEquals(1, stories.size());
       assertEquals(9, stories.get(0).getId());
   }

   @Test
   public void testSearchByPortfolioType() {
	   int searchfolio = 1;
	   String name = "parity";
	   PortfolioType portfolio = new PortfolioType();
	   portfolio.setId(searchfolio);
	   portfolio.setName(name);
	   executeClassSql();
	   List<Story> stories = storyDAO.searchByPortfolioType(portfolio);
	   
	   assertEquals(4, stories.size());
/*	   for (Story s: stories) {
		   	System.err.println(s.getName() + " " + s.getPortfoliotype().getName());
	   }
	   
	   portfolio.setId(2);
	   portfolio.setName("tech-debt");
	   stories.get(1).setPortfoliotype(portfolio);
	   
	   for (Story s: stories) {
		   	System.err.println(s.getName() + " " + s.getPortfoliotype().getName());
	   }
	   
	   storyDAO.store(stories.get(1));
	   
	   List<Story> newstories = storyDAO.searchByPortfolioType(portfolio);
	   
	   assertEquals(1, newstories.size());
	   for (Story s: newstories) {
		   	System.err.println(s.getName() + " " + s.getPortfoliotype().getName());
	   }
*/ 	   
   }
   
   @Test
   public void testSerchByPortfolioType_notFound() {
	   int searchfolio =  -1;
	   PortfolioType portfolio = new PortfolioType();
	   portfolio.setId(searchfolio);
	   executeClassSql();
	   List<Story> stories = storyDAO.searchByPortfolioType(portfolio);
	   assertEquals(0, stories.size());
   }
   
   @Test
   public void testSerchByProductFeature() {
	   int searchfeature =  4;
	   ProductFeature feature = new ProductFeature();
	   feature.setId(searchfeature);
	   executeClassSql();
	   List<Story> stories = storyDAO.searchByProductFeature(feature);
	   assertEquals(2, stories.size());
	   assertFalse(1 == stories.size());
   }
   @Test
   public void testSerchByProductFeature_notFound() {
	   int searchfolio =  -1;
	   PortfolioType portfolio = new PortfolioType();
	   portfolio.setId(searchfolio);
	   executeClassSql();
	   List<Story> stories = storyDAO.searchByPortfolioType(portfolio);
	   assertEquals(0, stories.size());
   }
   @Test
   public void testSearchByName_notFound() {
       String search  = "not found string";
       executeClassSql();
       List<Story> stories = storyDAO.searchByName(search);
       assertEquals(0, stories.size());
   }
   
   
   @Test
   public void testCalculateMetrics() {
       executeClassSql();
       StoryMetrics actualMetrics = storyDAO.calculateMetrics(100);
       assertEquals(150L, actualMetrics.getEffortLeft());
       assertEquals(400L, actualMetrics.getOriginalEstimate());
   }
   
   @Test
   public void testCalculateMetrics_noTasks() {
       executeClassSql();
       StoryMetrics actualMetrics = storyDAO.calculateMetrics(4);
       assertEquals(0L, actualMetrics.getEffortLeft());
       assertEquals(0L, actualMetrics.getOriginalEstimate());    
   }
}
