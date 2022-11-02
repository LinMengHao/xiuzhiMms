package com.jiujia.khd.controller;

import com.alibaba.fastjson.JSON;
import com.jiujia.common.core.controller.BaseController;
import com.jiujia.common.core.domain.AjaxResult;
import com.jiujia.common.core.domain.entity.SysUser;
import com.jiujia.common.utils.http.HttpUtils;
import com.jiujia.common.utils.security.Md5Utils;
import com.jiujia.khd.domain.TApplication;
import com.jiujia.khd.domain.TModel;
import com.jiujia.khd.service.ITApplicationService;
import com.jiujia.khd.service.ITModelService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 发送静态，还是动态发送.测试
 * 
 * @author lixl
 * @date 2022-01-24
 */
@Controller
@RequestMapping("/mmskhd/staticSender")
public class MmsSenderController extends BaseController
{
    private String prefix = "mmskhd/staticSender";

    @Autowired
    private ITModelService tModelService;
    @Autowired
    private ITApplicationService tApplicationService;

    @Autowired
    private ITApplicationService iTApplicationService;

    @RequiresPermissions("mmskhd:staticSender:view")
    @GetMapping()
    public String mt(ModelMap mmap)
    {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();

        List<TModel> modellist = tModelService.selectTModelListN(user.getCompanyId());
        List<TApplication> applist = tApplicationService.selectTApplicationListN(user.getCompanyId());
//        logger.info("[applist][RECHARGE] data="+ JSONObject.toJSONString(applist));
        mmap.put("applist", applist);
        return prefix + "/mmsSender";
    }

    @RequiresPermissions("mmskhd:staticSender:view")
    @PostMapping("/signature")
    @ResponseBody
    public AjaxResult signature(Long appId, Long modelId, String param1, String title){
        logger.info("[LOGIN] appId=" + appId+"modelId="+modelId+"param1"+param1);
        String[] titleArr = title.split(",");
        if(titleArr.length > 200){
            return AjaxResult.error("单次提交号码不能超过200个");
        }
//        if(!ObjectUtils.isEmpty(param1)){
//            String[] param1Arr = param1.split(",");
//            if(param1Arr.length != titleArr.length) {
//                return AjaxResult.error("参数组与手机号码数量一致");
//            }
//        }
        TModel tModel = tModelService.selectTModelById(modelId);
        TApplication tApplication = iTApplicationService.selectTApplicationById(appId);
        String appid = tApplication.getAppName();
        Long timestamp = System.currentTimeMillis();
        //sign=md5(pwd+ appid + timestamp + pwd)
        String signStr = tApplication.getPassword()+appid+timestamp+tApplication.getPassword();
        String sign = Md5Utils.hash(signStr);
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("mms_from","");
        bodyMap.put("mms_id",tModel.getModelId());
        bodyMap.put("phones",title);
        if(!ObjectUtils.isEmpty(param1)){
//            bodyMap.put("templateParam",param1);
            String[] param1Arr = param1.split(",");
            List<Object> resultList = new ArrayList<>();
            List<Object> tempList = new ArrayList<>();
            for(String para : param1Arr){
                if(tempList.size() == 2){
                    resultList.add(tempList);
                    tempList = new ArrayList<>();
                }
                tempList.add(para);
            }
            if(tempList.size() > 0){
                resultList.add(tempList);
            }
            bodyMap.put("templateParam",JSON.toJSONString(resultList));
        }
        //base64
        String  body = Base64.getEncoder().encodeToString(JSON.toJSONString(bodyMap).getBytes());
        String url = "http://172.16.30.22:8090/mms_model/send";
        if(!ObjectUtils.isEmpty(param1)){
            url = "http://172.16.30.22:8090/mms_model/variable/send";
        }

        logger.info("url="+url+"body="+body+"appid="+appid+"sign="+sign);
        String result = HttpUtils.sendPost(url,"appid="+appid+"&timestamp="+timestamp+"&sign="+sign+"&body="+body+"");
        logger.info(result);
        return AjaxResult.success(JSON.parse(result));
    }



}
