package com.schoolwork.desktopapp.service.Imp;


import com.schoolwork.desktopapp.Log.Log;
import com.schoolwork.desktopapp.entity.SysLogBO;
import com.schoolwork.desktopapp.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Slf4j
@Service
public class SysLogServiceImp implements SysLogService {
    @Override
    public boolean save(SysLogBO sysLogBO) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String user = request.getSession().getAttribute("User").toString();
        Log.WriteLog(user, sysLogBO.getRemark() + "  " + sysLogBO.getParams(), sysLogBO.getCreateDate(), sysLogBO.getStatus());
        return true;

    }
}