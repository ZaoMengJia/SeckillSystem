package com.zaomengjia.bankmanager.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.bankmanager.dto.LoginDto;
import com.zaomengjia.common.entity.User;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.bankmanager.service.OldUserService;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Deprecated
public class OldUserController {

    private final OldUserService oldUserService;

    public OldUserController(OldUserService oldUserService) {
        this.oldUserService = oldUserService;
    }

    @PostMapping("/login")
    public ResultVO<?> toLogin(@Validated @RequestBody LoginDto loginDto){
        User admin = oldUserService.getAdminByName(loginDto.getLoginName());
        if(admin.getPassword().equals(SecureUtil.md5(String.valueOf(loginDto.getPassword())))){
            return ResultUtils.success(MapUtil.builder()
                    .put("id",admin.getUid())
                    .put("sex",admin.isSex())
                    .put("userName",admin.getUserName())
                    .map());
        }else{
            return ResultUtils.error(ResultCode.NO_SUCH_ACCOUNT_ERROR);
        }
    }

    @GetMapping("/logout")
    public ResultVO<?> toLogout(){
        return ResultUtils.success(null);
    }

    /**
     * 查询是否存在该用户或管理员
     * @param userName
     * @return
     */
    @GetMapping("/userNameExist/{userName}")
    public ResultVO<?> userNameExist(@PathVariable String userName){
        try{
            return ResultUtils.success(oldUserService.userExist(userName));
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
            return ResultUtils.success(oldUserService.getUserList(pageIndex, pageSize));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 搜索管理员
     * @param keyword
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/searchAdmin/{keyword}/{pageIndex}/{pageSize}")
    public ResultVO<?> searchAdminList(@PathVariable String keyword,@PathVariable int pageIndex,@PathVariable int pageSize){
        try{
            return ResultUtils.success(oldUserService.searchAdminList(keyword, pageIndex, pageSize));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 搜索用户
     * @param keyword
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/searchUser/{keyword}/{pageIndex}/{pageSize}")
    public ResultVO<?> searchUserList(@PathVariable String keyword,@PathVariable int pageIndex,@PathVariable int pageSize){
        try{
            return ResultUtils.success(oldUserService.searchUserList(keyword, pageIndex, pageSize));
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
            return ResultUtils.success(oldUserService.getAdminList(pageIndex, pageSize));
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
            return ResultUtils.success(oldUserService.getAdminById(id));
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
            return ResultUtils.success(oldUserService.getUserById(id));
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
            return ResultUtils.success(oldUserService.getAdminByName(adminName));
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
            return ResultUtils.success(oldUserService.getUserByName(userName));
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
            admin.setType(1);
            oldUserService.addUser(admin);
            return ResultUtils.success();
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
            user.setType(0);
            oldUserService.addUser(user);
            return ResultUtils.success();
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
            oldUserService.deleteUser(id);
            return ResultUtils.success();
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
            oldUserService.updateUser(user);
            return ResultUtils.success();
        } catch (Exception e) {
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}