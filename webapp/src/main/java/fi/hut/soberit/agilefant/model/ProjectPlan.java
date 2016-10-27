package fi.hut.soberit.agilefant.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import flexjson.JSON;

@Entity
@Table(name = "projectplans")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ProjectPlan {

	public ProjectPlan() {

	}

	private int id;
	private String label;
	private Project project;
	private boolean committed;
	private Set<Story> stories = new HashSet<Story>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlAttribute(name = "objectId")
	@Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(nullable = true)
	@XmlAttribute
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@ManyToOne(targetEntity=Project.class)
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Column(nullable = true)
	@XmlAttribute
	public boolean getCommitted() {
		return committed;
	}

	public void setCommitted(boolean committed) {
		this.committed = committed;
	}
	
    @ManyToMany(
            targetEntity = fi.hut.soberit.agilefant.model.Story.class,
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "projectplan_story",
            		joinColumns={@JoinColumn(name = "projectplan_id")},
                    inverseJoinColumns={@JoinColumn(name = "story_id")}
    )
	public Set<Story> getStories() {
		return stories;
	}

	public void setStories(Set<Story> stories) {
		this.stories = stories;
	}

}
