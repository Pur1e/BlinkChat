package kg.purple.blinkchat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Size(max = 25)
	@NotNull
	@Column(name = "username", nullable = false, length = 25)
	private String username;
	
	@Size(max = 255)
	@NotNull
	@Column(name = "password", nullable = false)
	private String password;
	
	@Size(max = 50)
	@NotNull
	@Column(name = "name", nullable = false, length = 50)
	private String name;
	
	@Size(max = 50)
	@Column(name = "surname", length = 50)
	private String surname;
	
	@NotNull
	@ColumnDefault("true")
	@Column(name = "enabled", nullable = false)
	private Boolean enabled;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return this.enabled;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
}