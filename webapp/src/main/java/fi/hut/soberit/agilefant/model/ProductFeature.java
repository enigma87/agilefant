package fi.hut.soberit.agilefant.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;


@Entity
@Table(name="productfeatures")
@Audited
@XmlRootElement
@XmlAccessorType( XmlAccessType.NONE )
public class ProductFeature {

	public ProductFeature() {
		
	}
	
	private int id;
	private String name;
	private Product product;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@XmlAttribute(name = "objectId")
	@Column(name="id")
	public int getId() {
		return id;
	}
	public void setId(int feature_id) {
		this.id = feature_id;
	}
	
	
	@Column(nullable=false)
	@XmlAttribute
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	
	@ManyToOne(optional = true, targetEntity=Product.class)
    @JoinColumn(name="product_id")
    public Product getProduct() {
    	return product;
    }
    
    public void setProduct(Product product) {
    	this.product = product;
    }
   
	
	
}