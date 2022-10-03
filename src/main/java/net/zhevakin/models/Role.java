package net.zhevakin.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String name;

	@ManyToMany(cascade=CascadeType.ALL,fetch= FetchType.LAZY,mappedBy="roles")
	private Set<User> users;
	
}
