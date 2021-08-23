package com.eastbabel.service;

import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.notify.CreateNotifyReq;
import com.eastbabel.bo.notify.NotifyBo;

public interface NotifyService {

    void createNotify(CreateNotifyReq createNotifyReq);

    void deleteNotify(Integer id);

    void updateNotifyStatus(Integer id, Integer status);

    PagedResource<NotifyBo> getNotifys(Integer status, Integer page, Integer size);

}
