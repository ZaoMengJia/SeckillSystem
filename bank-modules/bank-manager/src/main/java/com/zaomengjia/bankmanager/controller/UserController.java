package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.pojo.User;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.bankmanager.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Todo: 网关里集成了认证模块，不需要使用shiro了

    /**
     * 查询是否存在该用户或管理员
     * @param userName
     * @return
     */
    @GetMapping("/userNameExist/{userName}")
    public ResultVO<?> userNameExist(@PathVariable String userName){
        try{
            return ResultUtils.success(userService.userExist(userName));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_ACCOUNT_ERROR, e.getMessage());
        }
    }

    /**
     * 分页获取所有用户
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getAllUser/{pageIndex}/{pageSize}")
    public ResultVO<?> getAllUser(@PathVariable int pageIndex, @PathVariable int pageSize){
        try{
            return ResultUtils.success(userService.getUserList(pageIndex, pageSize));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 分页获取所有管理员
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getAllAdmin/{pageIndex}/{pageSize}")
    public ResultVO<?> getAllAdmin(@PathVariable int pageIndex, @PathVariable int pageSize){
        try{
            return ResultUtils.success(userService.getAdminList(pageIndex, pageSize));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 通过id获取管理员
     * @param id
     * @return
     */
    @GetMapping(value = "/getAdminById/{id}")
    public ResultVO<?> getAdminById(@PathVariable int id){
        try{
            return ResultUtils.success(userService.getAdminById(id));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_ACCOUNT_ERROR, e.getMessage());
        }
    }

    /**
     * 通过id获取用户
     * 获取用户信息
     * @param id
     * @return
     */
    @GetMapping(value = "/getUserById/{id}")
    public ResultVO<?> getUserById(@PathVariable int id){
        try{
            return ResultUtils.success(userService.getUserById(id));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_ACCOUNT_ERROR, e.getMessage());
        }
    }

    /**
     *通过名字获取管理员
     * @param adminName
     * @return
     */
    @GetMapping(value = "/getAdminByName/{adminName}")
    public ResultVO<?> getAdminByName(@PathVariable String adminName){
        try{
            return ResultUtils.success(userService.getAdminByName(adminName));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_ACCOUNT_ERROR, e.getMessage());
        }
    }

    /**
     *通过名字获取用户
     * @param userName
     * @return
     */
    @GetMapping(value = "/getUserByName/{userName}")
    public ResultVO<?> getUserByName(@PathVariable String userName){
        try{
            return ResultUtils.success(userService.getUserByName(userName));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_ACCOUNT_ERROR, e.getMessage());
        }
    }

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @PostMapping(value = "/addAdmin")
    public ResultVO<?> addAdmin(@RequestBody User admin){
        try {
            admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
            admin.setAdmin(true);
            return ResultUtils.success(userService.addUser(admin));
        } catch (Exception e) {
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping(value = "/addUser")
    public ResultVO<?> addUser(@RequestBody User user){
        try {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            user.setAdmin(false);
            return ResultUtils.success(userService.addUser(user));
        } catch (Exception e) {
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteUser/{id}")
    public ResultVO<?> deleteUser(@PathVariable int id){
        try{
            return ResultUtils.success(userService.deleteUser(id));
        } catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PutMapping(value = "/updateUser")
    public ResultVO<?> updateUser(@RequestBody User user){
        try {
            return ResultUtils.success(userService.updateUser(user));
        } catch (Exception e) {
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
