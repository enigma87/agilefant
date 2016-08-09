
/**
 * Model class for products.
 * @constructor
 * @base BacklogModel
 * @see BacklogModel#initializeBacklogModel
 */
var PortfoliotypeModel = function PortfoliotypeModel() {
  this.initialize();
  this.persistedClassName = "fi.hut.soberit.agilefant.model.PortfolioType";
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

PortfoliotypeModel.prototype = new CommonModel();


/**
 * Internal function to set data
 * @see CommonModel#setData
 */
PortfoliotypeModel.prototype._setData = function(newData) {

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

PortfoliotypeModel.prototype.getName = function() {
  return this.currentData.name;
};

PortfoliotypeModel.prototype.getId = function() {
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

PortfoliotypeModel.prototype.getStories = function() {
  return this.relations.story;
};