package fi.hut.soberit.agilefant.business.impl;

import org.springframework.stereotype.Service;

import fi.hut.soberit.agilefant.business.PortfoliotypeBusiness;
import fi.hut.soberit.agilefant.db.BacklogDAO;
import fi.hut.soberit.agilefant.db.PortfoliotypeDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.PortfolioType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("portfoliotypeBusiness")
@Transactional
public class PortfoliotypeBusinessImpl extends GenericBusinessImpl<PortfolioType> implements PortfoliotypeBusiness{

	private PortfoliotypeDAO portfoliotypeDAO;
	
	public PortfoliotypeBusinessImpl() {
        super(PortfolioType.class);
    }

	@Autowired
	public void setPortfoliotypeDAO(PortfoliotypeDAO portfoliotypeDAO) {
		this.genericDAO = portfoliotypeDAO;
		this.portfoliotypeDAO = portfoliotypeDAO;
		
	}
	
	
    
}
