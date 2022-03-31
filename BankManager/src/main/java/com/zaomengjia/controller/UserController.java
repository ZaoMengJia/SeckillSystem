package com.zaomengjia.controller;


import com.zaomengjia.message.Result;
import com.zaomengjia.pojo.User;
import com.zaomengjia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserService userService;




    /**
     * 查询是否存在该用户或管理员
     * @param userName
     * @return
     */
    @GetMapping("/userNameExist/{userName}")
    public Result userNameExist(@PathVariable String userName){
        try{
            return Result.succ(userService.userExist(userName));
        }catch (Exception e){
            return Result.fail(500, e.getMessage(), false);
        }
    }

    /**
     * 分页获取所有用户
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getAllUser/{pageIndex}/{pageSize}")
    public Result getAllUser(@PathVariable int pageIndex, @PathVariable int pageSize){
        try{
            return Result.succ(userService.getUserList(pageIndex, pageSize));
        }catch (Exception e){
            return Result.fail(500, e.getMessage(), null);
        }
    }

    /**
     * 分页获取所有管理员
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getAllAdmin/{pageIndex}/{pageSize}")
    public Result getAllAdmin(@PathVariable int pageIndex, @PathVariable int pageSize){
        try{
            return Result.succ(userService.getAdminList(pageIndex, pageSize));
        }catch (Exception e){
            return Result.fail(500, e.getMessage(), null);
        }
    }

    /**
     * 通过id获取管理员
     * @param id
     * @return
     */
    @GetMapping(value = "/getAdminById/{id}")
    public Result getAdminById(@PathVariable int id){
        try{
            return Result.succ(userService.getAdminById(id));
        }catch (Exception e){
            return Result.fail(500, e.getMessage(), null);
        }
    }

    /**
     * 通过id获取用户
     * 获取用户信息
     * @param id
     * @return
     */
    @GetMapping(value = "/getUserById/{id}")
    public Result getUserById(@PathVariable int id){
        try{
            return Result.succ(userService.getUserById(id));
        }catch (Exception e){
            return Result.fail(500, e.getMessage(), null);
        }
    }

    /**
     *通过名字获取管理员
     * @param adminName
     * @return
     */
    @GetMapping(value = "/getAdminByName/{adminName}")
    public Result getAdminByName(@PathVariable String adminName){
        try{
            return Result.succ(userService.getAdminByName(adminName));
        }catch (Exception e){
            return Result.fail(500, e.getMessage(), null);
        }
    }

    /**
     *通过名字获取用户
     * @param userName
     * @return
     */
    @GetMapping(value = "/getUserByName/{userName}")
    public Result getUserByName(@PathVariable String userName){
        try{
            return Result.succ(userService.getUserByName(userName));
        }catch (Exception e){
            return Result.fail(500, e.getMessage(), null);
        }
    }

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @PostMapping(value = "/addAdmin")
    public Result addAdmin(@RequestBody User admin){
        try {
            admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
            admin.setAdmin(true);
            return Result.succ(userService.addUser(admin));
        } catch (Exception e) {
            return Result.fail(500, e.getMessage(), -1);
        }
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping(value = "/addUser")
    public Result addUser(@RequestBody User user){
        try {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            user.setAdmin(false);
            return Result.succ(userService.addUser(user));
        } catch (Exception e) {
            return Result.fail(500, e.getMessage(), -1);
        }
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteUser/{id}")
    public Result deleteUser(@PathVariable int id){
        try{
            return Result.succ(userService.deleteUser(id));
        } catch (Exception e){
            return Result.fail(500, e.getMessage(), -1);
        }
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PutMapping(value = "/updateUser")
    public Result updateAdmin(@RequestBody User user){
        try {
            return Result.succ(userService.updateUser(user));
        } catch (Exception e) {
            return Result.fail(500, e.getMessage(), -1);
        }
    }
}
