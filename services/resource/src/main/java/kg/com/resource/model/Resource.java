package kg.com.resource.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "resources")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Size(max = 100)
	@NotNull
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "description", length = Integer.MAX_VALUE)
	private String description;
	
	@Size(max = 100)
	@Column(name = "location", length = 100)
	private String location;
	
	@Size(max = 30)
	@Column(name = "status", length = 30)
	private String status;
	
	@NotNull
	@Column(name = "owner_id", nullable = false)
	private Long ownerId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(mappedBy = "resource")
	private Set<RentalPrice> rentalPrices = new LinkedHashSet<>();
	
}