
/**
 * Model class for products.
 * @constructor
 * @base BacklogModel
 * @see BacklogModel#initializeBacklogModel
 */
var ProductfeatureModel = function ProductfeatureModel() {
  this.initialize();
  this.persistedClassName = "fi.hut.soberit.agilefant.model.ProductFeature";
  this.relations = {
    story: []
  };
  this.currentData = {
  };
  this.copiedFields = {
    "name":   "name"
  };
  this.classNameToRelation = {
      "fi.hut.soberit.agilefant.model.Story":         "story"
  };
};

ProductfeatureModel.prototype = new CommonModel();


/**
 * Internal function to set data
 * @see CommonModel#setData
 */
ProductfeatureModel.prototype._setData = function(newData) {

  // Set the id
  this.id = newData.id;
  
  // Copy fields
  this._copyFields(newData);
  
  // Set stories
  if (newData.stories) {
    this._updateRelations(ModelFactory.types.story, newData.stories);
  }
    
};

/* GETTERS AND SETTERS */

ProductfeatureModel.prototype.getName = function() {
  return this.currentData.name;
};

ProductfeatureModel.prototype.getId = function() {
	if (this.currentData.id) {
		return this.currentData.id;
	} else {
		return this.id;
	}
}
/*
 * 
 *ProductModel.prototype.setName = function(name) {
  this.currentData.name = name;
};
*/

ProductfeatureModel.prototype.getStories = function() {
  return this.relations.story;
};