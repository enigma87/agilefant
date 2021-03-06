package fi.hut.soberit.agilefant.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name="portfoliotypes")
@Audited
@XmlRootElement
@XmlAccessorType( XmlAccessType.NONE )
public class PortfolioType {

	public PortfolioType() {
		
	}
	
	private int id;
	private String name;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@XmlAttribute(name = "objectId")
	@Column(name="id")
	public int getId() {
		return id;
	}
	public void setId(int portfolio_id) {
		this.id = portfolio_id;
	}
	
	
	@Column(nullable=false)
	@XmlAttribute
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}