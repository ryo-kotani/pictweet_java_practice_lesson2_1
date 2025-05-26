package in.tech_camp.pictweet.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.pictweet.entity.UserEntity;

@Mapper
public interface UserRepository {
  @Insert("INSERT INTO users (email, password, first_name, last_name) VALUES (#{email}, #{password}, #{firstName}, #{lastName})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(UserEntity user);

  @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
  boolean existsByEmail(String email);

  @Select("SELECT * FROM users WHERE email = #{email}")
  UserEntity findByEmail(String email);
}