/**
 * 
 */
package fi.hut.soberit.agilefant.web;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.business.IterationBusiness;
import fi.hut.soberit.agilefant.model.Iteration;;

/**
 * @author enigma
 *
 */
@Component("testPieAction")
@Scope("prototype")
public class TestPieActioin extends ActionSupport {
	
	private String pie = "Sweet!";
	
	HashMap<String, Integer> portfolioPoints;
	
	Iteration iteration;
	
	@Autowired
	private IterationBusiness iterationBusiness;
	
	public String retrieve() {
		iteration = iterationBusiness.retrieve(9);
		return Action.SUCCESS;
	}
	
	public HashMap<String, Integer> getPortfolioPoints() {
		return portfolioPoints;
	}

	public void setPortfolioPoints(HashMap<String, Integer> portfolioPoints) {
		this.portfolioPoints = portfolioPoints;
	}

	public String execute() throws Exception {
        return "success";
		
    }

	public String getPie() {
		return pie;
	}

	public void setPie(String pie) {
		this.pie = pie;
	}

	public String pieMetrics() {
		retrieve();
		portfolioPoints = iterationBusiness.calculateIterationPortfolio(iteration);
		return Action.SUCCESS;
	}
}
