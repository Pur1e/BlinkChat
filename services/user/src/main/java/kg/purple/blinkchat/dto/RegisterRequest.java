package kg.purple.blinkchat.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	@Min(value = 3, message = "min 3")
	@Max(value = 25, message = "max 25")
	private String username;
	@Min(value = 8)
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
	private String password;
	@NotBlank
	private String name;
	private String surname;
}
