package fi.hut.soberit.agilefant.web.widgets;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fi.hut.soberit.agilefant.business.AuthorizationBusiness;
import fi.hut.soberit.agilefant.business.BacklogBusiness;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.Iteration;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.transfer.IterationMetrics;

@Component("portfolioMetricsWidget")
@Scope("prototype")
public class portfolioMetricsWidget extends CommonWidget {

	HashMap<String, Integer> portfolioPoints;
	Backlog backlog;
	
	@Autowired
	private BacklogBusiness backlogBusiness;
	
	@Autowired
	private AuthorizationBusiness authorizationBusiness;
	
	@Override
	public String execute() {
		
		 backlog = backlogBusiness.retrieve(getObjectId());		 
	     portfolioPoints = backlogBusiness.calculateBacklogPortfolio(backlog);
	     return SUCCESS;
	}
	
	public HashMap<String, Integer> getPortfolioPoints() {
		return portfolioPoints;
	}

	public boolean getAccess() {
        User user = SecurityUtil.getLoggedUser();
        return  this.authorizationBusiness.isBacklogAccessible(backlog.getId(), user);
    }

    public Backlog getBacklog() {
        return backlog;
    }

}
