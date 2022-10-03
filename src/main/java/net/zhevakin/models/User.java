package net.zhevakin.models;

import lombok.Getter;
import lombok.Setter;
import net.zhevakin.models.enums.Provider;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String username;
	private String email;
	private String password;
	private boolean enabled;

	@OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
	private Set<Message> messages;

	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Transient
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="users")
	private Set<Chat> chats = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Role> roles = new HashSet<>();

	@Override
	public String toString() {
		return username;
	}

	public Set<Chat> getChats() {
		return chats;
	}

	public void setChats(Set<Chat> chats) {
		this.chats = chats;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
