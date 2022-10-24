package com.edo.user.dto;

import com.edo.user.constant.Role;
import com.edo.user.entity.Users;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String usersName;

	@NotEmpty(message = "이메일은 필수 입력 값입니다.")
	private String usersEmail;

	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
	private String usersPassword;

	@NotNull(message = "핸드폰 번호는 필수 입력 값입니다.")
	private String usersPhone;

	@NotEmpty(message = "닉네임은 필수 입력 값입니다.")
	private String usersNickname;

//	role이 안 들어감...
//	@NotEmpty
//	private Role userRole;

	private static ModelMapper modelMapper = new ModelMapper();

	public Users createUsers(){
		return modelMapper.map(this, Users.class);
	}

	public static UserDto of(Users users){
		return modelMapper.map(users, UserDto.class);
	}
}
