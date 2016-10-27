package fi.hut.soberit.agilefant.business;

import java.util.List;

import fi.hut.soberit.agilefant.transfer.AgilefantHistoryEntry;

public interface GitBusiness {

	public Object getReport(List<Integer> taskIDs);
	public List<AgilefantHistoryEntry> dumpAllTaskRevisions();
	
}
