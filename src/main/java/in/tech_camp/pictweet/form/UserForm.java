package in.tech_camp.pictweet.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BindingResult;

import in.tech_camp.pictweet.validation.ValidationPriority1;
import in.tech_camp.pictweet.validation.ValidationPriority2;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserForm {
  @NotBlank(message = "First Name can't be blank", groups=ValidationPriority1.class)
  @Length(max=6, message="First Name should be less than 6 characters", groups=ValidationPriority2.class)
  private String firstName;

  @NotBlank(message = "Last Name can't be blank", groups=ValidationPriority1.class)
  @Length(max=6, message="Last Name should be less than 6 characters", groups=ValidationPriority2.class)
  private String lastName;

  @NotBlank(message = "Email can't be blank", groups=ValidationPriority1.class)
  @Email(message = "Email should be valid", groups=ValidationPriority2.class)
  private String email;

  @NotBlank(message = "Password can't be blank", groups=ValidationPriority1.class)
  @Length(min = 6, max = 128, message = "Password should be between 6 and 128 characters", groups=ValidationPriority2.class)
  private String password;

  private String passwordConfirmation;

  public void validatePasswordConfirmation(BindingResult result) {
      if (!password.equals(passwordConfirmation)) {
          result.rejectValue("passwordConfirmation", null, "Password confirmation doesn't match Password");
      }
  }
}