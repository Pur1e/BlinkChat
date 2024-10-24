package kg.com.resource.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "rental_prices")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalPrice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Size(max = 20)
	@NotNull
	@Column(name = "rent_type", nullable = false, length = 20)
	private String rentType;
	
	@NotNull
	@Column(name = "price", nullable = false, precision = 8, scale = 2)
	private BigDecimal price;
	
	@Size(max = 3)
	@Column(name = "currency", length = 3)
	private String currency;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "resource_id", nullable = false)
	private Resource resource;
	
}