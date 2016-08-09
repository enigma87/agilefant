/**
 * 
 */
package fi.hut.soberit.agilefant.db.hibernate;

import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.PortfoliotypeDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.PortfolioType;

/**
 * @author enigma
 *
 */
@Repository("portfoliotypeDAO")
public class PortfoliotypeDAOHibernate extends GenericDAOHibernate<PortfolioType> implements PortfoliotypeDAO{

	public PortfoliotypeDAOHibernate() {
        super(PortfolioType.class);
    }
}
