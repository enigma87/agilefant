/**
 * 
 */
package fi.hut.soberit.agilefant.db.hibernate;

import org.springframework.stereotype.Repository;

import fi.hut.soberit.agilefant.db.PortfoliotypeDAO;
import fi.hut.soberit.agilefant.db.ProductfeatureDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.PortfolioType;
import fi.hut.soberit.agilefant.model.ProductFeature;

/**
 * @author enigma
 *
 */
@Repository("productfeatureDAO")
public class ProductfeatureDAOHibernate extends GenericDAOHibernate<ProductFeature> implements ProductfeatureDAO{

	public ProductfeatureDAOHibernate() {
        super(ProductFeature.class);
    }
}
