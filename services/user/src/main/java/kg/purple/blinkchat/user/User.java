package kg.purple.blinkchat.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User  {
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
	@NotNull
	@Column(name = "surname", nullable = false, length = 50)
	private String surname;
	
	@NotNull
	@ColumnDefault("true")
	@Column(name = "enabled", nullable = false)
	private Boolean enabled = false;
	
}