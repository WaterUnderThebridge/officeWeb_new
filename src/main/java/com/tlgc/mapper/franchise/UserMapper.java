package com.tlgc.mapper.franchise;


import com.tlgc.entity.Admin;
import com.tlgc.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


import java.util.HashMap;
import java.util.List;

/**
 * Created by TONY on 2017/10/2.
 */
@Repository
public interface UserMapper {
     List<Admin> getAll();

     Integer userUpdate(@Param("id") Integer id,@Param("username") String username,@Param("fullname") String fullname,@Param("password") String password,@Param("roleId") Integer roleId);

    @Insert("INSERT INTO TLG_User(username,fullname,password,roleId,createTime,modules,isdelete)" +
            "VALUES(#{username},#{fullname},#{password},#{roleId},getdate(),'',0)")
     Integer userAdd(@Param("username") String username,@Param("fullname") String fullname,@Param("password") String password,@Param("roleId") Integer roleId);

    @Update("update tlg_user set isdelete=1 where id=${id}")
    Integer userDel(@Param("id") Integer id);


    @Update("update tlg_user set city='${city}' where id=${userId}")
     Integer updateCity(@Param("userId") Integer userId, @Param("city") String city);

    @Select("select top 1 id,username,password,fullname,roleId from tlg_user where username=#{username} and isDelete=0 ")
    @Results({
            @Result(id=true,property="id",column="id"),@Result(property="username",column="username"),
            @Result(property="password",column="password"),@Result(property="fullname",column="fullname"),
            @Result(property="role",column="roleId",one=@One(select="com.tlgc.mapper.franchise.RoleMapper.getRoleById"))
    })

     User getUserByUsername(String username);
    @Select("select u.id,username,password,fullname,roleId,r.title role,u.isdelete from tlg_user u join tlg_roles r on u.roleId=r.id")
     List<HashMap>  getUserList();
}
