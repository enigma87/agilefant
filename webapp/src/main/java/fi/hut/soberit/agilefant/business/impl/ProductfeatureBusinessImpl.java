package fi.hut.soberit.agilefant.business.impl;

import org.springframework.stereotype.Service;

import fi.hut.soberit.agilefant.business.ProductfeatureBusiness;
import fi.hut.soberit.agilefant.db.BacklogDAO;
import fi.hut.soberit.agilefant.db.ProductfeatureDAO;
import fi.hut.soberit.agilefant.model.Backlog;
import fi.hut.soberit.agilefant.model.ProductFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("productfeatureBusiness")
@Transactional
public class ProductfeatureBusinessImpl extends GenericBusinessImpl<ProductFeature> implements ProductfeatureBusiness{

	private ProductfeatureDAO productfeatureDAO;
	
	public ProductfeatureBusinessImpl() {
        super(ProductFeature.class);
    }

	@Autowired
	public void setProductfeatureDAO(ProductfeatureDAO productfeatureDAO) {
		this.genericDAO = productfeatureDAO;
		this.productfeatureDAO = productfeatureDAO;
		
	}
	
	
    
}
