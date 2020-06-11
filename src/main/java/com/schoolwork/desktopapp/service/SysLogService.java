package com.schoolwork.desktopapp.service;

import com.schoolwork.desktopapp.entity.SysLogBO;

import java.io.IOException;

public interface SysLogService {
    public boolean save(SysLogBO sysLogBO) throws IOException;
}
