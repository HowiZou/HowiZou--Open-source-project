package com.itheima.ssm.service;

import com.itheima.ssm.domain.SysLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ISysLogService {


    public void save(SysLog sysLog) throws Exception;

    List<SysLog> findAll() throws Exception;
}
